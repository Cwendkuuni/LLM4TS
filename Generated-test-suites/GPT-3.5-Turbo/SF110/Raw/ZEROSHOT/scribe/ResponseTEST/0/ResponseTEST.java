package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Arrange
        Response response;
        
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to initialize Response object.");
            return;
        }
        
        // Act
        String body = response.getBody();
        
        // Assert
        Assert.assertNotNull(body);
    }
    
    @Test
    public void testGetStream() {
        // Arrange
        Response response;
        
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to initialize Response object.");
            return;
        }
        
        // Act
        InputStream stream = response.getStream();
        
        // Assert
        Assert.assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() {
        // Arrange
        Response response;
        
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to initialize Response object.");
            return;
        }
        
        // Act
        int code = response.getCode();
        
        // Assert
        Assert.assertTrue(code >= 200 && code < 400);
    }
    
    @Test
    public void testGetHeaders() {
        // Arrange
        Response response;
        
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to initialize Response object.");
            return;
        }
        
        // Act
        Map<String, String> headers = response.getHeaders();
        
        // Assert
        Assert.assertNotNull(headers);
    }
    
    @Test
    public void testGetHeader() {
        // Arrange
        Response response;
        String headerName = "Content-Type";
        
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to initialize Response object.");
            return;
        }
        
        // Act
        String headerValue = response.getHeader(headerName);
        
        // Assert
        Assert.assertNotNull(headerValue);
    }
}