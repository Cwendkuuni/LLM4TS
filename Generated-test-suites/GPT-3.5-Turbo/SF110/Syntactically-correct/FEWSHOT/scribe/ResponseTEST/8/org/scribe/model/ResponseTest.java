package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.*;
import java.net.*;
import org.scribe.utils.StreamUtils;
import java.util.*;

public class ResponseTest {
    @Test
    public void testConstructor() throws IOException {
        URL url = new URL("https://example.com");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        Response response = new Response(connection);
        
        // Test code
        assertEquals(200, response.getCode());
        assertEquals("", response.getBody());
        assertEquals(null, response.getStream());
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = new Response(connection);
        
        // Test with body
        response.getBody();
        assertEquals("Testing body contents", response.getBody());
        
        // Test without body
        response = new Response(connection);
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        Response response = new Response(connection);
        
        // Test with headers
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        assertEquals(expectedHeaders, response.getHeaders());
        
        // Test without headers
        response = new Response(connection);
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }
    
    @Test
    public void testWasSuccessful() {
        Response response = new Response(connection);
        
        // Test successful code
        assertEquals(true, response.wasSuccessful());
        
        // Test unsuccessful code
        response = new Response(connection);
        assertEquals(false, response.wasSuccessful());
    }
    
    @Test
    public void testGetBody() {
        Response response = new Response(connection);
        
        // Test with body
        assertEquals("Testing body contents", response.getBody());
        
        // Test without body
        response = new Response(connection);
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(connection);
        
        // Test with stream
        InputStream expectedStream = new ByteArrayInputStream("Testing stream".getBytes());
        assertEquals(expectedStream, response.getStream());
        
        // Test without stream
        response = new Response(connection);
        assertEquals(null, response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(connection);
        
        // Test with code
        assertEquals(200, response.getCode());
        
        // Test without code
        response = new Response(connection);
        assertEquals(404, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(connection);
        
        // Test with headers
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        assertEquals(expectedHeaders, response.getHeaders());
        
        // Test without headers
        response = new Response(connection);
        assertEquals(Collections.emptyMap(), response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(connection);
        
        // Test with header
        assertEquals("application/json", response.getHeader("Content-Type"));
        
        // Test without header
        assertEquals(null, response.getHeader("Unknown-Header"));
    }
}