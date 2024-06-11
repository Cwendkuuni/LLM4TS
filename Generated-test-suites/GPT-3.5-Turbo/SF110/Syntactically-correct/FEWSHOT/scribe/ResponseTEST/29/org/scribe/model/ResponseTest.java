package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(null) {
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
        };
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        response.body = "test body";
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetStream() {
        InputStream inputStream = new ByteArrayInputStream("test stream".getBytes());
        response.stream = inputStream;
        assertEquals(inputStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        response.code = 200;
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        response.headers = headers;
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        response.headers = headers;
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
        assertNull(response.getHeader("header3"));
    }
}