package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private HttpURLConnection mockConnection;
    private InputStream mockInputStream;
    private Response response;
    
    @Before
    public void setup() throws IOException {
        mockConnection = new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }
            
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<String, List<String>>();
                headers.put("Content-Type", Arrays.asList("application/json"));
                headers.put("Expires", Arrays.asList("0"));
                return headers;
            }
            
            @Override
            public InputStream getInputStream() throws IOException {
                return mockInputStream;
            }
            
            @Override
            public InputStream getErrorStream() {
                return mockInputStream;
            }
        };
        
        mockInputStream = new ByteArrayInputStream("Mock Response".getBytes());
        
        response = new Response(mockConnection);
    }
    
    @Test
    public void testGetBodyWhenBodyIsNull() {
        assertNotNull(response.getBody());
        assertEquals("Mock Response", response.getBody());
    }
    
    @Test
    public void testGetBodyWhenBodyIsNotNull() throws IOException {
        response = new Response(mockConnection);
        response.getBody();

        assertNotNull(response.getBody());
        assertEquals("Mock Response", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
        assertEquals(2, response.getHeaders().size());
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
        assertEquals("0", response.getHeaders().get("Expires"));
    }
    
    @Test
    public void testGetHeader() {
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}