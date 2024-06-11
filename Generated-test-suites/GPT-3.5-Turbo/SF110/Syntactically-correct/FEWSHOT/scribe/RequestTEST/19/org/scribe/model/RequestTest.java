package org.scribe.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testSend() {
        // create a mocked HttpURLConnection
        HttpURLConnection mockConnection = createMockConnection();
        Request request = createRequest(mockConnection);

        Response response = request.send();

        // assert the response
        assertNotNull(response);
        // TODO: perform more assertions on the response
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        // create a mocked HttpURLConnection that throws IOException
        HttpURLConnection mockConnection = createMockConnection();
        mockConnection.getInputStream();

        Request request = createRequest(mockConnection);

        request.send(); // this should throw an OAuthException
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = createRequest(null);
        request.createConnection();

        HttpURLConnection connection = request.getConnection();

        assertNotNull(connection);
        // TODO: perform more assertions on the connection
    }

    @Test
    public void testDoSend() throws IOException {
        // create a mocked HttpURLConnection
        HttpURLConnection mockConnection = createMockConnection();
        Request request = createRequest(mockConnection);

        Response response = request.doSend();

        // assert the response
        assertNotNull(response);
        // TODO: perform more assertions on the response
    }

    @Test
    public void testAddHeaders() throws IOException {
        // create a mocked HttpURLConnection
        HttpURLConnection mockConnection = createMockConnection();
        Request request = createRequest(mockConnection);

        // add headers to the request
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");

        // assert the headers in the connection
        assertEquals("application/json", mockConnection.getRequestProperty("Content-Type"));
        assertEquals("Bearer token", mockConnection.getRequestProperty("Authorization"));
    }

    @Test
    public void testAddBody() throws IOException {
        // create a mocked HttpURLConnection
        HttpURLConnection mockConnection = createMockConnection();
        Request request = createRequest(mockConnection);

        // create a byte array as the body content
        byte[] body = "Hello, world!".getBytes();

        // add the body to the request
        request.addBody(mockConnection, body);

        // assert the Content-Length header
        assertEquals(String.valueOf(body.length), mockConnection.getRequestProperty("Content-Length"));
        // TODO: perform more assertions on the body of the connection
    }

    @Test
    public void testAddHeader() {
        // create a mocked HttpURLConnection
        HttpURLConnection mockConnection = createMockConnection();
        Request request = createRequest(mockConnection);

        // add a header to the request
        request.addHeader("Content-Type", "application/json");

        // assert the header in the connection
        assertEquals("application/json", mockConnection.getRequestProperty("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = createRequest(null);

        // add body parameters to the request
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        // assert the body parameters
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = createRequest(null);

        // add query string parameters to the request
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        // assert the query string parameters
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }

    @Test
    public void testAddPayload() {
        Request request = createRequest(null);

        // add a payload to the request
        request.addPayload("Hello, world!");

        // assert the payload
        assertEquals("Hello, world!", request.getBodyContents());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = createRequest(null);

        // set the connect timeout
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);

        // assert the connect timeout
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = createRequest(null);

        // set the read timeout
        request.setReadTimeout(2000, TimeUnit.MILLISECONDS);

        // assert the read timeout
        assertEquals(2000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = createRequest(null);

        // set the charset
        request.setCharset("UTF-8");

        // assert the charset
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = createRequest(null);

        // set the connection keep alive
        request.setConnectionKeepAlive(true);

        // assert the connection keep alive
        assertTrue(request.getConnection().getRequestProperty("http.keepAlive").equals("true"));
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");

        // assert the toString() result
        assertEquals("@Request(GET http://example.com)", request.toString());
    }

    // helper method to create a mocked HttpURLConnection
    private HttpURLConnection createMockConnection() {
        HttpURLConnection mockConnection = new HttpURLConnection() {
            // TODO: override methods as needed for testing
        };
        return mockConnection;
    }

    // helper method to create a Request object
    private Request createRequest(HttpURLConnection connection) {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnection(connection);
        return request;
    }
}