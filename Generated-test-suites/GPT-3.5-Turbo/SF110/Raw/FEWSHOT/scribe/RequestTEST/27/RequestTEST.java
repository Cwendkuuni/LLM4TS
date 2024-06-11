package org.scribe.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertEquals(new HashMap<String, String>(), request.getQueryStringParams());
        assertEquals(new HashMap<String, String>(), request.getBodyParams());
        assertEquals(new HashMap<String, String>(), request.getHeaders());
        assertNull(request.getSanitizedUrl());
        assertNull(request.getBodyContents());
        assertNull(request.getByteBodyContents());
        assertNull(request.getCharset());
        assertFalse(request.isConnectionKeepAlive());
    }

    @Test
    public void testSend() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        Response responseMock = mock(Response.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        when(connectionMock.getInputStream()).thenReturn(null);
        request.setConnection(connectionMock);

        Response response = request.send();

        assertEquals(responseMock, response);
        verify(connectionMock).setRequestMethod(verb.name());
        verify(connectionMock).connect();
        verify(connectionMock, times(2)).getResponseCode();
        verify(connectionMock).getInputStream();
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        Request request = new Request(verb, url);

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenThrow(new IOException());
        request.setConnection(connectionMock);

        request.send();
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");

        String key = "Key";
        String value = "Value";

        request.addHeader(key, value);

        Map<String, String> headers = new HashMap<>();
        headers.put(key, value);

        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://example.com");

        String key = "Key";
        String value = "Value";

        request.addBodyParameter(key, value);

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(key, value);

        assertEquals(bodyParams, request.getBodyParams());
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");

        String key = "Key";
        String value = "Value";

        request.addQuerystringParameter(key, value);

        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put(key, value);

        assertEquals(queryStringParams, request.getQueryStringParams());
    }

    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.GET, "http://example.com");

        String payload = "Payload";

        request.addPayload(payload);

        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.GET, "http://example.com");

        byte[] payload = {1, 2, 3};

        request.addPayload(payload);

        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testAddPayloadNull() {
        Request request = new Request(Verb.GET, "http://example.com");

        request.addPayload(null);

        assertNull(request.getPayload());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://example.com");

        String bodyContents = "BodyContents";

        request.addPayload(bodyContents);

        assertEquals(bodyContents, request.getBodyContents());
    }

    @Test
    public void testGetBodyContentsUnsupportedEncodingException() {
        Request request = new Request(Verb.GET, "http://example.com");

        byte[] bytePayload = {1, 2, 3};

        request.addPayload(bytePayload);

        assertNull(request.getBodyContents());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");

        String charset = "UTF-8";

        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");

        int duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        request.setConnectTimeout(duration, unit);

        verify(request.getConnection()).setConnectTimeout(duration);
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");

        int duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        request.setReadTimeout(duration, unit);

        verify(request.getConnection()).setReadTimeout(duration);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");

        boolean connectionKeepAlive = true;

        request.setConnectionKeepAlive(connectionKeepAlive);

        assertTrue(request.isConnectionKeepAlive());
    }

}