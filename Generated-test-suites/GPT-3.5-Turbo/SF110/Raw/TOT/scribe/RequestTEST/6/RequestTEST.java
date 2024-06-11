package org.scribe.model;

import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    // Test for constructor of Request class
    @Test
    public void testRequestConstructor() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        assertNotNull(request.getQueryStringParams());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getHeaders());
        assertFalse(request.getHeaders().isEmpty());
    }

    // Test for addHeader() method
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://example.com");
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);

        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }

    // Test for addBodyParameter() method
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://example.com");
        String key = "username";
        String value = "testUser";
        request.addBodyParameter(key, value);

        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }

    // Test for addQuerystringParameter() method
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://example.com");
        String key = "page";
        String value = "1";
        request.addQuerystringParameter(key, value);

        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    // Test for addPayload() method with String parameter
    @Test
    public void testAddPayloadWithString() {
        Request request = new Request(Verb.POST, "https://example.com");
        String payload = "Test payload";
        request.addPayload(payload);

        assertEquals(payload, request.getBodyContents());
    }

    // Test for addPayload() method with byte[] parameter
    @Test
    public void testAddPayloadWithByteArray() {
        Request request = new Request(Verb.POST, "https://example.com");
        byte[] payload = {0x01, 0x02, 0x03};
        request.addPayload(payload);

        assertArrayEquals(payload, request.getByteBodyContents());
    }

    // Test for getQueryStringParams() method
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://example.com?page=1");
        String key = "filter";
        String value = "true";
        request.addQuerystringParameter(key, value);

        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    // Test for getBodyParams() method
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "https://example.com");
        String key = "username";
        String value = "testUser";
        request.addBodyParameter(key, value);

        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }

    // Test for getUrl() method
    @Test
    public void testGetUrl() {
        String url = "https://example.com";
        Request request = new Request(Verb.GET, url);

        assertEquals(url, request.getUrl());
        assertEquals(url, request.getSanitizedUrl());
    }

    // Test for getBodyContents() method
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "https://example.com");
        String key = "username";
        String value = "testUser";
        request.addBodyParameter(key, value);

        assertEquals(key + "=" + value, request.getBodyContents());
    }

    // Test for getByteBodyContents() method
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "https://example.com");
        String key = "username";
        String value = "testUser";
        request.addBodyParameter(key, value);

        byte[] expected = (key + "=" + value).getBytes();
        assertArrayEquals(expected, request.getByteBodyContents());
    }

    // Test for getVerb() method
    @Test
    public void testGetVerb() {
        Verb verb = Verb.GET;
        Request request = new Request(verb, "https://example.com");

        assertEquals(verb, request.getVerb());
    }

    // Test for getHeaders() method
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://example.com");
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);

        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }

    // Test for getCharset() method
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        String charsetName = "UTF-8";
        request.setCharset(charsetName);

        assertEquals(charsetName, request.getCharset());
    }

    // Test for setConnectTimeout() method
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setConnectTimeout(duration, unit);

        assertEquals(duration, request.connection.getConnectTimeout());
    }

    // Test for setReadTimeout() method
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setReadTimeout(duration, unit);

        assertEquals(duration, request.connection.getReadTimeout());
    }

    // Test for setCharset() method
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        String charsetName = "UTF-8";
        request.setCharset(charsetName);

        assertEquals(charsetName, request.getCharset());
    }

    // Test for setConnectionKeepAlive() method
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://example.com");
        boolean connectionKeepAlive = true;
        request.setConnectionKeepAlive(connectionKeepAlive);

        assertTrue(request.connectionKeepAlive);
    }

    // Mock Response class for testing
    private class ResponseMock extends Response {
        public ResponseMock(HttpURLConnection connection) {
            super(connection);
        }
    }

    // Test for send() method with successful response
    @Test
    public void testSendSuccess() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void connect() throws IOException {
                // Mock connection establishment
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;   // Mock successful response
            }
        };

        request.setConnection(connectionMock);
        Response response = request.send();

        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    // Test for send() method with IOException
    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void connect() throws IOException {
                // Mock connection establishment
                throw new IOException();
            }
        };

        request.setConnection(connectionMock);
        request.send();
    }

    // Test for createConnection() method with existing connection
    @Test
    public void testCreateConnectionWithExistingConnection() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void connect() throws IOException {
                // Mock connection establishment
            }
        };
        request.setConnection(connectionMock);

        request.createConnection();
        assertEquals(connectionMock, request.connection);
    }

    // Test for createConnection() method without existing connection
    @Test
    public void testCreateConnectionWithoutExistingConnection() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");

        String effectiveUrl = "https://example.com?param=value";
        String expectedUrl = "https://example.com";

        Map<String, String> querystringParams = new HashMap<>();
        querystringParams.put("param", "value");
        request.addQuerystringParameter("filter", "true");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL(expectedUrl)) {
            @Override
            public void connect() throws IOException {
                // Mock connection establishment
            }
        };
        request.setConnection(null);

        request.createConnection();
        assertEquals(connectionMock.getRequestMethod(), request.getVerb().name());
        assertEquals(connectionMock.getRequestProperty("filter"), "true");
        assertEquals(connectionMock.getURL().toString(), expectedUrl);
        assertEquals(System.getProperty("http.keepAlive"), "false");
        assertEquals(connectionMock, request.connection);
    }

    // Test for doSend() method with PUT verb
    @Test
    public void testDoSendWithPutVerb() throws IOException {
        Request request = new Request(Verb.PUT, "https://example.com");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void setRequestMethod(String method) throws ProtocolException {
                assertEquals(method, Verb.PUT.name());
            }
        };
        request.setConnection(connectionMock);

        request.doSend();

        // Additional assertions can be added to validate other behaviors
    }

    // Test for doSend() method with POST verb
    @Test
    public void testDoSendWithPostVerb() throws IOException {
        Request request = new Request(Verb.POST, "https://example.com");

        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void setRequestMethod(String method) throws ProtocolException {
                assertEquals(method, Verb.POST.name());
            }
        };
        request.setConnection(connectionMock);

        request.doSend();

        // Additional assertions can be added to validate other behaviors
    }

    // Test for addHeaders() method
    @Test
    public void testAddHeaders() throws IOException {
        Request request = new Request(Verb.GET, "https://example.com");
        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            Map<String, String> requestHeaders = new HashMap<>();

            @Override
            public void setRequestProperty(String key, String value) {
                requestHeaders.put(key, value);
            }

            @Override
            public Map<String, String> getRequestProperties() {
                return requestHeaders;
            }
        };
        request.setConnection(connectionMock);
        request.addHeader("Content-Type", "application/json");

        request.addHeaders(connectionMock);

        assertTrue(connectionMock.getRequestProperties().containsKey("Content-Type"));
        assertEquals("application/json", connectionMock.getRequestProperties().get("Content-Type"));
    }

    // Test for addBody() method
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "https://example.com");

        byte[] bytePayload = {0x01, 0x02, 0x03};
        String contentLength = String.valueOf(bytePayload.length);
        HttpURLConnection connectionMock = new HttpURLConnection(new URL("https://example.com")) {
            String requestContentLength;

            @Override
            public void setRequestProperty(String key, String value) {
                if (key.equals("Content-Length")) {
                    requestContentLength = value;
                }
            }

            @Override
            public void setDoOutput(boolean dooutput) {
                assertTrue(dooutput);
            }

            @Override
            public OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }
        };
        request.setConnection(connectionMock);
        request.addPayload(bytePayload);

        request.addBody(connectionMock, bytePayload);

        assertEquals(contentLength, connectionMock.getRequestProperty("Content-Length"));
    }

    // Test for getSanitizedUrl() method
    @Test
    public void testGetSanitizedUrl() {
        String url = "https://example.com:8080";
        Request request = new Request(Verb.GET, url);

        assertEquals("https://example.com", request.getSanitizedUrl());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        String expected = "@Request(" + verb.name() + " " + url + ")";
        assertEquals(expected, request.toString());
    }

}