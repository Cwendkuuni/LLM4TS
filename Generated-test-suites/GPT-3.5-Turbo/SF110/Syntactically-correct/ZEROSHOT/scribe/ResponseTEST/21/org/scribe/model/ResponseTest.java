package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Test when body is null
        Response response = new Response(null);
        assertNull(response.getBody());
        
        // Test when body is not null
        response = new Response(createDummyConnection("dummyBody"));
        assertEquals("dummyBody", response.getBody());
    }

    @Test
    public void testGetStream() {
        // Test when stream is null
        Response response = new Response(null);
        assertNull(response.getStream());
        
        // Test when stream is not null
        response = new Response(createDummyConnection("dummyBody"));
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        // Test when code is 0
        Response response = new Response(null);
        assertEquals(0, response.getCode());
        
        // Test when code is not 0
        response = new Response(createDummyConnection("dummyBody"));
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        // Test when headers is null
        Response response = new Response(null);
        assertNull(response.getHeaders());
        
        // Test when headers is not null
        response = new Response(createDummyConnection("dummyBody"));
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        // Test when header is null
        Response response = new Response(null);
        assertNull(response.getHeader("dummyHeader"));
        
        // Test when header is not null
        response = new Response(createDummyConnection("dummyBody"));
        assertEquals("dummyHeader", response.getHeader("dummyHeader"));
    }

    private HttpURLConnection createDummyConnection(String body) {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public void disconnect() {}

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<String, List<String>>();
                List<String> values = new ArrayList<String>();
                values.add("dummyHeader");
                headers.put("dummyHeader", values);
                return headers;
            }
        };

        return connection;
    }
}