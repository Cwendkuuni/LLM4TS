package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Response response = request.send();
        Assert.assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnection(null); // Set connection to null to force IOException
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.doSend();
        Assert.assertNotNull(response);
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        HttpURLConnection connection = null; // Dummy connection
        request.addHeaders(connection);
        Assert.assertEquals("application/json", connection.getRequestProperty("Content-Type"));
    }

    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        request.addBodyParameter("param1", "value1");
        HttpURLConnection connection = null; // Dummy connection
        request.addBody(connection, request.getByteBodyContents());
        Assert.assertEquals("application/json", connection.getRequestProperty("Content-Type"));
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        Assert.assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        Assert.assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        Assert.assertEquals("value1", queryStringParams.get("param1"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        String payload = request.getPayload();
        Assert.assertEquals("payload", payload);
    }

    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payloadByte = "payload".getBytes();
        request.addPayload(payloadByte);
        byte[] bytePayload = request.getBytePayload();
        Assert.assertArrayEquals(payloadByte, bytePayload);
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        Assert.assertEquals("value1", queryStringParams.get("param1"));
        Assert.assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        Map<String, String> bodyParams = request.getBodyParams();
        Assert.assertEquals("value1", bodyParams.get("param1"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        String url = request.getUrl();
        Assert.assertEquals("http://www.example.com", url);
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com:8080");
        String sanitizedUrl = request.getSanitizedUrl();
        Assert.assertEquals("http://www.example.com", sanitizedUrl);
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        String bodyContents = request.getBodyContents();
        Assert.assertEquals("payload", bodyContents);
    }

    @Test
    public void testGetByteBodyContents() throws UnsupportedEncodingException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        byte[] byteBodyContents = request.getByteBodyContents();
        String bodyContents = new String(byteBodyContents, request.getCharset());
        Assert.assertEquals("param1=value1", bodyContents);
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Verb verb = request.getVerb();
        Assert.assertEquals(Verb.GET, verb);
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        Assert.assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        String charset = request.getCharset();
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        int connectTimeout = request.getConnection().getConnectTimeout();
        Assert.assertEquals(10000, connectTimeout);
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        int readTimeout = request.getConnection().getReadTimeout();
        Assert.assertEquals(10000, readTimeout);
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        String charset = request.getCharset();
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        boolean connectionKeepAlive = request.isConnectionKeepAlive();
        Assert.assertTrue(connectionKeepAlive);
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        String toString = request.toString();
        Assert.assertEquals("@Request(GET http://www.example.com)", toString);
    }
}