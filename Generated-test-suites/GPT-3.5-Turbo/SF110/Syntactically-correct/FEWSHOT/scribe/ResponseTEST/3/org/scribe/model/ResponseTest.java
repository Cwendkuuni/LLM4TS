package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    
    private HttpURLConnection connection;
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        connection = new HttpURLConnection(new URL("http://www.example.com"));
        response = new Response(connection);
    }
    
    @Test
    public void testGetBody() {
        assertNotNull(response.getBody());
        
        // Test with null body
        response.body = null;
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
        
        // Test with different code
        response.code = 404;
        assertEquals(404, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
        
        // Test with empty headers
        response.headers = new HashMap<>();
        assertTrue(response.getHeaders().isEmpty());
    }
    
    @Test
    public void testGetHeader() {
        assertNotNull(response.getHeader("Content-Type"));
        
        // Test with non-existent header
        assertNull(response.getHeader("Invalid-Header"));
        
        // Test with null header
        response.headers.put(null, "value");
        assertEquals("value", response.getHeader(null));
    }
    
    @Test
    public void testParseBodyContents() throws IOException {
        // Test with non-null body
        response.body = "Test body";
        assertEquals("Test body", response.parseBodyContents());
        
        // Test with null body
        response.body = null;
        
        // Test with non-null stream
        String expectedContents = "Test stream";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedContents.getBytes());
        response.stream = inputStream;
        String parsedContents = response.parseBodyContents();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        assertEquals(sb.toString(), parsedContents);
        
        // Test with null stream
        response.stream = null;
        assertNull(response.parseBodyContents());
    }
    
    @Test
    public void testParseHeaders() {
        // Test with non-null headers
        HttpURLConnection conn = new HttpURLConnection(new URL("http://www.example.com"));
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer token");
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Authorization", "Bearer token");
        Map<String, String> parsedHeaders = response.parseHeaders(conn);
        assertEquals(expectedHeaders, parsedHeaders);
        
        // Test with null headers
        conn.setHeaderFields(null);
        assertNotNull(response.parseHeaders(conn));
    }
    
    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
        
        // Test with unsuccessful code
        response.code = 500;
        assertFalse(response.wasSuccessful());
    }
}