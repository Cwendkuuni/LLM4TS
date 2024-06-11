import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import org.scribe.model.Request.*;

public class RequestTest {
    
    @Test
    public void testSend() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Send the request and get the response
        Response response = request.send();

        // Assert that the response is not null
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a header to the request
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);

        // Get the headers of the request
        Map<String, String> headers = request.getHeaders();

        // Assert that the added header is in the headers map
        assertTrue(headers.containsKey(key));
        assertEquals(value, headers.get(key));
    }
    
    @Test
    public void testAddBodyParameter() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a body parameter to the request
        String key = "name";
        String value = "John";
        request.addBodyParameter(key, value);

        // Get the body parameters of the request
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert that the added body parameter is in the body parameters map
        assertTrue(bodyParams.containsKey(key));
        assertEquals(value, bodyParams.get(key));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a querystring parameter to the request
        String key = "id";
        String value = "123";
        request.addQuerystringParameter(key, value);

        // Get the querystring parameters of the request
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert that the added querystring parameter is in the querystring parameters map
        assertTrue(queryStringParams.containsKey(key));
        assertEquals(value, queryStringParams.get(key));
    }
    
    @Test
    public void testAddPayload() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a payload to the request
        String payload = "This is a test payload";
        request.addPayload(payload);

        // Get the body contents of the request
        String bodyContents = request.getBodyContents();

        // Assert that the body contents match the added payload
        assertEquals(payload, bodyContents);
    }
    
    @Test
    public void testAddPayload_Byte() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a payload as byte array to the request
        byte[] payload = "This is a test payload".getBytes();
        request.addPayload(payload);

        // Get the byte body contents of the request
        byte[] byteBodyContents = request.getByteBodyContents();

        // Assert that the byte body contents match the added payload
        assertArrayEquals(payload, byteBodyContents);
    }
    
    @Test
    public void testGetQueryStringParams() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com?id=123";
        Request request = new Request(verb, url);

        // Add a querystring parameter to the request
        String key = "name";
        String value = "John";
        request.addQuerystringParameter(key, value);

        // Get the querystring parameters of the request
        Map<String, String> queryStringParams = request.getQueryStringParams();

        // Assert that the querystring parameters include the initial querystring parameter as well as the added parameter
        assertTrue(queryStringParams.containsKey("id"));
        assertTrue(queryStringParams.containsKey(key));
        assertEquals("123", queryStringParams.get("id"));
        assertEquals(value, queryStringParams.get(key));
    }
    
    @Test
    public void testGetBodyParams() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a body parameter to the request
        String key = "name";
        String value = "John";
        request.addBodyParameter(key, value);

        // Get the body parameters of the request
        Map<String, String> bodyParams = request.getBodyParams();

        // Assert that the body parameters include the added parameter
        assertTrue(bodyParams.containsKey(key));
        assertEquals(value, bodyParams.get(key));
    }
    
    @Test
    public void testGetUrl() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Get the URL of the request
        String actualUrl = request.getUrl();

        // Assert that the returned URL matches the initial URL
        assertEquals(url, actualUrl);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com?id=123";
        Request request = new Request(verb, url);

        // Get the sanitized URL of the request
        String sanitizedUrl = request.getSanitizedUrl();

        // Assert that the sanitized URL matches the initial URL with the querystring and port removed
        assertEquals("https://example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a payload to the request
        String payload = "This is a test payload";
        request.addPayload(payload);

        // Get the body contents of the request
        String bodyContents = request.getBodyContents();

        // Assert that the body contents match the added payload
        assertEquals(payload, bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        // Initialize Request object
        Verb verb = Verb.POST;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a payload as byte array to the request
        byte[] payload = "This is a test payload".getBytes();
        request.addPayload(payload);

        // Get the byte body contents of the request
        byte[] byteBodyContents = request.getByteBodyContents();

        // Assert that the byte body contents match the added payload
        assertArrayEquals(payload, byteBodyContents);
    }
    
    @Test
    public void testGetVerb() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Get the verb of the request
        Verb actualVerb = request.getVerb();

        // Assert that the returned verb matches the initial verb
        assertEquals(verb, actualVerb);
    }
    
    @Test
    public void testGetHeaders() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Add a header to the request
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);

        // Get the headers of the request
        Map<String, String> headers = request.getHeaders();

        // Assert that the headers include the added header
        assertTrue(headers.containsKey(key));
        assertEquals(value, headers.get(key));
    }
    
    @Test
    public void testGetCharset() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Get the charset of the request
        String charset = request.getCharset();

        // Assert that the returned charset matches the default charset
        assertEquals(Charset.defaultCharset().name(), charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Set the connect timeout for the request
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        request.setConnectTimeout(duration, unit);

        // Get the connect timeout of the request
        int connectTimeout = request.getConnection().getConnectTimeout();

        // Assert that the connect timeout matches the set duration in milliseconds
        assertEquals((int)unit.toMillis(duration), connectTimeout);
    }
    
    @Test
    public void testSetReadTimeout() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Set the read timeout for the request
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        request.setReadTimeout(duration, unit);

        // Get the read timeout of the request
        int readTimeout = request.getConnection().getReadTimeout();

        // Assert that the read timeout matches the set duration in milliseconds
        assertEquals((int)unit.toMillis(duration), readTimeout);
    }
    
    @Test
    public void testSetCharset() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Set the charset for the request
        String charset = "UTF-8";
        request.setCharset(charset);

        // Get the charset of the request
        String actualCharset = request.getCharset();

        // Assert that the returned charset matches the set charset
        assertEquals(charset, actualCharset);
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Set the keep alive flag for the request
        boolean connectionKeepAlive = true;
        request.setConnectionKeepAlive(connectionKeepAlive);

        // Get the keep alive flag of the request
        boolean actualConnectionKeepAlive = request.getConnectionKeepAlive();

        // Assert that the returned keep alive flag matches the set flag
        assertEquals(connectionKeepAlive, actualConnectionKeepAlive);
    }
    
    @Test
    public void testSetConnection() throws IOException {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Create a new connection
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();

        // Set the connection for the request
        request.setConnection(connection);

        // Get the connection of the request
        HttpURLConnection actualConnection = request.getConnection();

        // Assert that the returned connection matches the set connection
        assertEquals(connection, actualConnection);
    }
    
    @Test
    public void testToString() {
        // Initialize Request object
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);

        // Get the string representation of the request
        String stringRepresentation = request.toString();

        // Assert that the returned string represents the request with the verb and URL
        assertEquals(String.format("@Request(%s %s)", verb, url), stringRepresentation);
    }
}