package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test(expected = OAuthException.class)
    public void testCreateConnectionIOException() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        request.createConnection();
    }

    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = null;
        request.addHeaders(connection);
        // No assertion as headers are added silently
    }

    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] content = "Test Content".getBytes();
        HttpURLConnection connection = null;
        request.addBody(connection, content);
        // No assertion as body is added silently
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("page", "1");
        Map<String, String> querystringParams = request.getQueryStringParams();
        assertTrue(querystringParams.containsKey("page"));
        assertEquals("1", querystringParams.get("page"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?page=1");
        request.addQuerystringParameter("limit", "10");
        Map<String, String> querystringParams = request.getQueryStringParams();
        assertTrue(querystringParams.containsKey("page"));
        assertTrue(querystringParams.containsKey("limit"));
        assertEquals("1", querystringParams.get("page"));
        assertEquals("10", querystringParams.get("limit"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?page=1");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
        byte[] byteBodyContents = request.getByteBodyContents();
        assertNotNull(byteBodyContents);
        assertEquals("username=testuser", new String(byteBodyContents));
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNull(request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = null;
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}