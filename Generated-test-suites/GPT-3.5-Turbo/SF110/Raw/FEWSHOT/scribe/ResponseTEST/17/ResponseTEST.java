package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private Response response;
    private HttpURLConnection connection;

    @Before
    public void setup() {
        connection = new FakeHttpURLConnection();
        InputStream inputStream = new ByteArrayInputStream("Test Body".getBytes());
        FakeHttpURLConnection.setInputStream(inputStream);
        response = new Response(connection);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
        assertEquals(200, response.getCode());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testParseBodyContents() throws IOException {
        assertEquals("Test Body", response.getBody());
    }

    @Test
    public void testWasSuccessful() {
        assertEquals(true, response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        assertEquals("Test Body", response.getBody());
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

    @Test
    public void testGetHeaderNull() {
        assertNull(response.getHeader("NonExistingHeader"));
    }

    private class FakeHttpURLConnection extends HttpURLConnection {
        private static InputStream inputStream;

        public FakeHttpURLConnection() throws IOException {
            super(new URL("http://fakeurl.com"));
        }

        @Override
        public void connect() throws IOException {
            // Do nothing
        }

        @Override
        public void disconnect() {
            // Do nothing
        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        public static void setInputStream(InputStream in) {
            inputStream = in;
        }

        // Other overridden methods
    }
}