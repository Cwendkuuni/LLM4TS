package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    private HttpURLConnection mockConnection;
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        mockConnection = mock(HttpURLConnection.class);
        response = new Response(mockConnection);
    }
    
    @Test
    public void testGetBody() {
        String body = "Example Body";
        response.body = body;
        String result = response.getBody();
        assertEquals(body, result);
    }
    
    @Test
    public void testGetBodyIsNull() {
        response.body = null;
        String result = response.getBody();
        assertNotNull(result);
    }
    
    @Test
    public void testGetStream() {
        InputStream stream = mock(InputStream.class);
        response.stream = stream;
        InputStream result = response.getStream();
        assertEquals(stream, result);
    }
    
    @Test
    public void testGetCode() {
        int code = 200;
        response.code = code;
        int result = response.getCode();
        assertEquals(code, result);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        response.headers = headers;
        Map<String, String> result = response.getHeaders();
        assertEquals(headers, result);
    }
    
    @Test
    public void testGetHeader() {
        String headerName = "Header1";
        String headerValue = "Value1";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(headerName, headerValue);
        response.headers = headers;
        String result = response.getHeader(headerName);
        assertEquals(headerValue, result);
    }
}