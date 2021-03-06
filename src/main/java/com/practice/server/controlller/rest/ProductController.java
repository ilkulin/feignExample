package com.practice.server.controlller.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practice.server.entity.Product;
import com.practice.server.service.ProductService;
import com.practice.server.util.LocalDateAdapter;
import com.practice.server.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static java.util.Objects.nonNull;

public class ProductController extends HttpServlet {

    private Gson gson;
    private ProductService productService;
    private ServletHelper servletHelper;

    public ProductController() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        productService = new ProductService();
        servletHelper = ServletHelper.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        Product product = productService.getProductById(name);
        if (nonNull(product)) {
            servletHelper.sendJsonResponse(response, product, HttpServletResponse.SC_ACCEPTED);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productJson = servletHelper.getBody(request);
        Product product = gson.fromJson(productJson, Product.class);
        if (productService.saveProduct(product)) {
            servletHelper.sendJsonResponse(response, product, HttpServletResponse.SC_CREATED);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Data is not valid");
        }
    }

    public void setServletHelper(ServletHelper servletHelper) {
        this.servletHelper = servletHelper;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
