
package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    private HttpURLConnection createHttpURLConnection(int responseCode, String body, Map<String, String> headers) {
        return new HttpURLConnection() {
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
                return responseCode;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream("Error Stream".getBytes());
            }

            @Override
            public Map<String, java.util.List<String>> getHeaderFields() {
                Map<String, java.util.List<String>> headerFields = new HashMap<>();
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    java.util.List<String> values = new java.util.ArrayList<>();
                    values.add(entry.getValue());
                    headerFields.put(entry.getKey(), values);
                }
                return headerFields;
            }
        };
    }

    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = createHttpURLConnection(200, "Body", new HashMap<>());
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertEquals("Body", response.getBody());
        assertNull(response.getStream());
        assertTrue(response.getHeaders().isEmpty());
        assertNull(response.getHeader("name"));
    }

    @Test
    public void testConstructorWithUnknownHostException() throws IOException {
        Response response = new Response(null);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        assertTrue(response.getHeaders().isEmpty());
        assertNull(response.getHeader("name"));
    }

    @Test
    public void testGetBody() throws IOException {
        HttpURLConnection connection = createHttpURLConnection(200, "Body", new HashMap<>());
        Response response = new Response(connection);

        assertEquals("Body", response.getBody());
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetStream() throws IOException {
        HttpURLConnection connection = createHttpURLConnection(200, "Body", new HashMap<>());
        Response response = new Response(connection);

        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        HttpURLConnection connection = createHttpURLConnection(200, "Body", new HashMap<>());
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");

        HttpURLConnection connection = createHttpURLConnection(200, "Body", headers);
        Response response = new Response(connection);

        assertEquals(headers, response.getHeaders());
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("Bearer token", response.getHeader("Authorization"));
        assertNull(response.getHeader("Invalid-Header"));
    }
}
