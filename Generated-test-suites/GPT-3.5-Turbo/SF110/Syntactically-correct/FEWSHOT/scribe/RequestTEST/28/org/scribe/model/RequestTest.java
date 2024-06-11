package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    private Request request;
    private Verb verb;
    private String url;

    @Before
    public void setUp() {
        this.verb = Verb.GET;
        this.url = "http://www.example.com";
        this.request = new Request(verb, url);
    }

    @After
    public void tearDown() {
        this.request = null;
        this.verb = null;
        this.url = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(request);
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testAddHeader() {
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);

        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey(key));
        assertEquals(value, headers.get(key));
    }

    @Test
    public void testAddBodyParameter() {
        String key = "name";
        String value = "John";
        request.addBodyParameter(key, value);

        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey(key));
        assertEquals(value, bodyParams.get(key));
    }

    @Test
    public void testAddQuerystringParameter() {
        String key = "page";
        String value = "1";
        request.addQuerystringParameter(key, value);

        Map<String, String> queryParams = request.getQueryStringParams();
        assertTrue(queryParams.containsKey(key));
        assertEquals(value, queryParams.get(key));
    }

    @Test
    public void testAddPayload_String() {
        String payload = "test payload";
        request.addPayload(payload);

        assertEquals(payload, request.getBodyContents());
    }

    @Test
    public void testAddPayload_byteArray() {
        byte[] payload = "test payload".getBytes();
        request.addPayload(payload);

        assertEquals(new String(payload), request.getBodyContents());
    }

    @Test
    public void testGetSanitizedUrl() {
        String sanitizedUrl = request.getSanitizedUrl();
        assertEquals(url, sanitizedUrl);
    }

    @Test
    public void testSetConnectTimeout() {
        int duration = 5000;
        request.setConnectTimeout(duration, TimeUnit.MILLISECONDS);

        int actualTimeout = request.getConnection().getConnectTimeout();
        assertEquals(duration, actualTimeout);
    }

    @Test
    public void testSetReadTimeout() {
        int duration = 5000;
        request.setReadTimeout(duration, TimeUnit.MILLISECONDS);

        int actualTimeout = request.getConnection().getReadTimeout();
        assertEquals(duration, actualTimeout);
    }

    @Test
    public void testSetCharset() {
        String charset = "UTF-8";
        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        boolean keepAlive = true;
        request.setConnectionKeepAlive(keepAlive);

        assertTrue(request.getConnectionKeepAlive());
    }

    // Add more test cases to comprehensively test all methods in the Request class

}