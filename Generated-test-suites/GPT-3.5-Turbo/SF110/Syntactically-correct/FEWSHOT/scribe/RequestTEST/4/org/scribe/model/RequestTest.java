package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        request.createConnection();
        // Test connection creation
        assertNotNull(request.connection);
    }
    
    @Test
    public void testDoSend() {
        Response response = request.doSend();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        // Test adding headers
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBody() {
        request.addBodyParameter("name", "John");
        request.addBodyParameter("age", "25");
        // Test adding body parameters
        assertEquals("John", request.getBodyParams().get("name"));
        assertEquals("25", request.getBodyParams().get("age"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        // Test adding querystring parameters
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testAddPayload() {
        request.addPayload("Hello, World!");
        // Test adding payload
        assertEquals("Hello, World!", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = {72, 101, 108, 108, 111};
        request.addPayload(payload);
        // Test adding payload as byte array
        assertEquals("Hello", request.getBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        String url = request.getUrl();
        // Test getting URL
        assertEquals("http://www.example.com", url);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        String sanitizedUrl = request.getSanitizedUrl();
        // Test getting sanitized URL
        assertEquals("http://www.example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        String bodyContents = request.getBodyContents();
        // Test getting body contents
        assertNotNull(bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        byte[] byteBodyContents = request.getByteBodyContents();
        // Test getting byte body contents
        assertNotNull(byteBodyContents);
    }
    
    @Test
    public void testGetVerb() {
        Verb verb = request.getVerb();
        // Test getting verb
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        // Test getting headers
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        String charset = request.getCharset();
        // Test getting charset
        assertNotNull(charset);
        // Test setting charset
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        // Test setting connect timeout
        assertEquals(5000, request.connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        // Test setting read timeout
        assertEquals(5000, request.connection.getReadTimeout());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        // Test setting connection keep alive
        assertEquals(true, request.connectionKeepAlive);
    }
    
    @Test
    public void testSetConnection() {
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        // Test setting connection
        assertNotNull(request.connection);
    }
    
    @Test
    public void testToString() {
        String representation = request.toString();
        // Test toString method
        assertNotNull(representation);
    }
}