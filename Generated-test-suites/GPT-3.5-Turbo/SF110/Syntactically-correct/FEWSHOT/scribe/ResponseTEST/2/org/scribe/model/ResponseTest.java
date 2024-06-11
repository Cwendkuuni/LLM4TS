package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private Response response;

    @Before
    public void setup() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("https://example.com")) {
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
            public OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Test Response Body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream("Error Response Body".getBytes());
            }

            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<String, List<String>>();
                headers.put("header1", Arrays.asList("value1"));
                headers.put("header2", Arrays.asList("value2", "value3"));
                return headers;
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }
        };
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        assertEquals("Test Response Body", response.getBody());
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
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals("value1", headers.get("header1"));
        assertEquals("value2, value3", headers.get("header2"));
    }

    @Test
    public void testGetHeader() {
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2, value3", response.getHeader("header2"));
    }
}