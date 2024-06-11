import org.junit.*;
import static org.junit.Assert.*;

import org.scribe.model.*;

import java.util.*;

public class RequestTest {
    
    // Test addHeader method
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    // Test addBodyParameter method
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "john.doe");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("john.doe", bodyParams.get("username"));
    }
    
    // Test addQuerystringParameter method
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("page", "1");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertTrue(queryStringParams.containsKey("page"));
        assertEquals("1", queryStringParams.get("page"));
    }
    
    // Test addPayload method with String
    @Test
    public void testAddPayloadWithString() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    // Test addPayload method with byte array
    @Test
    public void testAddPayloadWithBytes() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    // Test getQueryStringParams method
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }
    
    // Test getBodyParams method
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("username", "john.doe");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("john.doe", bodyParams.get("username"));
    }
    
    // Test getUrl method
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    // Test getSanitizedUrl method
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com:8080/page?param=value");
        assertEquals("http://www.example.com/page", request.getSanitizedUrl());
    }
    
    // Test getBodyContents method
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    // Test getByteBodyContents method
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    // Test getVerb method
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    // Test getHeaders method
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    // Test getCharset method
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    // Test setConnectTimeout method
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    // Test setReadTimeout method
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    // Test setCharset method
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    // Test setConnectionKeepAlive method
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
    
    // Test setConnection method
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
    
    // Test send method
    // TODO: Write comprehensive test cases for the send method
    
}