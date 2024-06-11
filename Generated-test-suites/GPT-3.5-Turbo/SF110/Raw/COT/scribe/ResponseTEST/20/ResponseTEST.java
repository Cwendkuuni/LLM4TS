package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        assertNull(response.getBody());
        
        response = new Response(getDummyConnection());
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(null);
        assertNull(response.getStream());
        
        response = new Response(getDummyConnection());
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        assertEquals(0, response.getCode());
        
        response = new Response(getDummyConnection());
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        assertNull(response.getHeaders());
        
        response = new Response(getDummyConnection());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        assertNull(response.getHeader("Content-Type"));
        
        response = new Response(getDummyConnection());
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
    
    private HttpURLConnection getDummyConnection() {
        // Create and return a dummy HttpURLConnection
    }
}