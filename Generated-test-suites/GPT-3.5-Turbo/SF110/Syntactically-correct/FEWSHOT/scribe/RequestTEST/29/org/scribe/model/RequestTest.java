package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        
        Request request = new Request(verb, url);
        
        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
        
        Map<String, String> expectedQuerystringParams = new HashMap<String, String>();
        assertEquals(expectedQuerystringParams, request.getQueryStringParams());
        
        Map<String, String> expectedBodyParams = new HashMap<String, String>();
        assertEquals(expectedBodyParams, request.getBodyParams());
        
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        assertEquals(expectedHeaders, request.getHeaders());
        
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
    }
    
    @Test
    public void testSend() throws IOException {
        Request request = mock(Request.class);
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = mock(Response.class);
        
        when(request.createConnection()).thenReturn(connection);
        when(request.doSend()).thenReturn(response);
        
        Response actualResponse = request.send();
        
        verify(request).createConnection();
        verify(request).doSend();
        assertEquals(response, actualResponse);
    }
    
    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        Request request = mock(Request.class);
        
        when(request.createConnection()).thenThrow(new IOException());
        
        request.send();
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        
        Request request = new Request(verb, url);
        
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        URL effectiveUrlMock = mock(URL.class);
        
        when(effectiveUrlMock.openConnection()).thenReturn(connectionMock);
        whenNew(URL.class).withArguments(url).thenReturn(effectiveUrlMock);
        
        request.createConnection();
        
        verifyStatic();
        System.setProperty("http.keepAlive", "false");
        verify(effectiveUrlMock).openConnection();
        assertEquals(connectionMock, request.getConnection());
    }
    
    @Test
    public void testDoSend() throws IOException {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        
        Request request = new Request(verb, url);
        
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        byte[] byteBodyContentsMock = new byte[0];
        
        Response response = request.doSend();
        
        verify(connectionMock).setRequestMethod(verb.name());
        verify(request).addHeaders(connectionMock);
        verify(request).addBody(connectionMock, byteBodyContentsMock);
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        
        request.addHeaders(conn);
        
        verify(conn).setRequestProperty("Content-Type", "application/json");
        verify(conn).setRequestProperty("Authorization", "Bearer token");
    }
    
    @Test
    public void testAddBody() throws IOException {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        ArgumentCaptor<byte[]> argument = ArgumentCaptor.forClass(byte[].class);
        
        Request request = new Request(Verb.POST, "http://example.com");
        
        byte[] content = {1, 2, 3};
        request.addBody(conn, content);
        
        verify(conn).setRequestProperty("Content-Length", String.valueOf(content.length));
        verify(conn).setDoOutput(true);
        verify(conn.getOutputStream()).write(argument.capture());
        assertArrayEquals(content, argument.getValue());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Content-Type", "application/json");
        
        assertEquals(expectedHeaders, request.getHeaders());
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("username", "John");
        
        Map<String, String> expectedBodyParams = new HashMap<String, String>();
        expectedBodyParams.put("username", "John");
        
        assertEquals(expectedBodyParams, request.getBodyParams());
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("page", "1");
        
        Map<String, String> expectedQuerystringParams = new HashMap<String, String>();
        expectedQuerystringParams.put("page", "1");
        
        assertEquals(expectedQuerystringParams, request.getQueryStringParams());
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://example.com");
        String payload = "data";
        request.addPayload(payload);
        
        assertEquals(payload, request.getPayload());
        assertNull(request.getBytePayload());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] bytePayload = {1, 2, 3};
        request.addPayload(bytePayload);
        
        assertArrayEquals(bytePayload, request.getBytePayload());
        assertNull(request.getPayload());
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
        
        String charset = "UTF-8";
        request.setCharset(charset);
        
        assertEquals(charset, request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        
        request.setConnectTimeout(duration, unit);
        
        verify(request.getConnection()).setConnectTimeout(eq((int) unit.toMillis(duration)));
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        
        request.setReadTimeout(duration, unit);
        
        verify(request.getConnection()).setReadTimeout(eq((int) unit.toMillis(duration)));
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.isConnectionKeepAlive());
        
        request.setConnectionKeepAlive(false);
        
        assertFalse(request.isConnectionKeepAlive());
    }
    
    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        
        Request request = new Request(verb, url);
        
        String expectedToString = String.format("@Request(%s %s)", verb, url);
        
        assertEquals(expectedToString, request.toString());
    }
}