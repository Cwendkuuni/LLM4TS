package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResponseTest {
    @Test
    public void testConstructor() {
        // Test code 200
        HttpURLConnection connection200 = createMockConnection(200, "body", null);
        Response response200 = new Response(connection200);
        assertEquals(200, response200.getCode());
        assertEquals("body", response200.getBody());
        assertNull(response200.getStream());
        assertNull(response200.getHeaders());
        
        // Test code 404
        HttpURLConnection connection404 = createMockConnection(404, "", UnknownHostException.class);
        Response response404 = new Response(connection404);
        assertEquals(404, response404.getCode());
        assertEquals("", response404.getBody());
        assertNull(response404.getStream());
        assertNull(response404.getHeaders());
    }
    
    @Test
    public void testParseBodyContents() {
        HttpURLConnection connection = createMockConnection(200, "body", null);
        Response response = new Response(connection);
        assertEquals("body", response.parseBodyContents());
    }
    
    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = createMockConnection(200, "", null);
        Response response = new Response(connection);
        Map<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        assertEquals(headers, response.parseHeaders(connection));
    }
    
    @Test
    public void testWasSuccessful() {
        // Test code 200
        HttpURLConnection connection200 = createMockConnection(200, "", null);
        Response response200 = new Response(connection200);
        assertEquals(true, response200.wasSuccessful());
        
        // Test code 400
        HttpURLConnection connection400 = createMockConnection(400, "", null);
        Response response400 = new Response(connection400);
        assertEquals(false, response400.wasSuccessful());
    }
    
    @Test
    public void testGetBody() {
        HttpURLConnection connection = createMockConnection(200, "body", null);
        Response response = new Response(connection);
        assertEquals("body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        HttpURLConnection connection = createMockConnection(200, "", null);
        Response response = new Response(connection);
        assertNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        HttpURLConnection connection = createMockConnection(200, "", null);
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        HttpURLConnection connection = createMockConnection(200, "", null);
        Response response = new Response(connection);
        assertNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        HttpURLConnection connection = createMockConnection(200, "", null);
        Response response = new Response(connection);
        assertNull(response.getHeader("key"));
    }
    
    // Helper method to create a mock HttpURLConnection
    private HttpURLConnection createMockConnection(int code, String body, Class<?> exceptionClass) {
        try {
            HttpURLConnection connection = mock(HttpURLConnection.class);
            when(connection.getResponseCode()).thenReturn(code);
            when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
            when(connection.getErrorStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
            if (exceptionClass != null) {
                doThrow(exceptionClass).when(connection).connect();
            }
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}