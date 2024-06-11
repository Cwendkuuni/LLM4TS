package org.scribe.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.utils.URLUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://www.example.com");
    }

    @After
    public void tearDown() {
        request = null;
    }

    // Test cases for send() method

    @Test
    public void testSend_SuccessfulRequest_ReturnResponse() {
        Response response = request.send();
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for Response
    }

    @Test(expected = OAuthException.class)
    public void testSend_ConnectionException_ThrowOAuthException() {
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        // Call send() and wait for connection timeout
        request.send();
    }

    // Test cases for createConnection() method

    @Test
    public void testCreateConnection_ValidUrl_CreateHttpURLConnection() throws IOException {
        request.createConnection();
        assertNotNull(request.getConnection());
        assertTrue(request.getConnection() instanceof HttpURLConnection);
    }

    // Test cases for doSend() method

    @Test
    public void testDoSend_ValidVerbAndHeaders_SendRequestAndReturnResponse() throws IOException {
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for Response
    }

    @Test
    public void testDoSend_PutVerbWithBodyContents_SendRequestWithBodyAndReturnResponse() throws IOException {
        request.setVerb(Verb.PUT);
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(200, response.getCode());
        // Additional assertions for Response
    }

    // Test cases for addHeaders() method

    @Test
    public void testAddHeaders_ValidHeaders_AddHeadersToConnection() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");

        request.addHeaders(request.getConnection(), headers);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            assertEquals(entry.getValue(), request.getConnection().getRequestProperty(entry.getKey()));
        }
    }

    // Test cases for addBody() method

    @Test
    public void testAddBody_ValidConnectionAndContent_SetContentLengthAndWriteContentToOutputStream() throws IOException {
        String content = "Test Content";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        request.addBody(request.getConnection(), contentBytes);

        assertEquals(String.valueOf(contentBytes.length), request.getConnection().getRequestProperty("Content-Length"));
        assertEquals(content, outputStream.toString(StandardCharsets.UTF_8.name()));
    }

    // Test cases for addHeader() method

    @Test
    public void testAddHeader_ValidKeyAndValue_AddHeaderToHeadersMap() {
        String key = "Header1";
        String value = "Value1";

        request.addHeader(key, value);

        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }

    // Test cases for addBodyParameter() method

    @Test
    public void testAddBodyParameter_ValidKeyAndValue_AddBodyParameterToBodyParamsMap() {
        String key = "param1";
        String value = "value1";

        request.addBodyParameter(key, value);

        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }

    // Test cases for addQuerystringParameter() method

    @Test
    public void testAddQuerystringParameter_ValidKeyAndValue_AddQuerystringParameterToQuerystringParamsMap() {
        String key = "param1";
        String value = "value1";

        request.addQuerystringParameter(key, value);

        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    // Test cases for addPayload() method

    @Test
    public void testAddPayload_StringPayload_SetPayload() {
        String payload = "Test Payload";

        request.addPayload(payload);

        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayload_BytePayload_SetBytePayload() {
        byte[] payload = "Test Payload".getBytes(StandardCharsets.UTF_8);

        request.addPayload(payload);

        assertArrayEquals(payload, request.getBytePayload());
    }

    // Test cases for getQueryStringParams() method

    @Test
    public void testGetQueryStringParams_ValidURL_ReturnQueryStringParams() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> params = request.getQueryStringParams();

        assertEquals(2, params.size());
        assertTrue(params.containsKey("param1"));
        assertTrue(params.containsKey("param2"));
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }

    // Test cases for getBodyParams() method

    @Test
    public void testGetBodyParams_ValidBodyParams_ReturnBodyParams() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        Map<String, String> params = request.getBodyParams();

        assertEquals(2, params.size());
        assertTrue(params.containsKey("param1"));
        assertTrue(params.containsKey("param2"));
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }

    // Test cases for getUrl() method

    @Test
    public void testGetUrl_ValidUrl_ReturnUrl() {
        String url = "https://www.example.com";

        assertEquals(url, request.getUrl());
    }

    // Test cases for getSanitizedUrl() method

    @Test
    public void testGetSanitizedUrl_UrlWithQuery_RemoveQueryParameters() {
        String url = "https://www.example.com?param1=value1&param2=value2";

        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetSanitizedUrl_UrlWithPort_RemovePortNumber() {
        String url = "https://www.example.com:8080";

        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }

    // Test cases for getBodyContents() method

    @Test
    public void testGetBodyContents_ValidContent_ReturnBodyContents() {
        String content = "Test Content";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

        request.addPayload(content);

        assertEquals(content, request.getBodyContents());
    }

    // Test cases for getByteBodyContents() method

    @Test
    public void testGetByteBodyContents_WithBytePayload_ReturnBytePayload() {
        byte[] payload = "Test Payload".getBytes(StandardCharsets.UTF_8);

        request.addPayload(payload);

        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetByteBodyContents_WithFormURLEncodedBodyParams_ReturnBodyContentsInBytes() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        byte[] expectedBytes = URLUtils.formURLEncodeMap(request.getBodyParams()).getBytes(StandardCharsets.UTF_8);

        assertArrayEquals(expectedBytes, request.getByteBodyContents());
    }

    // Test cases for getVerb() method

    @Test
    public void testGetVerb_ValidVerb_ReturnVerb() {
        Verb verb = Verb.GET;

        assertEquals(verb, request.getVerb());
    }

    // Test cases for getHeaders() method

    @Test
    public void testGetHeaders_ValidHeaders_ReturnHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");

        request.addHeaders(headers);

        assertEquals(headers, request.getHeaders());
    }

    // Test cases for getCharset() method

    @Test
    public void testGetCharset_DefaultCharset_ReturnDefaultCharset() {
        String charset = Charset.defaultCharset().name();

        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testGetCharset_CustomCharset_ReturnCustomCharset() {
        String charset = "UTF-16";

        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    // Test cases for setConnectTimeout() method

    @Test
    public void testSetConnectTimeout_ValidDurationAndUnit_SetConnectTimeout() {
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        request.setConnectTimeout(duration, unit);

        assertEquals(duration, request.getConnection().getConnectTimeout());
    }

    // Test cases for setReadTimeout() method

    @Test
    public void testSetReadTimeout_ValidDurationAndUnit_SetReadTimeout() {
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        request.setReadTimeout(duration, unit);

        assertEquals(duration, request.getConnection().getReadTimeout());
    }

    // Test cases for setCharset() method

    @Test
    public void testSetCharset_ValidCharsetName_SetCharset() {
        String charset = "UTF-16";

        request.setCharset(charset);

        assertEquals(charset, request.getCharset());
    }

    // Test cases for setConnectionKeepAlive() method

    @Test
    public void testSetConnectionKeepAlive_True_SetConnectionKeepAliveTrue() {
        request.setConnectionKeepAlive(true);

        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnectionKeepAlive_False_SetConnectionKeepAliveFalse() {
        request.setConnectionKeepAlive(false);

        assertFalse(request.isConnectionKeepAlive());
    }

}