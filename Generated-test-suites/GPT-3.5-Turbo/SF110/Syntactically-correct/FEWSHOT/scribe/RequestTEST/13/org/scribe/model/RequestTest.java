package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.net.HttpURLConnection;

public class RequestTest {
    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testSend() {
        assertNotNull(request.send());
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() {
        request.setConnection(null);
        request.send();
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        assertEquals(1, request.getHeaders().size());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        assertEquals(1, request.getBodyParams().size());
        assertEquals("value1", request.getBodyParams().get("param1"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        assertEquals(1, request.getQueryStringParams().size());
        assertEquals("value1", request.getQueryStringParams().get("param1"));
    }

    @Test
    public void testAddPayloadWithString() {
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testAddPayloadWithByteArray() {
        byte[] payload = "payload".getBytes(Charset.defaultCharset());
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetUrl() {
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        byte[] payload = "payload".getBytes(Charset.defaultCharset());
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
        assertEquals(1, request.getHeaders().size());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
}