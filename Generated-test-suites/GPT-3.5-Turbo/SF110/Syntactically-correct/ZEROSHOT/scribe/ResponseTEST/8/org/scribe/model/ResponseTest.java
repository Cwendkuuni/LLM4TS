package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        InputStream inputStream = new ByteArrayInputStream("Test Body".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(inputStream);
        
        Response response = new Response(connection);
        
        // Ensure getBody returns the correct body
        Assert.assertEquals("Test Body", response.getBody());
    }
    
    @Test
    public void testGetStream() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        InputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(inputStream);
        
        Response response = new Response(connection);
        
        // Ensure getStream returns the correct input stream
        Assert.assertEquals(inputStream, response.getStream());
    }
    
    @Test
    public void testGetCode() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        
        Response response = new Response(connection);
        
        // Ensure getCode returns the correct code
        Assert.assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Mockito.when(connection.getHeaderFields()).thenReturn(headers);
        
        Response response = new Response(connection);
        
        // Ensure getHeaders returns the correct headers
        Assert.assertEquals(headers, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Mockito.when(connection.getHeaderFields()).thenReturn(headers);
        
        Response response = new Response(connection);
        
        // Ensure getHeader returns the correct header value
        Assert.assertEquals("application/json", response.getHeader("Content-Type"));
    }
    
    @Test
    public void testParseBodyContents() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        InputStream inputStream = new ByteArrayInputStream("Test Body".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(inputStream);
        
        Response response = new Response(connection);
        
        // Ensure parseBodyContents returns the correct body
        Assert.assertEquals("Test Body", response.getBody());
    }
    
    @Test
    public void testParseHeaders() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Map<String, java.util.List<String>> headerFields = new HashMap<>();
        java.util.List<String> values = new java.util.ArrayList<>();
        values.add("application/json");
        headerFields.put("Content-Type", values);
        Mockito.when(connection.getHeaderFields()).thenReturn(headerFields);
        
        Response response = new Response(connection);
        
        // Ensure parseHeaders returns the correct headers
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        Assert.assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testWasSuccessfulSuccess() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        
        Response response = new Response(connection);
        
        // Ensure wasSuccessful returns true when code is between 200 and 399
        Assert.assertTrue(response.wasSuccessful());
    }
    
    @Test
    public void testWasSuccessfulFailure() throws IOException {
        // Mock the HttpURLConnection
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connection.getResponseCode()).thenReturn(400);
        
        Response response = new Response(connection);
        
        // Ensure wasSuccessful returns false when code is not between 200 and 399
        Assert.assertFalse(response.wasSuccessful());
    }
}