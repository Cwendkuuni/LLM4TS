package org.scribe.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {
    
    private Request request;
    
    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
    }
    
    @After
    public void tearDown() {
        request = null;
    }
    
    @Test
    public void testSend() {
        try {
            Response response = request.send();
            assertNotNull(response);
        } catch (OAuthException ex) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateConnection() {
        try {
            request.createConnection();
            assertNotNull(request.getConnection());
        } catch (IOException ex) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDoSend() {
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException ex) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testAddBody() {
        try {
            byte[] content = "test body".getBytes();
            request.createConnection();
            request.addBody(request.getConnection(), content);
            String contentLength = request.getConnection().getRequestProperty("Content-Length");
            assertNotNull(contentLength);
            assertEquals(String.valueOf(content.length), contentLength);
        } catch (IOException ex) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("param"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param", "value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value", queryStringParams.get("param"));
    }
    
    @Test
    public void testAddPayload() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        request.addQuerystringParameter("param", "value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value", queryStringParams.get("param"));
    }
    
    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("param", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("param"));
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
        byte[] bodyContents = "body content".getBytes();
        request.setBytePayload(bodyContents);
        assertEquals("body content", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        byte[] bodyContents = "body content".getBytes();
        request.setBytePayload(bodyContents);
        assertArrayEquals(bodyContents, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertSame(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}