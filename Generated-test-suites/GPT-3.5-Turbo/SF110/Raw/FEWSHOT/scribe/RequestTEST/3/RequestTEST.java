package org.scribe.model;

import static org.junit.Assert.assertEquals;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    
    private Request request;
    private HttpURLConnection connection;
    
    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
        connection = new HttpURLConnection(url.openConnection());
    }
    
    @Test
    public void testSend() {
        Response response = request.send();
        // Add assertions here
    }
    
    @Test
    public void testCreateConnection() {
        request.createConnection();
        // Add assertions here
    }
    
    @Test
    public void testDoSend() {
        Response response = request.doSend();
        // Add assertions here
    }
    
    @Test
    public void testAddHeaders() {
        request.addHeaders(connection);
        // Add assertions here
    }
    
    @Test
    public void testAddBody() {
        byte[] content = { 0x01, 0x02, 0x03 };
        request.addBody(connection, content);
        // Add assertions here
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        // Add assertions here
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        // Add assertions here
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("key", "value");
        // Add assertions here
    }
    
    @Test
    public void testAddPayload_String() {
        request.addPayload("payload");
        // Add assertions here
    }
    
    @Test
    public void testAddPayload_Byte() {
        byte[] payload = { 0x01, 0x02, 0x03 };
        request.addPayload(payload);
        // Add assertions here
    }
    
    @Test
    public void testGetQueryStringParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        
        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");
        
        Map<String, String> queryParams = request.getQueryStringParams();
        
        assertEquals(expectedParams, queryParams);
    }
    
    @Test
    public void testGetBodyParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        
        Map<String, String> bodyParams = request.getBodyParams();
        
        assertEquals(expectedParams, bodyParams);
    }
    
    @Test
    public void testGetUrl() {
        String expectedUrl = "http://www.example.com";
        String url = request.getUrl();
        
        assertEquals(expectedUrl, url);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        String expectedUrl = "http://www.example.com";
        String sanitizedUrl = request.getSanitizedUrl();
        
        assertEquals(expectedUrl, sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        String expectedContents = "bodyContents";
        
        byte[] bodyContents = expectedContents.getBytes();
        request.addPayload(bodyContents);
        
        String contents = request.getBodyContents();
        
        assertEquals(expectedContents, contents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        byte[] expectedContents = { 0x01, 0x02, 0x03 };
        
        byte[] bodyContents = request.getByteBodyContents();
        
        assertArrayEquals(expectedContents, bodyContents);
    }
    
    @Test
    public void testGetVerb() {
        Verb expectedVerb = Verb.GET;
        
        Verb verb = request.getVerb();
        
        assertEquals(expectedVerb, verb);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        
        request.addHeader("Content-Type", "application/json");
        
        Map<String, String> headers = request.getHeaders();
        
        assertEquals(expectedHeaders, headers);
    }
    
    @Test
    public void testGetCharset() {
        String expectedCharset = "UTF-8";
        
        String charset = request.getCharset();
        
        assertEquals(expectedCharset, charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        int expectedTimeout = 1000;
        
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        
        int timeout = connection.getConnectTimeout();
        
        assertEquals(expectedTimeout, timeout);
    }
    
    @Test
    public void testSetReadTimeout() {
        int expectedTimeout = 1000;
        
        request.setReadTimeout(1, TimeUnit.SECONDS);
        
        int timeout = connection.getReadTimeout();
        
        assertEquals(expectedTimeout, timeout);
    }
    
    @Test
    public void testSetCharset() {
        String expectedCharset = "UTF-8";
        
        request.setCharset("UTF-8");
        
        String charset = request.getCharset();
        
        assertEquals(expectedCharset, charset);
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        boolean expectedKeepAlive = true;
        
        request.setConnectionKeepAlive(true);
        
        boolean keepAlive = connection.getAllowUserInteraction();
        
        assertEquals(expectedKeepAlive, keepAlive);
    }
    
    @Test
    public void testSetConnection() {
        request.setConnection(connection);
        // Add assertions here
    }
    
    @Test
    public void testToString() {
        String expectedToString = "@Request(GET http://www.example.com)";
        String toString = request.toString();
        
        assertEquals(expectedToString, toString);
    }
}