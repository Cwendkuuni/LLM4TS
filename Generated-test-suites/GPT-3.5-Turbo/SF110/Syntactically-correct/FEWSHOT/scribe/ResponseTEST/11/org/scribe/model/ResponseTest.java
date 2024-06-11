package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(200);
        Response response = new Response(connection);
        
        assertEquals(200, response.getCode());
        assertNull(response.getStream());
        assertNull(response.getBody());
        assertEquals(0, response.getHeaders().size());
    }
    
    @Test
    public void testConstructor_UnsuccessfulConnection() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(500);
        Response response = new Response(connection);
        
        assertEquals(500, response.getCode());
        assertNull(response.getStream());
        assertNull(response.getBody());
        assertEquals(0, response.getHeaders().size());
    }
    
    @Test
    public void testConstructor_ConnectionException() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(new UnknownHostException());
        Response response = new Response(connection);
        
        assertEquals(404, response.getCode());
        assertNull(response.getStream());
        assertEquals("", response.getBody());
        assertEquals(0, response.getHeaders().size());
    }
    
    @Test
    public void testGetBody_NullBody() {
        Response response = new Response(null);
        assertNull(response.getBody());
    }
    
    @Test
    public void testGetBody_NonNullBody() throws IOException {
        byte[] bodyBytes = "Test body".getBytes();
        InputStream inputStream = new ByteArrayInputStream(bodyBytes);
        Response response = new Response(null);
        response.setStream(inputStream);
        
        assertEquals("Test body", response.getBody());
    }
    
    @Test
    public void testGetCode() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(200);
        Response response = new Response(connection);
        
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
        connection.setMockHeaders(headers);
        
        Response response = new Response(connection);
        Map<String, String> responseHeaders = response.getHeaders();
        
        assertEquals(2, responseHeaders.size());
        assertEquals("application/json", responseHeaders.get("Content-Type"));
        assertEquals("Thu, 01 Jan 1970 00:00:00 GMT", responseHeaders.get("Expires"));
    }
    
    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnection connection = new MockHttpURLConnection(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
        connection.setMockHeaders(headers);
        
        Response response = new Response(connection);
        
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("Thu, 01 Jan 1970 00:00:00 GMT", response.getHeader("Expires"));
    }
    
    private class MockHttpURLConnection extends HttpURLConnection {
        
        private int responseCode;
        private Map<String, String> mockHeaders;
        
        public MockHttpURLConnection(int responseCode) {
            super(null);
            this.responseCode = responseCode;
            this.mockHeaders = new HashMap<>();
        }
        
        public MockHttpURLConnection(UnknownHostException exception) {
            super(null);
            this.responseCode = 404;
            this.mockHeaders = new HashMap<>();
        }
        
        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }
        
        @Override
        public InputStream getErrorStream() {
            return null;
        }
        
        @Override
        public Map<String, java.util.List<String>> getHeaderFields() {
            Map<String, java.util.List<String>> headerFields = new HashMap<>();
            for (Map.Entry<String, String> entry : mockHeaders.entrySet()) {
                java.util.List<String> values = new ArrayList<>();
                values.add(entry.getValue());
                headerFields.put(entry.getKey(), values);
            }
            return headerFields;
        }
        
        public void setMockHeaders(Map<String, String> mockHeaders) {
            this.mockHeaders = mockHeaders;
        }
        
        @Override
        public void connect() throws IOException {
            // Do nothing
        }
    }
}