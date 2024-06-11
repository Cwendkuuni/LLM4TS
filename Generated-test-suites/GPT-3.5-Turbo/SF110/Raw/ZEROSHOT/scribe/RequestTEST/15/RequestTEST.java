package org.scribe.model;

import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "https://nonexistenturl.com");
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testAddHeaders() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");
        HttpURLConnection connection = (HttpURLConnection) new URL("https://example.com").openConnection();
        request.addHeaders(connection);
        assertNotNull(connection.getRequestProperties());
    }

    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.PUT, "https://example.com");
        HttpURLConnection connection = (HttpURLConnection) new URL("https://example.com").openConnection();
        byte[] content = "test".getBytes();
        request.addBody(connection, content);
        assertTrue(connection.getDoOutput());
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addQuerystringParameter("key", "value");
        assertEquals("value", request.getQueryStringParams().get("key"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "https://example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://example.com?key1=value1");
        request.addQuerystringParameter("key2", "value2");
        Map<String, String> params = request.getQueryStringParams();
        assertTrue(params.containsKey("key1"));
        assertTrue(params.containsKey("key2"));
        assertEquals("value1", params.get("key1"));
        assertEquals("value2", params.get("key2"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        Map<String, String> params = request.getBodyParams();
        assertTrue(params.containsKey("key1"));
        assertTrue(params.containsKey("key2"));
        assertEquals("value1", params.get("key1"));
        assertEquals("value2", params.get("key2"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals("https://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://example.com?key1=value1");
        assertEquals("https://example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addPayload("payload");
        byte[] expected = "payload".getBytes(Charset.defaultCharset());
        assertArrayEquals(expected, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertNull(request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertFalse(request.isConnectionKeepAlive());
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "https://example.com");
        HttpURLConnection connection = (HttpURLConnection) new URL("https://example.com").openConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
}