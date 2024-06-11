package org.scribe.model;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Test send method
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        // Test createConnection method
        Request request = new Request(Verb.GET, "http://www.example.com");
        try{
            request.createConnection();
            assertNotNull(request.getConnection());
        }catch (IOException e){
            fail("IOException occurred");
        }
    }

    @Test
    public void testDoSend() {
        // Test doSend method
        Request request = new Request(Verb.GET, "http://www.example.com");
        try{
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        }catch (IOException e){
            fail("IOException occurred");
        }
    }

    @Test
    public void testAddHeaders() {
        // Test addHeaders method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBody() {
        // Test addBody method
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] content = "Test Body".getBytes();
        try{
            request.createConnection();
            request.addBody(request.getConnection(), content);
            assertEquals("Content-Length", request.getConnection().getHeaderFieldKey(0));
            assertEquals(String.valueOf(content.length), request.getConnection().getHeaderField(0));
        }catch (IOException e){
            fail("IOException occurred");
        }
    }

    @Test
    public void testAddHeader() {
        // Test addHeader method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        // Test addBodyParameter method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals(1, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Test addQuerystringParameter method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals(1, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
    }

    @Test
    public void testAddPayloadString() {
        // Test addPayload method with String parameter
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        // Test addPayload method with byte[] parameter
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        // Test getQueryStringParams method
        Request request = new Request(Verb.GET, "http://www.example.com?key1=value1&key2=value2");
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals(3, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value1", queryStringParams.get("key1"));
        assertEquals("value2", queryStringParams.get("key2"));
    }

    @Test
    public void testGetBodyParams() {
        // Test getBodyParams method
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals(1, bodyParams.size());
        assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testGetUrl() {
        // Test getUrl method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        // Test getSanitizedUrl method
        Request request = new Request(Verb.GET, "http://www.example.com?key1=value1&key2=value2");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        // Test getBodyContents method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("param1=value1", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        // Test getByteBodyContents method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        byte[] byteBodyContents = request.getByteBodyContents();
        assertNotNull(byteBodyContents);
        assertEquals("param1=value1", new String(byteBodyContents));
    }

    @Test
    public void testGetVerb() {
        // Test getVerb method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        // Test getHeaders method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        // Test getCharset method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        // Test setConnectTimeout method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);
        // Assert statement for verifying the timeout duration
    }

    @Test
    public void testSetReadTimeout() {
        // Test setReadTimeout method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);
        // Assert statement for verifying the timeout duration
    }

    @Test
    public void testSetCharset() {
        // Test setCharset method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Test setConnectionKeepAlive method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
        request.setConnectionKeepAlive(false);
        assertFalse(request.getConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        // Test setConnection method
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com"));
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    @Test
    public void testToString() {
        // Test toString method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }

}