package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testConstructor_WithConnection_Successful() throws IOException {
        HttpURLConnection connection = createConnection(200);
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test
    public void testConstructor_WithConnection_Unsuccessful() throws IOException {
        HttpURLConnection connection = createConnection(404);
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test(expected = UnknownHostException.class)
    public void testConstructor_WithConnection_UnknownHostException() throws IOException {
        HttpURLConnection connection = createConnectionUnknownHost();
        new Response(connection);
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        HttpURLConnection connection = createConnection(200);
        Map<String, String> headers = response.parseHeaders(connection);

        assertNotNull(headers);
        assertEquals("value1", headers.get("key1"));
        assertEquals("value2", headers.get("key2"));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.setCode(200);
        assertEquals(true, response.wasSuccessful());

        response.setCode(300);
        assertEquals(false, response.wasSuccessful());

        response.setCode(400);
        assertEquals(false, response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.setBody("test body");
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.setCode(200);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        response.setHeaders(headers);

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        response.setHeaders(headers);

        assertEquals("value1", response.getHeader("key1"));
        assertEquals("value2", response.getHeader("key2"));
    }

    // Helper methods

    private HttpURLConnection createConnection(int code) {
        return new HttpURLConnectionMock(code);
    }

    private HttpURLConnection createConnectionUnknownHost() {
        return new HttpURLConnectionUnknownHostMock();
    }

    private static class HttpURLConnectionMock extends HttpURLConnection {

        private int code;

        protected HttpURLConnectionMock(int code) {
            super(null);
            this.code = code;
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
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public int getResponseCode() throws IOException {
            return code;
        }
    }

    private static class HttpURLConnectionUnknownHostMock extends HttpURLConnection {

        protected HttpURLConnectionUnknownHostMock() {
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
            throw new UnknownHostException();
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
            return 0;
        }
    }
}