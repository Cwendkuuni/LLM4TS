package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        Response response = request.send();
        
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        
        assertNotNull(connection);
    }
    
    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        Response response = request.doSend();
        
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        request.setHeaders(headers);
        
        request.addHeaders();
        
        assertEquals(2, request.getConnection().getRequestProperties().size());
        assertEquals("application/json", request.getConnection().getRequestProperty("Content-Type"));
        assertEquals("Bearer token", request.getConnection().getRequestProperty("Authorization"));
    }
    
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com/");
        byte[] content = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        request.setBytePayload(content);
        
        request.addBody();
        
        assertEquals(content.length, request.getConnection().getRequestProperty("Content-Length"));
        assertEquals(1, request.getConnection().getOutputStream().write());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.addHeader("Content-Type", "application/json");
        
        assertEquals(1, request.getHeaders().size());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.addBodyParameter("key", "value");
        
        assertEquals(1, request.getBodyParams().size());
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.addQuerystringParameter("key", "value");
        
        assertEquals(1, request.getQueryStringParams().size());
        assertEquals("value", request.getQueryStringParams().get("key"));
    }
    
    @Test
    public void testAddPayload1() {
        Request request = new Request(Verb.POST, "http://www.example.com/");
        request.addPayload("Hello, world!");
        
        assertEquals("Hello, world!", request.getPayload());
    }
    
    @Test
    public void testAddPayload2() {
        Request request = new Request(Verb.POST, "http://www.example.com/");
        byte[] payload = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com:8080/path?param=value");
        String sanitizedUrl = request.getSanitizedUrl();
        
        assertEquals("http://www.example.com/path", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com/");
        byte[] payload = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);
        
        assertEquals("Hello, world!", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com/");
        byte[] payload = "Hello, world!".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        Verb verb = request.getVerb();
        
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        String charset = request.getCharset();
        
        assertEquals(Charset.defaultCharset().name(), charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        
        assertEquals(1000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        HttpURLConnection connection = new HttpURLConnection(null);
        request.setConnection(connection);
        
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com/");
        
        assertEquals("@Request(GET http://www.example.com/)", request.toString());
    }
}