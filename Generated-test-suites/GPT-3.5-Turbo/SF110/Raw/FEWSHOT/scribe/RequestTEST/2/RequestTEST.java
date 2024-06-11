package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

public class RequestTest {
    
    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testCreateConnection() throws IOException {
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
        assertTrue(connection instanceof HttpURLConnection);
    }

    @Test(expected = OAuthException.class)
    public void testCreateConnectionIOException() throws IOException {
        request.setUrl("http://www.example.com/test");
        request.createConnection();
    }

    @Test
    public void testDoSendGet() throws IOException {
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(Verb.GET, response.getRequest().getVerb());
    }

    @Test
    public void testDoSendPut() throws IOException {
        request.setVerb(Verb.PUT);
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(Verb.PUT, response.getRequest().getVerb());
    }

    @Test
    public void testDoSendPost() throws IOException {
        request.setVerb(Verb.POST);
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(Verb.POST, response.getRequest().getVerb());
    }

    @Test
    public void testAddHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        request.addHeaders(headers);

        Map<String, String> requestHeaders = request.getHeaders();

        assertNotNull(requestHeaders);
        assertEquals(2, requestHeaders.size());
        assertTrue(requestHeaders.containsKey("Content-Type"));
        assertTrue(requestHeaders.containsKey("Accept"));
        assertEquals("application/json", requestHeaders.get("Content-Type"));
        assertEquals("application/json", requestHeaders.get("Accept"));
    }

    @Test
    public void testAddBody() throws IOException {
        byte[] content = "This is the request body".getBytes();
        request.addBody(request.getConnection(), content);

        assertNotNull(request.getConnection());
        assertEquals("This is the request body", new String(request.getConnection().getOutputStream().toString()));
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");

        Map<String, String> requestHeaders = request.getHeaders();

        assertNotNull(requestHeaders);
        assertEquals(1, requestHeaders.size());
        assertTrue(requestHeaders.containsKey("Content-Type"));
        assertEquals("application/json", requestHeaders.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        Map<String, String> bodyParams = request.getBodyParams();

        assertNotNull(bodyParams);
        assertEquals(2, bodyParams.size());
        assertTrue(bodyParams.containsKey("param1"));
        assertTrue(bodyParams.containsKey("param2"));
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> queryStringParams = request.getQueryStringParams();

        assertNotNull(queryStringParams);
        assertEquals(2, queryStringParams.size());
        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testAddPayload() {
        request.addPayload("{\"key\": \"value\"}");

        assertNotNull(request.getBodyContents());
        assertEquals("{\"key\": \"value\"}", request.getBodyContents());
    }

    @Test
    public void testAddPayloadBytes() {
        byte[] payload = "{\"key\": \"value\"}".getBytes();
        request.addPayload(payload);

        assertNotNull(request.getByteBodyContents());
        assertEquals(payload.length, request.getByteBodyContents().length);
    }

    @Test
    public void testGetSanitizedUrl() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());

        request.setUrl("http://www.example.com/test?param1=value1&param2=value2");

        assertEquals("http://www.example.com/test", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        request.addPayload("{\"key\": \"value\"}");

        assertNotNull(request.getBodyContents());
        assertEquals("{\"key\": \"value\"}", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        byte[] payload = "{\"key\": \"value\"}".getBytes();
        request.addPayload(payload);

        assertNotNull(request.getByteBodyContents());
        assertEquals(payload.length, request.getByteBodyContents().length);
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);

        assertEquals(5000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);

        assertEquals(5000, request.getConnection().getReadTimeout());
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

    @Test
    public void testToString() {
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}