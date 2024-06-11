package org.scribe.model;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testConstructorWithConnection() throws IOException {
        HttpURLConnection connection = null;
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        response.parseBodyContents();

        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        HttpURLConnection conn = null;
        Map<String, String> headers = response.parseHeaders(conn);

        assertNotNull(headers);
        assertEquals(new HashMap<String, String>(), headers);
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.setCode(200);

        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.setBody("test body");

        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(null);
        InputStream stream = null;
        response.setStream(stream);

        assertEquals(stream, response.getStream());
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
        HashMap<String, String> headers = new HashMap<>();
        response.setHeaders(headers);

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}