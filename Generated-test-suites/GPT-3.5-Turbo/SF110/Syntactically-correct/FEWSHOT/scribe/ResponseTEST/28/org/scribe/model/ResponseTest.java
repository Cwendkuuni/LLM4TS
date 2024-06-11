package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    
    private Response response;

    @Before
    public void setup() {
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com")) {
            @Override
            public void disconnect() {}
            
            @Override
            public boolean usingProxy() { return false; }
            
            @Override
            public void connect() throws IOException {}
        };
        
        response = new Response(connection);
    }

    @Test
    public void testConstructor() {
        assertEquals(200, response.getCode());
        assertNull(response.getBody());
        assertNull(response.getStream());
        assertTrue(response.getHeaders().isEmpty());
    }

    @Test
    public void testParseBodyContents() {
        InputStream stream = new ByteArrayInputStream("Test Body".getBytes());
        response.stream = stream;
        
        assertEquals("Test Body", response.parseBodyContents());
        assertEquals("Test Body", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com")) {
            @Override
            public void disconnect() {}
            
            @Override
            public boolean usingProxy() { return false; }
            
            @Override
            public void connect() throws IOException {}
            
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headerFields = new HashMap<>();
                headerFields.put("Content-Type", Arrays.asList("application/json"));
                headerFields.put("Cache-Control", Arrays.asList("no-cache"));
                return headerFields;
            }
        };
        
        response = new Response(connection);
        
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testWasSuccessful() {
        response.code = 200;
        assertTrue(response.wasSuccessful());
        
        response.code = 400;
        assertFalse(response.wasSuccessful());
        
        response.code = 404;
        assertFalse(response.wasSuccessful());
    }
    
    @Test
    public void testGetBody() {
        response.body = "Test Body";
        assertEquals("Test Body", response.getBody());
        
        response.body = null;
        response.stream = new ByteArrayInputStream("Test Body".getBytes());
        assertEquals("Test Body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNull(response.getStream());
        
        InputStream stream = new ByteArrayInputStream("Test Body".getBytes());
        response.stream = stream;
        
        assertEquals(stream, response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        response.headers.put("Content-Type", "application/json");
        assertEquals("application/json", response.getHeader("Content-Type"));
        
        assertNull(response.getHeader("Cache-Control"));
    }
}