package org.scribe.model;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = createResponse();
        String expectedBody = ""; // empty body
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }
    
    @Test
    public void testGetStream() {
        Response response = createResponse();
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = createResponse();
        int expectedCode = 404;
        int actualCode = response.getCode();
        assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetHeaders() {
        Response response = createResponse();
        Map<String, String> expectedHeaders = new HashMap<>();
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }
    
    @Test
    public void testGetHeader() {
        Response response = createResponse();
        String headerName = "Content-Type";
        String expectedHeaderValue = null;
        String actualHeaderValue = response.getHeader(headerName);
        assertEquals(expectedHeaderValue, actualHeaderValue);
    }
    
    // Helper method to create a mock Response object
    private Response createResponse() {
        HttpURLConnection mockConnection = null;
        try {
            mockConnection = new HttpURLConnection(new URL("http://example.com")) {
                @Override
                public void disconnect() {}

                @Override
                public boolean usingProxy() {
                    return false;
                }

                @Override
                public void connect() throws IOException {}

                @Override
                public int getResponseCode() throws IOException {
                    return 404;
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
                public Map<String, List<String>> getHeaderFields() {
                    Map<String, List<String>> headers = new HashMap<>();
                    return headers;
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new Response(mockConnection);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}