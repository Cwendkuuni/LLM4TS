package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.*;
import java.net.*;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = request.getConnection();
        request.addHeaders(connection);
        Map<String, String> headers = request.getHeaders();
        assertEquals(headers, connection.getRequestProperties());
    }
    
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        HttpURLConnection connection = request.getConnection();
        byte[] content = "body content".getBytes();
        request.addBody(connection, content);
        assertEquals(content.length, connection.getContentLength());
        assertTrue(connection.getDoOutput());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        Map<String, String> headers = request.getHeaders();
        assertEquals("value", headers.get("key"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("key", "value");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertEquals("value", queryParams.get("key"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayload_byteArray() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertEquals("value1", queryParams.get("param1"));
        assertEquals("value2", queryParams.get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com:8080/path?param=value");
        assertEquals("http://www.example.com/path", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("body contents");
        assertEquals("body contents", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "body contents".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        Map<String, String> headers = request.getHeaders();
        assertEquals("value", headers.get("key"));
    }
    
    @Test
    public void testGetCharset_default() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        HttpURLConnection connection = request.getConnection();
        assertEquals(10000, connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        HttpURLConnection connection = request.getConnection();
        assertEquals(10000, connection.getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        HttpURLConnection connection = request.getConnection();
        assertTrue(connection.getKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}