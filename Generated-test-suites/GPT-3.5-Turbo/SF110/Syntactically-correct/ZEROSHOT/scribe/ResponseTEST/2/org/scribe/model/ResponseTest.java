package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResponseTest {

    @Test
    public void testGetBody() {
        //create Response object with body
        Response response = new Response(null);
        response.body = "test body";

        //verify getBody() returns correct body
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetStream() {
        //create Response object with stream
        Response response = new Response(null);
        InputStream stream = new ByteArrayInputStream("test stream".getBytes());
        response.stream = stream;

        //verify getStream() returns correct stream
        assertEquals(stream, response.getStream());
    }

    @Test
    public void testGetCode() {
        //create Response object with code
        Response response = new Response(null);
        response.code = 200;

        //verify getCode() returns correct code
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        //create Response object with headers
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        response.headers = headers;

        //verify getHeaders() returns correct headers
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        //create Response object with headers
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        response.headers = headers;

        //verify getHeader() returns correct header value
        assertEquals("value1", response.getHeader("header1"));
        assertEquals("value2", response.getHeader("header2"));
    }

    @Test
    public void testParseHeaders() {
        //create HttpURLConnection with headers
        HttpURLConnection connection = new HttpURLConnection() {
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
            public InputStream getInputStream() throws IOException {
                return null;
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
            public String getHeaderField(String name) {
                if (name.equals("header1")) {
                    return "value1";
                } else if (name.equals("header2")) {
                    return "value2";
                } else {
                    return null;
                }
            }
        };

        //create Response object and parse headers
        Response response = new Response(connection);
        Map<String, String> headers = response.parseHeaders(connection);

        //verify parseHeaders() returns correct headers
        assertEquals(2, headers.size());
        assertEquals("value1", headers.get("header1"));
        assertEquals("value2", headers.get("header2"));
    }

    @Test
    public void testWasSuccessful() {
        //create Response object with code
        Response response = new Response(null);
        response.code = 200;

        //verify wasSuccessful() returns true for successful code
        assertTrue(response.wasSuccessful());

        //update code to unsuccessful value
        response.code = 500;

        //verify wasSuccessful() returns false for unsuccessful code
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testConstructor() {
        //create HttpURLConnection with successful response
        HttpURLConnection connection = new HttpURLConnection() {
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
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("test body".getBytes());
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
            public String getHeaderField(String name) {
                return null;
            }
        };

        //create Response object and check values
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertEquals("test body", response.getBody());
    }
}