package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testSend() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Send the request and get the response
        Response response = request.send();
        
        // Verify that the response is not null
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        try {
            // Invoke the private method createConnection using reflection
            java.lang.reflect.Method method = Request.class.getDeclaredMethod("createConnection");
            method.setAccessible(true);
            method.invoke(request);
            
            // Verify that the connection is not null
            assertNotNull(request.connection);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSendWithGetVerb() {
        // Create a Request object using GET verb
        Request request = new Request(Verb.GET, "https://example.com");
        
        try {
            // Invoke the private method doSend using reflection
            java.lang.reflect.Method method = Request.class.getDeclaredMethod("doSend");
            method.setAccessible(true);
            Response response = (Response) method.invoke(request);
            
            // Verify that the response is not null
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSendWithPutVerb() {
        // Create a Request object using PUT verb
        Request request = new Request(Verb.PUT, "https://example.com");
        
        try {
            // Invoke the private method doSend using reflection
            java.lang.reflect.Method method = Request.class.getDeclaredMethod("doSend");
            method.setAccessible(true);
            Response response = (Response) method.invoke(request);
            
            // Verify that the response is not null
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSendWithPostVerb() {
        // Create a Request object using POST verb
        Request request = new Request(Verb.POST, "https://example.com");
        
        try {
            // Invoke the private method doSend using reflection
            java.lang.reflect.Method method = Request.class.getDeclaredMethod("doSend");
            method.setAccessible(true);
            Response response = (Response) method.invoke(request);
            
            // Verify that the response is not null
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeaders() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add headers to the request
        request.addHeader("Content-Type", "application/json");
        
        // Verify that the headers are added correctly
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBody() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        try {
            // Invoke the private method addBody using reflection
            java.lang.reflect.Method method = Request.class.getDeclaredMethod("addBody", HttpURLConnection.class, byte[].class);
            method.setAccessible(true);
            method.invoke(request, request.connection, "test body".getBytes());
            
            // Verify that the Content-Length header is added correctly
            assertEquals("9", request.connection.getRequestProperty("Content-Length"));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeader() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a header to the request
        request.addHeader("Content-Type", "application/json");
        
        // Verify that the header is added correctly
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a body parameter to the request
        request.addBodyParameter("param", "value");
        
        // Verify that the body parameter is added correctly
        assertEquals("value", request.getBodyParams().get("param"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a querystring parameter to the request
        request.addQuerystringParameter("param", "value");
        
        // Verify that the querystring parameter is added correctly
        assertEquals("value", request.getQueryStringParams().get("param"));
    }
    
    @Test
    public void testAddPayloadWithStringPayload() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a payload to the request using a string
        request.addPayload("payload");
        
        // Verify that the payload is added correctly
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadWithBytePayload() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a payload to the request using a byte array
        request.addPayload("payload".getBytes());
        
        // Verify that the payload is added correctly
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com?param=value");
        
        // Add a querystring parameter to the request
        request.addQuerystringParameter("param2", "value2");
        
        // Get the querystring parameters
        Map<String, String> params = request.getQueryStringParams();
        
        // Verify that the querystring parameters are correct
        assertEquals("value", params.get("param"));
        assertEquals("value2", params.get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a body parameter to the request
        request.addBodyParameter("param", "value");
        
        // Get the body parameters
        Map<String, String> params = request.getBodyParams();
        
        // Verify that the body parameters are correct
        assertEquals("value", params.get("param"));
    }
    
    @Test
    public void testGetUrl() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Get the URL
        String url = request.getUrl();
        
        // Verify that the URL is correct
        assertEquals("https://example.com", url);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        // Create a Request object with a port number in the URL
        Request request = new Request(Verb.GET, "https://example.com:8080");
        
        // Get the sanitized URL
        String sanitizedUrl = request.getSanitizedUrl();
        
        // Verify that the sanitized URL is correct
        assertEquals("https://example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a payload to the request using a string
        request.addPayload("payload");
        
        // Get the body contents
        String bodyContents = request.getBodyContents();
        
        // Verify that the body contents are correct
        assertEquals("payload", bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add a payload to the request using a byte array
        request.addPayload("payload".getBytes());
        
        // Get the byte body contents
        byte[] byteContents = request.getByteBodyContents();
        
        // Verify that the byte body contents are correct
        assertArrayEquals("payload".getBytes(), byteContents);
    }
    
    @Test
    public void testGetVerb() {
        // Create a Request object with GET verb
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Get the verb
        Verb verb = request.getVerb();
        
        // Verify that the verb is GET
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetHeaders() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Add headers to the request
        request.addHeader("Content-Type", "application/json");
        
        // Get the headers
        Map<String, String> headers = request.getHeaders();
        
        // Verify that the headers are correct
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        // Create a Request object without setting the charset
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Get the charset
        String charset = request.getCharset();
        
        // Verify that the charset is the default charset
        assertEquals(Charset.defaultCharset().name(), charset);
        
        // Create a Request object with a specific charset
        Request requestWithCharset = new Request(Verb.GET, "https://example.com");
        requestWithCharset.setCharset("UTF-8");
        
        // Get the charset
        String charsetWithCharset = requestWithCharset.getCharset();
        
        // Verify that the charset is set correctly
        assertEquals("UTF-8", charsetWithCharset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Set the connect timeout
        request.setConnectTimeout(1, TimeUnit.SECONDS);
        
        // Get the connect timeout
        int timeout = request.connection.getConnectTimeout();
        
        // Verify that the connect timeout is set correctly
        assertEquals(1000, timeout);
    }
    
    @Test
    public void testSetReadTimeout() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Set the read timeout
        request.setReadTimeout(1, TimeUnit.SECONDS);
        
        // Get the read timeout
        int timeout = request.connection.getReadTimeout();
        
        // Verify that the read timeout is set correctly
        assertEquals(1000, timeout);
    }
    
    @Test
    public void testSetCharset() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Set the charset
        request.setCharset("UTF-8");
        
        // Get the charset
        String charset = request.getCharset();
        
        // Verify that the charset is set correctly
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Set the connection keep alive
        request.setConnectionKeepAlive(true);
        
        // Verify that the connection keep alive is set correctly
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Create a HttpURLConnection object
        HttpURLConnection conn = new HttpURLConnection();
        
        // Set the connection
        request.setConnection(conn);
        
        // Verify that the connection is set correctly
        assertSame(conn, request.connection);
    }
    
    @Test
    public void testToString() {
        // Create a Request object
        Request request = new Request(Verb.GET, "https://example.com");
        
        // Get the string representation
        String toString = request.toString();
        
        // Verify that the string representation is correct
        assertEquals("@Request(GET https://example.com)", toString);
    }
}