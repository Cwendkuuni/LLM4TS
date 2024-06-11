package org.scribe.model;

import org.junit.*;
import java.util.concurrent.*;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://www.example.com/api");
    }
    
    @Test
    public void testSend_success() {
        Response response = request.send();
        
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
        Assert.assertEquals("OK", response.getMessage());
    }
    
    @Test
    public void testSend_withIOException() {
        request.setConnectTimeout(1, TimeUnit.MILLISECONDS);
        
        try {
            request.send();
            Assert.fail("IOException expected");
        } catch (OAuthException e) {
            Assert.assertEquals("Problems while creating connection", e.getMessage());
            Assert.assertNotNull(e.getCause());
            Assert.assertTrue(e.getCause() instanceof IOException);
        }
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("User-Agent", "Mozilla/5.0");
        
        Assert.assertEquals("Mozilla/5.0", request.getHeaders().get("User-Agent"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        
        Assert.assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param", "value");
        
        Assert.assertEquals("value", request.getQueryStringParams().get("param"));
    }
    
    @Test
    public void testAddPayload_string() {
        request.addPayload("payload");
        
        Assert.assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayload_byteArray() {
        byte[] payload = new byte[] {1, 2, 3};
        request.addPayload(payload);
        
        Assert.assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Assert.assertEquals("https://www.example.com/api", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Assert.assertEquals("https://www.example.com/api", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents_defaultCharset() {
        request.addBodyParameter("key", "value");
        
        Assert.assertEquals("key=value", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        request.addBodyParameter("key", "value");
        
        byte[] expected = "key=value".getBytes();
        Assert.assertArrayEquals(expected, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Assert.assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testGetCharset_defaultCharset() {
        Assert.assertEquals(System.getProperty("file.encoding"), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        // No assertion, just checking if the method runs without exceptions
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(2, TimeUnit.SECONDS);
        // No assertion, just checking if the method runs without exceptions
    }
    
    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        
        Assert.assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        
        Assert.assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        request.setConnection(connection);
        
        Assert.assertEquals(connection, request.getConnection());
    }
    
    @After
    public void tearDown() {
        request = null;
    }
}