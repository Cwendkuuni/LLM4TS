package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    @Test
    public void testSend() {
        // Test if send method returns a Response object
        Response response = request.send();
        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        // Test if IOException is thrown in case of problems while creating connection
        request.setUrl("http://nonexistenturl");
        request.send();
    }

    @Test
    public void testCreateConnection() {
        // Test if connection object is created
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test
    public void testCreateConnectionRepeated() {
        // Test if connection object is not created if it already exists
        request.createConnection();
        HttpURLConnection existingConnection = request.getConnection();
        request.createConnection();
        assertEquals(existingConnection, request.getConnection());
    }

    @Test
    public void testDoSend() {
        // Test if request method and headers are set correctly
        Response response = request.doSend();
        assertEquals(request.getVerb().name(), response.getRequestMethod());
        Map<String, String> expectedHeaders = request.getHeaders();
        for (String key : expectedHeaders.keySet()) {
            assertTrue(response.getHeaders().containsKey(key));
            assertEquals(expectedHeaders.get(key), response.getHeaders().get(key));
        }
    }

    @Test
    public void testAddHeaders() {
        // Test if headers are added correctly to the connection object
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com"));
        request.addHeaders(connection);
        Map<String, String> expectedHeaders = request.getHeaders();
        for (String key : expectedHeaders.keySet()) {
            assertTrue(connection.getRequestProperties().containsKey(key));
            assertEquals(expectedHeaders.get(key), connection.getRequestProperties().get(key).get(0));
        }
    }

    @Test
    public void testAddBody() throws IOException {
        // Test if Content-Length and body contents are set correctly
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com"));
        byte[] bodyContents = "Test body".getBytes();
        request.addBody(connection, bodyContents);
        assertTrue(connection.getRequestProperties().containsKey("Content-Length"));
        assertEquals(String.valueOf(bodyContents.length), connection.getRequestProperties().get("Content-Length").get(0));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        connection.getOutputStream().write(bodyContents);
        byte[] actualBodyContents = outputStream.toByteArray();
        assertArrayEquals(bodyContents, actualBodyContents);
    }

    @Test
    public void testAddHeader() {
        // Test if header is added to the request object
        String key = "TestHeader";
        String value = "TestValue";
        request.addHeader(key, value);
        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }

    @Test
    public void testAddBodyParameter() {
        // Test if body parameter is added to the request object
        String key = "TestParameter";
        String value = "TestValue";
        request.addBodyParameter(key, value);
        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Test if querystring parameter is added to the request object
        String key = "TestParameter";
        String value = "TestValue";
        request.addQuerystringParameter(key, value);
        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    @Test
    public void testAddPayloadString() {
        // Test if payload is set correctly as string
        String payload = "TestPayload";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        // Test if payload is set correctly as byte array
        byte[] payload = "TestPayload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        // Test if querystring params are returned correctly
        String url = "http://www.example.com?param1=value1&param2=value2";
        request.setUrl(url);
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("param1", "value1");
        expectedParams.put("param2", "value2");
        assertEquals(expectedParams, request.getQueryStringParams());
    }

    @Test
    public void testGetBodyParams() {
        // Test if body params are returned correctly
        String key1 = "param1";
        String value1 = "value1";
        String key2 = "param2";
        String value2 = "value2";
        request.addBodyParameter(key1, value1);
        request.addBodyParameter(key2, value2);
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put(key1, value1);
        expectedParams.put(key2, value2);
        assertEquals(expectedParams, request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        // Test if url is returned correctly
        String url = "http://www.example.com";
        request.setUrl(url);
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        // Test if sanitized url is returned correctly
        String url = "http://www.example.com?param=value";
        request.setUrl(url);
        String sanitizedUrl = "http://www.example.com";
        assertEquals(sanitizedUrl, request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        // Test if body contents are returned correctly as string
        String body = "Test body";
        byte[] byteBody = body.getBytes();
        request.addPayload(byteBody);
        assertEquals(body, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        // Test if body contents are returned correctly as byte array
        byte[] body = "Test body".getBytes();
        request.addPayload(body);
        assertArrayEquals(body, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        // Test if verb is returned correctly
        Verb verb = Verb.POST;
        request.setVerb(verb);
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        // Test if headers are returned correctly
        String key1 = "Header1";
        String value1 = "Value1";
        String key2 = "Header2";
        String value2 = "Value2";
        request.addHeader(key1, value1);
        request.addHeader(key2, value2);
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(key1, value1);
        expectedHeaders.put(key2, value2);
        assertEquals(expectedHeaders, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        // Test if charset is returned correctly
        String charsetName = "UTF-8";
        request.setCharset(charsetName);
        assertEquals(charsetName, request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        // Test if connect timeout is set correctly
        int duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setConnectTimeout(duration, unit);
        assertEquals(duration, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        // Test if read timeout is set correctly
        int duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setReadTimeout(duration, unit);
        assertEquals(duration, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        // Test if charset is set correctly
        String charsetName = "UTF-8";
        request.setCharset(charsetName);
        assertEquals(charsetName, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Test if connection keep alive is set correctly
        boolean connectionKeepAlive = true;
        request.setConnectionKeepAlive(connectionKeepAlive);
        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        // Test if connection is set correctly
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com"));
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        // Test if toString method returns expected format
        Verb verb = Verb.GET;
        String url = "http://www.example.com";
        request.setVerb(verb);
        request.setUrl(url);
        String expectedToString = "@Request(" + verb + " " + url + ")";
        assertEquals(expectedToString, request.toString());
    }
    
}