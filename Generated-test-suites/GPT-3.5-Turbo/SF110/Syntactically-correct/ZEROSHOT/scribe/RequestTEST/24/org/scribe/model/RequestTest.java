package org.scribe.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testSend() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Send the request
        Response response = request.send();

        // Assert that the response is not null
        Assert.assertNotNull(response);
    }

    @Test
    public void testAddHeader() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Add a header
        request.addHeader("Authorization", "Bearer token123");

        // Get the headers
        Map<String, String> headers = request.getHeaders();

        // Assert that the headers map contains the added header
        Assert.assertTrue(headers.containsKey("Authorization"));
        Assert.assertEquals("Bearer token123", headers.get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        // Create a Request object
        Request request = new Request(Verb.POST, "http://www.example.com");

        // Add a body parameter
        request.addBodyParameter("username", "john");

        // Get the body parameters
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert that the body parameters map contains the added parameter
        Assert.assertTrue(bodyParams.containsKey("username"));
        Assert.assertEquals("john", bodyParams.get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Add a querystring parameter
        request.addQuerystringParameter("page", "1");

        // Get the querystring parameters
        Map<String, String> queryParams = request.getQueryStringParams();

        // Assert that the querystring parameters map contains the added parameter
        Assert.assertTrue(queryParams.containsKey("page"));
        Assert.assertEquals("1", queryParams.get("page"));
    }

    @Test
    public void testAddPayload() {
        // Create a Request object
        Request request = new Request(Verb.POST, "http://www.example.com");

        // Add a payload
        request.addPayload("Hello");

        // Get the body contents
        String bodyContents = request.getBodyContents();

        // Assert that the body contents is the same as the added payload
        Assert.assertEquals("Hello", bodyContents);
    }

    @Test
    public void testGetSanitizedUrl() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com?param=value");

        // Get the sanitized URL
        String sanitizedUrl = request.getSanitizedUrl();

        // Assert that the sanitized URL does not contain any query parameters or port numbers
        Assert.assertEquals("http://www.example.com", sanitizedUrl);
    }

    @Test
    public void testGetVerb() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Get the verb
        Verb verb = request.getVerb();

        // Assert that the verb is the same as the one passed to the constructor
        Assert.assertEquals(Verb.GET, verb);
    }

    @Test
    public void testSetConnectTimeout() throws InterruptedException {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Set the connect timeout
        request.setConnectTimeout(5, TimeUnit.SECONDS);

        // Sleep for a longer duration than the set connect timeout
        Thread.sleep(6000);

        // Attempt to send the request, should throw an exception due to timeout
        try {
            request.send();
            Assert.fail("Expected an exception to be thrown");
        } catch (OAuthException e) {
            // Timeout exception expected
            Assert.assertTrue(e.getMessage().contains("Problems while creating connection"));
        }
    }

    @Test
    public void testSetReadTimeout() throws InterruptedException {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Set the read timeout
        request.setReadTimeout(5, TimeUnit.SECONDS);

        // Attempt to send the request to a non-existent URL, should throw an exception due to timeout
        try {
            request.send();
            Assert.fail("Expected an exception to be thrown");
        } catch (OAuthException e) {
            // Timeout exception expected
            Assert.assertTrue(e.getMessage().contains("Problems while creating connection"));
        }
    }

    @Test
    public void testSetCharset() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Set the charset
        request.setCharset("UTF-8");

        // Get the charset
        String charset = request.getCharset();

        // Assert that the charset is the same as the one set
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Create a Request object
        Request request = new Request(Verb.GET, "http://www.example.com");

        // Set the connection keep alive
        request.setConnectionKeepAlive(true);

        // Get the connection keep alive
        boolean connectionKeepAlive = request.isConnectionKeepAlive();

        // Assert that the connection keep alive is the same as the one set
        Assert.assertTrue(connectionKeepAlive);
    }
}