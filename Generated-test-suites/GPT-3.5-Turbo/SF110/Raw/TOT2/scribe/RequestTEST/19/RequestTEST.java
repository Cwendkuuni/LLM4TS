package org.scribe.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.MalformedURLException;

public class RequestTest {

    private Request request;

    @BeforeEach
    public void setUp() {
        request = new Request(Verb.GET, "https://www.example.com");
    }

    @Test
    public void testSend() {
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Authorization", "Bearer token");
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param", "value");
        assertEquals("value", request.getQueryStringParams().get("param"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.getByteBodyContents());
    }

    @Test
    public void testAddPayloadByte() {
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getHeaders().get("Authorization"));
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getHeaders().get("Authorization"));
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getHeaders().containsKey("Connection"));
    }

    @Test
    public void testMalformedURLException() {
        assertThrows(OAuthException.class, () -> request.getQueryStringParams());
    }

    @Test
    public void testUnsupportedEncodingException() {
        assertThrows(OAuthException.class, () -> request.getBodyContents());
    }

    @Test
    public void testIOException() {
        assertThrows(OAuthException.class, () -> request.doSend());
    }
}