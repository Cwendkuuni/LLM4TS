package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "https://www.example.com");
    }

    @Test
    public void testSend() {
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() throws IOException {
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Key1", "Value1");
        request.addHeader("Key2", "Value2");
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Key1", "Value1");
        expectedHeaders.put("Key2", "Value2");
        assertEquals(expectedHeaders, request.getHeaders());
    }

    @Test
    public void testAddBody() throws IOException {
        byte[] content = {1, 2, 3};
        request.addBody(request.getConnection(), content);
        assertEquals(String.valueOf(content.length), request.getConnection().getRequestProperty("Content-Length"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("Param1", "Value1");
        request.addBodyParameter("Param2", "Value2");
        Map<String, String> expectedBodyParams = new HashMap<>();
        expectedBodyParams.put("Param1", "Value1");
        expectedBodyParams.put("Param2", "Value2");
        assertEquals(expectedBodyParams, request.getBodyParams());
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("Param1", "Value1");
        request.addQuerystringParameter("Param2", "Value2");
        Map<String, String> expectedQuerystringParams = new HashMap<>();
        expectedQuerystringParams.put("Param1", "Value1");
        expectedQuerystringParams.put("Param2", "Value2");
        assertEquals(expectedQuerystringParams, request.getQueryStringParams());
    }

    @Test
    public void testAddPayload() {
        request.addPayload("Payload");
        assertEquals("Payload", request.getPayload());
    }

    @Test
    public void testAddPayloadBytes() {
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertEquals(payload, request.getBytePayload());
    }

    @Test
    public void testToString() {
        assertEquals("@Request(GET https://www.example.com)", request.toString());
    }

    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertEquals(true, request.isConnectionKeepAlive());
    }

    @Test
    public void testSetConnection() {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public void disconnect() {
            }

            @Override
            public boolean usingProxy() {
                return false;
            }
        };
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
}