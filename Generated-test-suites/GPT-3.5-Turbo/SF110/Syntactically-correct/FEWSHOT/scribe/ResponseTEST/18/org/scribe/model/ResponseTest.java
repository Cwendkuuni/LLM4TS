package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.net.UnknownHostException;
import org.scribe.utils.StreamUtils;

public class ResponseTest {

    private Response response;
    private HttpURLConnection connection;

    @Before
    public void setUp() throws IOException {
        connection = null; // initialize connection here
        response = new Response(connection);
    }

    @Test
    public void testParseBodyContents() {
        // Test when body is not null
        response.body = "This is the response body";
        assertEquals("This is the response body", response.parseBodyContents());

        // Test when body is null
        response.body = null;
        assertEquals("", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        // Test when connection has headers
        HttpURLConnection conn = null; // initialize conn here
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        assertEquals(headers, response.parseHeaders(conn));

        // Test when connection has no headers
        conn = null; // initialize conn here
        headers = new HashMap<>();
        assertEquals(headers, response.parseHeaders(conn));
    }

    @Test
    public void testWasSuccessful() {
        // Test when code is greater than or equal to 200 and less than 400
        response.code = 200;
        assertEquals(true, response.wasSuccessful());

        // Test when code is less than 200
        response.code = 199;
        assertEquals(false, response.wasSuccessful());

        // Test when code is greater than or equal to 400
        response.code = 400;
        assertEquals(false, response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        // Test when body is not null
        response.body = "This is the response body";
        assertEquals("This is the response body", response.getBody());

        // Test when body is null
        response.body = null;
        response.stream = new ByteArrayInputStream("This is the response body".getBytes());
        assertEquals("This is the response body", response.getBody());
    }

    @Test
    public void testGetStream() {
        // Test when stream is not null
        InputStream stream = new ByteArrayInputStream("This is the response body".getBytes());
        response.stream = stream;
        assertEquals(stream, response.getStream());

        // Test when stream is null
        response.stream = null;
        assertNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        response.code = 200;
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        // Test when headers is not null
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        response.headers = headers;
        assertEquals(headers, response.getHeaders());

        // Test when headers is null
        response.headers = null;
        assertNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        // Test when headers contains the given name
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        response.headers = headers;
        assertEquals("application/json", response.getHeader("Content-Type"));

        // Test when headers does not contain the given name
        assertNull(response.getHeader("Invalid-Header"));
    }
}