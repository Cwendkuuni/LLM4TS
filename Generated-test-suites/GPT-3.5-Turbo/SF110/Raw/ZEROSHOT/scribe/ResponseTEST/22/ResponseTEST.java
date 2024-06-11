package org.scribe.model;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testParseHeaders() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertNotNull(response.getHeaders());
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
    }
    
    @Test
    public void testWasSuccessful() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertTrue(response.wasSuccessful());
    }
    
    @Test
    public void testGetBody() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals("This is the response body", response.getBody());
    }
    
    @Test
    public void testGetStream() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnectionMock mock = new HttpURLConnectionMock();
        Response response = new Response(mock);
        
        assertNotNull(response);
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
    }
    
    private class HttpURLConnectionMock extends HttpURLConnection {

        public HttpURLConnectionMock() {
            super(null);
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public String getHeaderField(String name) {
            if (name.equals("header1")) {
                return "value1";
            } else if (name.equals("header2")) {
                return "value2";
            }
            return null;
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> headers = new HashMap<>();
            headers.put("header1", Collections.singletonList("value1"));
            headers.put("header2", Collections.singletonList("value2"));
            return headers;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream("This is the response body".getBytes());
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public int getResponseCode() throws IOException {
            return 200;
        }
    }
}