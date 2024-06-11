package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.scribe.utils.StreamUtils;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response();
        assertEquals("body", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
        Response response = new Response(connection);
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        assertEquals(expectedHeaders, response.parseHeaders(connection));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response();
        response.setCode(200);
        assertTrue(response.wasSuccessful());
        response.setCode(400);
        assertFalse(response.wasSuccessful());
        response.setCode(500);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        Response response = new Response();
        assertEquals("body", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response();
        InputStream expectedStream = new InputStream();
        response.setStream(expectedStream);
        assertEquals(expectedStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response();
        response.setCode(200);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        response.setHeaders(expectedHeaders);
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/xml");
        response.setHeaders(headers);
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("application/xml", response.getHeader("Accept"));
    }
}