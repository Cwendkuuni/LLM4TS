package org.scribe.model;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void createConnection_WithValidUrl_CreatesHttpConnection() {
        Request request = new Request(Verb.GET, "https://api.example.com");

        try {
            request.createConnection();

            assertNotNull(request.getConnection());
        } catch (IOException e) {
            fail("Exception occurred while creating connection");
        }
    }

    @Test
    public void createConnection_WithInvalidUrl_ThrowsException() {
        Request request = new Request(Verb.GET, "invalid url");

        try {
            request.createConnection();

            fail("Exception should have been thrown");
        } catch (IOException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void doSend_WithValidRequest_ReturnsResponse() {
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setConnection(mock(HttpURLConnection.class));

        try {
            Response response = request.doSend();

            assertNotNull(response);
        } catch (IOException e) {
            fail("Exception occurred while sending request");
        }
    }

    @Test(expected = OAuthException.class)
    public void doSend_WithIOException_ThrowsOAuthException() throws IOException {
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setConnection(mock(HttpURLConnection.class));
        doThrow(new IOException()).when(request.getConnection()).setRequestMethod(anyString());

        request.doSend();
    }

    @Test
    public void addHeaders_WithValidHeaders_AddsHeadersToConnection() {
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setConnection(mock(HttpURLConnection.class));
        request.addHeader("Content-Type", "application/json");

        request.addHeaders(request.getConnection());

        Map<String, String> headers = request.getHeaders();
        HttpURLConnection connection = request.getConnection();
        headers.forEach((key, value) -> assertEquals(value, connection.getRequestProperty(key)));
    }

    @Test
    public void addBody_WithValidBody_AddsBodyToConnection() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.setConnection(mock(HttpURLConnection.class));
        byte[] body = "body".getBytes();

        try {
            request.addBody(request.getConnection(), body);

            assertEquals("4", request.getConnection().getRequestProperty("Content-Length"));
        } catch (IOException e) {
            fail("Exception occurred while adding body");
        }
    }

    @Test
    public void addHeader_WithValidKeyAndValue_AddsHeaderToRequestHeaders() {
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addHeader("Content-Type", "application/json");

        Map<String, String> headers = request.getHeaders();

        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void addBodyParameter_WithValidKeyAndValue_AddsBodyParameterToRequest() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("key", "value");

        Map<String, String> bodyParams = request.getBodyParams();

        assertTrue(bodyParams.containsKey("key"));
        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void addQuerystringParameter_WithValidKeyAndValue_AddsQuerystringParameterToRequest() {
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addQuerystringParameter("key", "value");

        Map<String, String> queryStringParams = request.getQueryStringParams();

        assertTrue(queryStringParams.containsKey("key"));
        assertEquals("value", queryStringParams.get("key"));
    }

    @Test
    public void addPayload_WithStringPayload_SetsStringPayloadInRequest() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addPayload("payload");

        assertEquals("payload", request.getPayload());
    }

    @Test
    public void addPayload_WithBytePayload_SetsBytePayloadInRequest() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);

        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void getQueryStringParams_WithValidUrl_ReturnsMergedQueryParams() {
        Request request = new Request(Verb.GET, "https://api.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> queryStringParams = request.getQueryStringParams();

        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test(expected = OAuthException.class)
    public void getQueryStringParams_WithMalformedUrl_ThrowsOAuthException() {
        Request request = new Request(Verb.GET, "invalid url");

        request.getQueryStringParams();
    }

    @Test
    public void getBodyParams_ReturnsBodyParams() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("key", "value");

        Map<String, String> bodyParams = request.getBodyParams();

        assertTrue(bodyParams.containsKey("key"));
        assertEquals("value", bodyParams.get("key"));
    }

    @Test
    public void getUrl_ReturnsUrl() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        assertEquals("https://api.example.com", request.getUrl());
    }

    @Test
    public void getSanitizedUrl_RemoveQueryStringAndPort_ReturnsSanitizedUrl() {
        Request request1 = new Request(Verb.GET, "https://api.example.com?param1=value1");
        Request request2 = new Request(Verb.POST, "https://api.example.com:8080");

        assertEquals("https://api.example.com", request1.getSanitizedUrl());
        assertEquals("https://api.example.com", request2.getSanitizedUrl());
    }

    @Test
    public void getBodyContents_ReturnsBodyContentsAsString() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.setCharset("UTF-8");
        request.addPayload("payload");

        assertEquals("payload", request.getBodyContents());
    }

    @Test(expected = OAuthException.class)
    public void getBodyContents_WithUnsupportedCharset_ThrowsOAuthException() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.setCharset("InvalidCharset");
        request.addPayload("payload");

        request.getBodyContents();
    }

    @Test
    public void getByteBodyContents_ReturnsByteBodyContents() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);

        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void getVerb_ReturnsVerb() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        assertEquals(Verb.POST, request.getVerb());
    }

    @Test
    public void getHeaders_ReturnsHeaders() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addHeader("Content-Type", "application/json");

        Map<String, String> headers = request.getHeaders();

        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void getCharset_WithCharsetSet_ReturnsCharset() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.setCharset("UTF-8");

        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void getCharset_WithCharsetNotSet_ReturnsDefaultCharset() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void setConnectTimeout_SetsConnectTimeout() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        request.setConnectTimeout(10, TimeUnit.SECONDS);

        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void setReadTimeout_SetsReadTimeout() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        request.setReadTimeout(10, TimeUnit.SECONDS);

        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void setCharset_SetsCharset() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        request.setCharset("UTF-8");

        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void setConnectionKeepAlive_SetsConnectionKeepAlive() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        request.setConnectionKeepAlive(true);

        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void setConnection_SetsConnection() {
        Request request = new Request(Verb.POST, "https://api.example.com");
        HttpURLConnection connection = mock(HttpURLConnection.class);

        request.setConnection(connection);

        assertEquals(connection, request.getConnection());
    }

    @Test
    public void toString_ReturnsStringRepresentation() {
        Request request = new Request(Verb.POST, "https://api.example.com");

        assertEquals("@Request(POST https://api.example.com)", request.toString());
    }
}