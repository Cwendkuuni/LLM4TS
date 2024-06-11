
package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RequestTest {

    private Request request;
    private HttpURLConnection connection;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://example.com");
        connection = mock(HttpURLConnection.class);
        request.setConnection(connection);
    }

    @Test
    public void testSendSuccess() throws Exception {
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(new URL("https://example.com").openStream());

        Response response = request.send();

        assertEquals(200, response.getCode());
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() throws Exception {
        when(connection.getResponseCode()).thenThrow(new IOException());

        request.send();
    }

    @Test
    public void testCreateConnection() throws Exception {
        URL expectedURL = new URL("https://example.com?key=value");
        String queryString = "key=value";
        Map<String, String> querystringParams = new HashMap<>();
        querystringParams.put("key", "value");
        request.addQuerystringParameter("key", "value");

        URLUtils urlUtils = mock(URLUtils.class);
        when(urlUtils.appendParametersToQueryString(eq(request.getUrl()), eq(querystringParams))).thenReturn(expectedURL.toString());
        URLUtils.setInstance(urlUtils);

        request.createConnection();

        verify(connection, times(1)).setDoOutput(anyBoolean());
        verify(connection, times(1)).setRequestMethod(eq(request.getVerb().name()));
        verify(connection, times(1)).setRequestProperty("Content-Length", "0");
        verify(connection, times(1)).setRequestProperty("http.keepAlive", "false");
        verify(connection, times(1)).setRequestProperty(anyString(), anyString());
        assertEquals(expectedURL, request.getUrl());
    }

    @Test
    public void testDoSend() throws Exception {
        String queryString = "key=value";
        Map<String, String> querystringParams = new HashMap<>();
        querystringParams.put("key", "value");
        request.addQuerystringParameter("key", "value");

        Request spyRequest = spy(request);
        Response response = mock(Response.class);
        when(response.getCode()).thenReturn(200);
        doReturn(response).when(spyRequest).doSend();
        doNothing().when(spyRequest).addHeaders(any(HttpURLConnection.class));
        doNothing().when(spyRequest).addBody(any(HttpURLConnection.class), any(byte[].class));
        doReturn(new byte[0]).when(spyRequest).getByteBodyContents();

        Response actualResponse = spyRequest.doSend();

        verify(spyRequest, times(1)).addHeaders(any(HttpURLConnection.class));
        verify(spyRequest, times(1)).addBody(any(HttpURLConnection.class), any(byte[].class));
        assertEquals(response, actualResponse);
    }

    @Test
    public void testAddHeaders() throws Exception {
        request.addHeader("header1", "value1");

        verify(connection, times(1)).setRequestProperty(eq("header1"), eq("value1"));
    }

    @Test
    public void testAddBody() throws Exception {
        byte[] content = "test".getBytes(StandardCharsets.UTF_8);

        request.addBody(connection, content);

        verify(connection, times(1)).setRequestProperty(eq("Content-Length"), eq(String.valueOf(content.length)));
        verify(connection, times(1)).setDoOutput(true);
        verify(connection, times(1)).getOutputStream();
        verify(connection.getOutputStream(), times(1)).write(content);
    }

    @Test
    public void testAddHeader() throws Exception {
        request.addHeader("header", "value");

        Map<String, String> headers = request.getHeaders();
        assertEquals("value", headers.get("header"));
    }

    @Test
    public void testAddBodyParameter() throws Exception {
        request.addBodyParameter("param", "value");

        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("param"));
    }

    @Test
    public void testAddQuerystringParameter() throws Exception {
        request.addQuerystringParameter("param", "value");

        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value", queryStringParams.get("param"));
    }

    @Test
    public void testAddPayload_String() throws Exception {
        String payload = "testPayload";
        request.addPayload(payload);

        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayload_ByteArray() throws Exception {
        byte[] payload = "testPayload".getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);

        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() throws Exception {
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("key1", "value1");
        queryStringParams.put("key2", "value2");
        request.addQuerystringParameter("key1", "value1");
        request.addQuerystringParameter("key2", "value2");

        Map<String, String> actualQueryStringParams = request.getQueryStringParams();

        assertEquals(queryStringParams, actualQueryStringParams);
    }

    @Test
    public void testGetBodyParams() throws Exception {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("key1", "value1");
        bodyParams.put("key2", "value2");
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");

        Map<String, String> actualBodyParams = request.getBodyParams();

        assertEquals(bodyParams, actualBodyParams);
    }

    @Test
    public void testGetUrl() throws Exception {
        String url = "https://example.com?key=value";
        request = new Request(Verb.GET, url);

        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() throws Exception {
        String url = "https://example.com?key=value&param=1234";
        String sanitizedURL = "https://example.com";
        request = new Request(Verb.GET, url);

        assertEquals(sanitizedURL, request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws Exception {
        String content = "testContent";
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
        request.addPayload(content);

        String actualContent = request.getBodyContents();

        assertEquals(content, actualContent);
    }

    @Test
    public void testGetByteBodyContents_WithPayload() throws Exception {
        String payload = "testPayload";
        byte[] bytePayload = payload.getBytes(StandardCharsets.UTF_8);
        request.addPayload(payload);

        byte[] actualBytePayload = request.getByteBodyContents();

        assertArrayEquals(bytePayload, actualBytePayload);
    }

    @Test
    public void testGetByteBodyContents_WithoutPayload() throws Exception {
        String param1 = "key1=value1";
        String param2 = "key2=value2";
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        String content = param1 + "&" + param2;
        byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);

        byte[] actualByteContent = request.getByteBodyContents();

        assertArrayEquals(byteContent, actualByteContent);
    }

    @Test
    public void testGetVerb() throws Exception {
        Verb verb = Verb.GET;
        request = new Request(verb, "https://example.com");

        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetHeaders() throws Exception {
        request.addHeader("header1", "value1");
        request.addHeader("header2", "value2");

        Map<String, String> headers = request.getHeaders();

        assertEquals("value1", headers.get("header1"));
        assertEquals("value2", headers.get("header2"));
    }

    @Test
    public void testGetCharset_Default() throws Exception {
        String expectedCharset = Charset.defaultCharset().name();

        String actualCharset = request.getCharset();

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharset_Custom() throws Exception {
        String customCharset = "UTF-8";
        request.setCharset(customCharset);

        String actualCharset = request.getCharset();

        assertEquals(customCharset, actualCharset);
    }

    @Test
    public void testSetConnectTimeout() throws Exception {
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        int expectedTimeout = (int) unit.toMillis(duration);

        request.setConnectTimeout(duration, unit);

        verify(connection, times(1)).setConnectTimeout(eq(expectedTimeout));
    }

    @Test
    public void testSetReadTimeout() throws Exception {
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        int expectedTimeout = (int) unit.toMillis(duration);

        request.setReadTimeout(duration, unit);

        verify(connection, times(1)).setReadTimeout(eq(expectedTimeout));
    }

    @Test
    public void testSetCharset() throws Exception {
        String customCharset = "UTF-8";

        request.setCharset(customCharset);

        assertEquals(customCharset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive_True() throws Exception {
        boolean connectionKeepAlive = true;

        request.setConnectionKeepAlive(connectionKeepAlive);

        assertEquals(connectionKeepAlive, request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnectionKeepAlive_False() throws Exception {
        boolean connectionKeepAlive = false;

        request.setConnectionKeepAlive(connectionKeepAlive);

        assertEquals(connectionKeepAlive, request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() throws Exception {
        HttpURLConnection newConnection = mock(HttpURLConnection.class);

        request.setConnection(newConnection);

        assertEquals(newConnection, request.getConnection());
    }

    @Test
    public void testToString() throws Exception {
        Verb verb = Verb.PUT;
        String url = "https://example.com";
        request = new Request(verb, url);
        String expectedToString = String.format("@Request(%s %s)", verb, url);

        String actualToString = request.toString();

        assertEquals(expectedToString, actualToString);
    }
}
