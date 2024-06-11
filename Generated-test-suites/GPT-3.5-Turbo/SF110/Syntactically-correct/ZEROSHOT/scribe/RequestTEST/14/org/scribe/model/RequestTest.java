package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://example.com");
    }

    @Test
    public void testSend() {
        // Test exception when creating connection
        try {
            request.send();
        } catch (OAuthException ex) {
            assertEquals("Problems while creating connection", ex.getMessage());
            assertNotNull(ex.getCause());
            assertTrue(ex.getCause() instanceof IOException);
        }
    }

    @Test
    public void testCreateConnection() throws IOException {
        request.createConnection();
        assertNotNull(request.getConnection());
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBody() throws IOException {
        byte[] content = "test body content".getBytes();
        request.addBody(request.getConnection(), content);
        assertEquals("Content-Length", request.getConnection().getRequestProperty("Content-Length"));
        assertTrue(request.getConnection().getDoOutput());
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(1, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(1, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("test payload");
        assertEquals("test payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByte() {
        byte[] payload = "test payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals(1, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
    }

    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals(1, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testGetUrl() {
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        request.addPayload("test payload");
        assertEquals("test payload", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        byte[] payload = "test payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(2000, TimeUnit.MILLISECONDS);
        assertEquals(2000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
}