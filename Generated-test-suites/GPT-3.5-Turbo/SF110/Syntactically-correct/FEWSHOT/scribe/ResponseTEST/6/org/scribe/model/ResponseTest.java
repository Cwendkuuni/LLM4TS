
package org.scribe.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ResponseTest {
    
    @Test
    public void testResponseConstructor() throws IOException {
        HttpURLConnection mockConnection = new MockHttpURLConnection();
        Response response = new Response(mockConnection);
        
        assertEquals(200, response.getCode());
        assertEquals("Test Body", response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testParseHeaders() throws IOException {
        HttpURLConnection mockConnection = new MockHttpURLConnection();
        Response response = new Response(mockConnection);
        
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("X-Header", "Test Header");
        
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnection mockConnection = new MockHttpURLConnection();
        Response response = new Response(mockConnection);
        
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("Test Header", response.getHeader("X-Header"));
        assertNull(response.getHeader("Invalid-Header"));
    }
    
    // Additional test cases can be added for the remaining methods
    
    // Helper class to mock HttpURLConnection
    private class MockHttpURLConnection extends HttpURLConnection {
        public MockHttpURLConnection() {
            super(null);
        }

        @Override
        public void disconnect() {}

        @Override
        public boolean usingProxy() {return false;}

        @Override
        public void connect() throws IOException {}

        @Override
        public InputStream getInputStream() {
            // Return a mock input stream
            String mockBody = "Test Body";
            InputStream inputStream = new ByteArrayInputStream(mockBody.getBytes());
            return inputStream;
        }

        @Override
        public InputStream getErrorStream() {
            // Return a mock error stream
            String errorBody = "Error Body";
            InputStream inputStream = new ByteArrayInputStream(errorBody.getBytes());
            return inputStream;
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, java.util.List<String>> getHeaderFields() {
            Map<String, java.util.List<String>> headers = new HashMap<>();
            headers.put("Content-Type", java.util.Arrays.asList("application/json"));
            headers.put("X-Header", java.util.Arrays.asList("Test Header"));
            return headers;
        }
    }
}

