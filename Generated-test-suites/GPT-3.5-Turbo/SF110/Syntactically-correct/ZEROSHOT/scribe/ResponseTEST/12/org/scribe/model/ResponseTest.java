package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {
    private Response response;

    @Before
    public void setUp() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public int getResponseCode() {
                return 200;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream("Response body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public Map<String, String> getHeaderFields() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        assertEquals("Response body", response.getBody());
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}