package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResponseTest {
    
    @Test
    public void testConstructor() {
        // Test with successful connection
        HttpURLConnection connection1 = null; // Replace with valid HttpURLConnection object
        Response response1 = new Response(connection1);
        assertEquals(200, response1.getCode());
        assertNotNull(response1.getHeaders());
        assertNotNull(response1.getStream());
        assertNotNull(response1.getBody());
        
        // Test with failed connection
        HttpURLConnection connection2 = null; // Replace with a HttpURLConnection object that throws UnknownHostException
        Response response2 = new Response(connection2);
        assertEquals(404, response2.getCode());
        assertNotNull(response2.getHeaders());
        assertNull(response2.getStream());
        assertNotNull(response2.getBody());
    }
    
    @Test 
    public void testParseBodyContents() {
        // Setup
        Response response = new Response(null);
        assertNull(response.getBody());
        
        // Test
        String body = response.parseBodyContents();
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        // Setup
        Response response = new Response(null);
        HttpURLConnection conn = null; // Replace with valid HttpURLConnection object
        
        // Test
        Map<String, String> headers = response.parseHeaders(conn);
        assertNotNull(headers);
    }
    
    @Test
    public void testWasSuccessful() {
        // Setup
        Response response = new Response(null);
        response.setCode(200);
        
        // Test
        boolean result = response.wasSuccessful();
        assertEquals(true, result);
        
        // Setup
        response.setCode(400);
        
        // Test
        result = response.wasSuccessful();
        assertEquals(false, result);
    }
    
    @Test
    public void testGetBody() {
        // Setup
        Response response = new Response(null);
        response.setBody("test");
        
        // Test
        String body = response.getBody();
        assertEquals("test", body);
    }
    
    @Test
    public void testGetStream() {
        // Setup
        Response response = new Response(null);
        InputStream stream = new InputStream(); // Replace with valid InputStream object
        response.setStream(stream);
        
        // Test
        InputStream result = response.getStream();
        assertNotNull(result);
    }
    
    @Test
    public void testGetCode() {
        // Setup
        Response response = new Response(null);
        response.setCode(200);
        
        // Test
        int code = response.getCode();
        assertEquals(200, code);
    }
    
    @Test
    public void testGetHeaders() {
        // Setup
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>(); // Replace with valid headers map
        response.setHeaders(headers);
        
        // Test
        Map<String, String> result = response.getHeaders();
        assertNotNull(result);
    }
    
    @Test
    public void testGetHeader() {
        // Setup
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>(); // Replace with valid headers map
        response.setHeaders(headers);
        
        // Test
        String header = response.getHeader("key");
        assertNotNull(header);
    }
}