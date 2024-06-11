package org.scribe.model;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {
    private static final String URL = "https://www.example.com";
    private static final String PAYLOAD = "sample payload";
    private static final byte[] BYTE_PAYLOAD = PAYLOAD.getBytes(StandardCharsets.UTF_8);
    private Request request;

    @Test
    public void testConstructor() {
        request = new Request(Verb.GET, URL);
        assertNotNull(request);
    }

    @Test
    public void testSend() {
        request = new Request(Verb.GET, URL);
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() throws IOException {
        request = new Request(Verb.GET, URL);
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testDoSend() throws IOException {
        request = new Request(Verb.GET, URL);
        request.createConnection();
        Response response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testAddHeaders() throws IOException {
        request = new Request(Verb.GET, URL);
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        request.addHeaders(connection);
        assertNotNull(connection.getRequestProperty("Content-Length"));
    }

    @Test
    public void testAddBody() throws IOException {
        request = new Request(Verb.POST, URL);
        request.setBytePayload(BYTE_PAYLOAD);
        request.setCharset(StandardCharsets.UTF_8.name());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpURLConnection connection = createMockHttpURLConnection(outputStream);

        request.addBody(connection, BYTE_PAYLOAD);
        byte[] requestBody = outputStream.toByteArray();

        assertNotNull(requestBody);
        assertEquals(new String(BYTE_PAYLOAD, StandardCharsets.UTF_8), new String(requestBody, StandardCharsets.UTF_8));
    }

    private HttpURLConnection createMockHttpURLConnection(OutputStream outputStream) throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL(URL)) {
            @Override
            public void connect() throws IOException {
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return outputStream;
            }
        };
        request.setConnection(connection);
        return connection;
    }

    @Test
    public void testAddHeader() {
        request = new Request(Verb.GET, URL);
        request.addHeader("key", "value");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals("value", headers.get("key"));
    }

    @Test
    public void testAddBodyParameter() {
        request = new Request(Verb.POST, URL);
        request.addBodyParameter("key", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request = new Request(Verb.GET, URL);
        request.addQuerystringParameter("key", "value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals("value", queryStringParams.get("key"));
    }

    @Test
    public void testAddPayload() {
        request = new Request(Verb.POST, URL);
        request.addPayload(PAYLOAD);
        assertNotNull(request.getPayload());
        assertEquals(PAYLOAD, request.getPayload());
    }

    @Test
    public void testAddBytePayload() {
        request = new Request(Verb.POST, URL);
        request.addPayload(BYTE_PAYLOAD);
        assertNotNull(request.getBytePayload());
        assertArrayEquals(BYTE_PAYLOAD, request.getBytePayload());
    }

    @Test
    public void testGetUrl() {
        request = new Request(Verb.GET, URL);
        assertEquals(URL, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        request = new Request(Verb.GET, URL + "?key=value");
        String sanitizedUrl = request.getSanitizedUrl();
        assertEquals(URL, sanitizedUrl);
    }

    @Test
    public void testGetBodyContents() {
        request = new Request(Verb.POST, URL);
        request.addPayload(PAYLOAD);
        assertEquals(PAYLOAD, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        request = new Request(Verb.POST, URL);
        request.addPayload(BYTE_PAYLOAD);
        byte[] byteBodyContents = request.getByteBodyContents();
        assertNotNull(byteBodyContents);
        assertArrayEquals(BYTE_PAYLOAD, byteBodyContents);
    }

    @Test
    public void testGetVerb() {
        request = new Request(Verb.GET, URL);
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("key1", "value1");
        headers.put("key2", "value2");
        request = new Request(Verb.GET, URL);
        request.setHeaders(headers);
        Map<String, String> actualHeaders = request.getHeaders();
        assertNotNull(actualHeaders);
        assertEquals(headers, actualHeaders);
    }

    @Test
    public void testGetCharset() {
        request = new Request(Verb.GET, URL);
        assertEquals(StandardCharsets.UTF_8.name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request = new Request(Verb.GET, URL);
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request = new Request(Verb.GET, URL);
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request = new Request(Verb.GET, URL);
        request.setCharset(StandardCharsets.UTF_8.name());
        assertEquals(StandardCharsets.UTF_8.name(), request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request = new Request(Verb.GET, URL);
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
}