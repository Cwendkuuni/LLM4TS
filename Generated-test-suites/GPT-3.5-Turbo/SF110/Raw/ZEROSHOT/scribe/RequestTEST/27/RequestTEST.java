package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {
    Request request;
    Response response;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
        response = new Response(null);
    }

    @Test
    public void testSend() {
        response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("createConnection raised an exception");
        }
    }

    @Test
    public void testDoSendPUT() {
        request.addHeader("Content-Type", "application/json");
        request.addBodyParameter("key", "value");
        request.setCharset("utf-8");
        request.setConnectionKeepAlive(true);
        response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testDoSendPOST() {
        request.addHeader("Content-Type", "application/xml");
        request.addBodyParameter("key", "value");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        request.setReadTimeout(20, TimeUnit.SECONDS);
        response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testAddBody() {
        try {
            request.setCharset("utf-8");
            request.addBody(null, new byte[10]);
            assertNotNull(request.getBodyContents());
        } catch (Exception e) {
            fail("addBody raised an exception");
        }
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("key1"));
        assertTrue(bodyParams.containsKey("key2"));
        assertEquals("value1", bodyParams.get("key1"));
        assertEquals("value2", bodyParams.get("key2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertTrue(queryStringParams.containsKey("key1"));
        assertTrue(queryStringParams.containsKey("key2"));
        assertEquals("value1", queryStringParams.get("key1"));
        assertEquals("value2", queryStringParams.get("key2"));
    }

    @Test
    public void testAddPayloadString() {
        request.addPayload("payload");
        String payload = request.getBodyContents();
        assertNotNull(payload);
        assertEquals("payload", payload);
    }

    @Test
    public void testAddPayloadByteArray() {
        request.addPayload(new byte[10]);
        byte[] payload = request.getByteBodyContents();
        assertNotNull(payload);
        assertEquals(10, payload.length);
    }

    @Test
    public void testGetQueryStringParams() {
        request.addQuerystringParameter("key1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertTrue(queryStringParams.containsKey("key1"));
        assertEquals("value1", queryStringParams.get("key1"));
    }

    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("key1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("key1"));
        assertEquals("value1", bodyParams.get("key1"));
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
        String bodyContents = request.getBodyContents();
        assertNotNull(bodyContents);
    }

    @Test
    public void testGetByteBodyContents() {
        byte[] byteBodyContents = request.getByteBodyContents();
        assertNotNull(byteBodyContents);
    }

    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
    }

    @Test
    public void testGetCharsetDefault() {
        String charset = request.getCharset();
        assertNotNull(charset);
    }

    @Test
    public void testGetCharset() {
        request.setCharset("utf-8");
        String charset = request.getCharset();
        assertEquals("utf-8", charset);
    }

    @Test
    public void testSetConnectTimeout() {
        try {
            request.setConnectTimeout(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("setConnectTimeout raised an exception");
        }
    }

    @Test
    public void testSetReadTimeout() {
        try {
            request.setReadTimeout(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("setReadTimeout raised an exception");
        }
    }

    @Test
    public void testSetCharset() {
        request.setCharset("utf-8");
        String charset = request.getCharset();
        assertEquals("utf-8", charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        request.setConnection(null);
        assertNull(request.connection);
    }

    @Test
    public void testToString() {
        String toString = request.toString();
        assertNotNull(toString);
    }

    @After
    public void tearDown() {
        request = null;
        response = null;
    }
}