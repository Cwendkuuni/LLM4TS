package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {
    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");

        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNull(request.getHeaders());
        assertNull(request.getQueryStringParams());
        assertNull(request.getBodyParams());
    }

    @Test
    public void testSend() throws IOException {
        // TODO: Implement test cases for send()
    }

    @Test
    public void testCreateConnection() throws IOException {
        // TODO: Implement test cases for createConnection()
    }

    @Test
    public void testDoSend() throws IOException {
        // TODO: Implement test cases for doSend()
    }

    @Test
    public void testAddHeaders() {
        // TODO: Implement test cases for addHeaders()
    }

    @Test
    public void testAddBody() throws IOException {
        // TODO: Implement test cases for addBody()
    }

    @Test
    public void testAddHeader() {
        // TODO: Implement test cases for addHeader()
    }

    @Test
    public void testAddBodyParameter() {
        // TODO: Implement test cases for addBodyParameter()
    }

    @Test
    public void testAddQuerystringParameter() {
        // TODO: Implement test cases for addQuerystringParameter()
    }

    @Test
    public void testAddPayload() {
        // TODO: Implement test cases for addPayload()
    }

    @Test
    public void testGetQueryStringParams() {
        // TODO: Implement test cases for getQueryStringParams()
    }

    @Test
    public void testGetBodyParams() {
        // TODO: Implement test cases for getBodyParams()
    }

    @Test
    public void testGetUrl() {
        // TODO: Implement test cases for getUrl()
    }

    @Test
    public void testGetSanitizedUrl() {
        // TODO: Implement test cases for getSanitizedUrl()
    }

    @Test
    public void testGetBodyContents() {
        // TODO: Implement test cases for getBodyContents()
    }

    @Test
    public void testGetByteBodyContents() {
        // TODO: Implement test cases for getByteBodyContents()
    }

    @Test
    public void testGetVerb() {
        // TODO: Implement test cases for getVerb()
    }

    @Test
    public void testGetHeaders() {
        // TODO: Implement test cases for getHeaders()
    }

    @Test
    public void testGetCharset() {
        // TODO: Implement test cases for getCharset()
    }

    @Test
    public void testSetConnectTimeout() {
        // TODO: Implement test cases for setConnectTimeout()
    }

    @Test
    public void testSetReadTimeout() {
        // TODO: Implement test cases for setReadTimeout()
    }

    @Test
    public void testSetCharset() {
        // TODO: Implement test cases for setCharset()
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // TODO: Implement test cases for setConnectionKeepAlive()
    }

    @Test
    public void testSetConnection() {
        // TODO: Implement test cases for setConnection()
    }

    @Test
    public void testToString() {
        // TODO: Implement test cases for toString()
    }
}