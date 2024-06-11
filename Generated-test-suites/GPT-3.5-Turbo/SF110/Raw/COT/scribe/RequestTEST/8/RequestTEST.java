package org.scribe.model;

import org.junit.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Test for send() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
      
        // Assertions
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void testCreateConnection() {
        // Test for createConnection() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
      
        // Assertions
        Assert.assertNotNull(request);
        Assert.assertNotNull(request.getConnection());
        Assert.assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testDoSend() {
        // Test for doSend() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response;
        try {
            request.createConnection();
            response = request.doSend();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
      
        // Assertions
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void testAddHeaders() {
        // Test for addHeaders() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
      
        // Assertions
        Assert.assertNotNull(request.getHeaders());
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBody() {
        // Test for addBody() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] content = "test".getBytes();
        try {
            request.createConnection();
            request.addBody(request.getConnection(), content);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
      
        // Assertions
        Assert.assertNotNull(request.getConnection().getOutputStream());
        Assert.assertEquals("4", request.getConnection().getRequestProperty("Content-Length"));
    }

    @Test
    public void testAddHeader() {
        // Test for addHeader() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Authorization", "Bearer token");
      
        // Assertions
        Assert.assertNotNull(request.getHeaders());
        Assert.assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        // Test for addBodyParameter() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
      
        // Assertions
        Assert.assertNotNull(request.getBodyParams());
        Assert.assertEquals("testuser", request.getBodyParams().get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Test for addQuerystringParameter() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("page", "1");
      
        // Assertions
        Assert.assertNotNull(request.getQueryStringParams());
        Assert.assertEquals("1", request.getQueryStringParams().get("page"));
    }

    @Test
    public void testAddPayload() {
        // Test for addPayload() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("test payload");
      
        // Assertions
        Assert.assertNotNull(request.getPayload());
        Assert.assertEquals("test payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByte() {
        // Test for addPayload() method with byte array
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "test payload".getBytes();
        request.addPayload(payload);
      
        // Assertions
        Assert.assertNotNull(request.getBytePayload());
        Assert.assertEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        // Test for getQueryStringParams() method
        Request request = new Request(Verb.GET, "http://www.example.com?page=1&limit=10");
        request.addQuerystringParameter("sort", "asc");
      
        // Assertions
        Assert.assertNotNull(request.getQueryStringParams());
        Assert.assertEquals("1", request.getQueryStringParams().get("page"));
        Assert.assertEquals("10", request.getQueryStringParams().get("limit"));
        Assert.assertEquals("asc", request.getQueryStringParams().get("sort"));
    }

    @Test
    public void testGetBodyParams() {
        // Test for getBodyParams() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
      
        // Assertions
        Assert.assertNotNull(request.getBodyParams());
        Assert.assertEquals("testuser", request.getBodyParams().get("username"));
    }

    @Test
    public void testGetUrl() {
        // Test for getUrl() method
        Request request = new Request(Verb.GET, "http://www.example.com");
      
        // Assertions
        Assert.assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        // Test for getSanitizedUrl() method
        String url = "http://www.example.com?page=1&sort=asc";
        Request request = new Request(Verb.GET, url);
      
        // Assertions
        Assert.assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        // Test for getBodyContents() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
      
        // Assertions
        Assert.assertNotNull(request.getBodyContents());
        Assert.assertEquals("username=testuser", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        // Test for getByteBodyContents() method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
      
        // Assertions
        Assert.assertNotNull(request.getByteBodyContents());
        Assert.assertEquals("username=testuser", new String(request.getByteBodyContents()));
    }

    @Test
    public void testGetVerb() {
        // Test for getVerb() method
        Request request = new Request(Verb.GET, "http://www.example.com");
      
        // Assertions
        Assert.assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        // Test for getHeaders() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
      
        // Assertions
        Assert.assertNotNull(request.getHeaders());
        Assert.assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        // Test for getCharset() method
        Request request = new Request(Verb.GET, "http://www.example.com");
      
        // Assertions
        Assert.assertNotNull(request.getCharset());
        Assert.assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        // Test for setConnectTimeout() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
      
        // Assertions
        Assert.assertNotNull(request.getConnection());
        Assert.assertEquals(500, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        // Test for setReadTimeout() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
      
        // Assertions
        Assert.assertNotNull(request.getConnection());
        Assert.assertEquals(1000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        // Test for setCharset() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
      
        // Assertions
        Assert.assertNotNull(request.getCharset());
        Assert.assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Test for setConnectionKeepAlive() method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
      
        // Assertions
        Assert.assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        // Test for setConnection() method
        HttpURLConnection connection = null;
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnection(connection);
      
        // Assertions
        Assert.assertNull(request.getConnection());
    }

    @Test
    public void testToString() {
        // Test for toString() method
        Request request = new Request(Verb.GET, "http://www.example.com");
      
        // Assertions
        Assert.assertNotNull(request.toString());
        Assert.assertTrue(request.toString().contains(request.getVerb().toString()));
        Assert.assertTrue(request.toString().contains(request.getUrl()));
    }
}