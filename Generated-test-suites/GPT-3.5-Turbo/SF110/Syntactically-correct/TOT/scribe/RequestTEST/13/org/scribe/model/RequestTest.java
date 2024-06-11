import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {


    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://www.example.com");
    }

    // Test send() method
    @Test
    public void send_SuccessfulResponse_SuccessfulRequest() {
        Response response = request.send();
        assertNotNull(response);
        assertEquals(200, response.getCode());
    }

    @Test(expected = OAuthException.class)
    public void send_ConnectionError_OAuthException() throws IOException {
        request = new Request(Verb.GET, "http://www.nonexistenturl.com");
        request.send();
    }

    // Test createConnection() method
    @Test
    public void createConnection_ValidUrl_ConnectionCreated() throws IOException {
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    @Test(expected = OAuthException.class)
    public void createConnection_InvalidUrl_OAuthException() throws IOException {
        request = new Request(Verb.GET, "not a valid url");
        request.createConnection();
    }

    // Test addHeaders() method
    @Test
    public void addHeaders_ValidHeaders_HeadersAdded() {
        request.addHeader("Authorization", "Bearer token");
        request.addHeader("Accept", "application/json");
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Authorization", "Bearer token");
        expectedHeaders.put("Accept", "application/json");
        assertEquals(expectedHeaders, request.getHeaders());
    }

    // Test addBody() method
    @Test
    public void addBody_ValidContent_BodyAdded() throws IOException {
        request.addBodyParameter("key", "value");
        byte[] content = request.getByteBodyContents();
        request.addBody(request.getConnection(), content);
        assertNotNull(request.getConnection().getOutputStream());
    }

    @Test(expected = IOException.class)
    public void addBody_InvalidContent_IOException() throws IOException {
        request.addBody(null, null);
    }

    // Test addHeader() method
    @Test
    public void addHeader_ValidKeyAndValue_HeaderAdded() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        assertEquals(expectedHeaders, request.getHeaders());
    }

    // Test addBodyParameter() method
    @Test
    public void addBodyParameter_ValidKeyAndValue_ParameterAdded() {
        request.addBodyParameter("key", "value");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key", "value");
        assertEquals(expectedParams, request.getBodyParams());
    }

    // Test addQuerystringParameter() method
    @Test
    public void addQuerystringParameter_ValidKeyAndValue_ParameterAdded() {
        request.addQuerystringParameter("key", "value");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key", "value");
        assertEquals(expectedParams, request.getQueryStringParams());
    }

    // Test addPayload() methods
    @Test
    public void addPayload_StringPayload_PayloadAdded() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }

    @Test
    public void addPayload_BytePayload_PayloadAdded() {
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    // Test getQueryStringParams() method
    @Test
    public void getQueryStringParams_ValidUrl_ValidParams() throws MalformedURLException {
        request = new Request(Verb.GET, "http://www.example.com?key1=value1");
        request.addQuerystringParameter("key2", "value2");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        assertEquals(expectedParams, request.getQueryStringParams());
    }

    @Test(expected = OAuthException.class)
    public void getQueryStringParams_MalformedUrl_OAuthException() {
        request = new Request(Verb.GET, "malformed url");
        request.getQueryStringParams();
    }

    // Test getBodyParams() method
    @Test
    public void getBodyParams_ValidBodyParams_ValidParams() {
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        assertEquals(expectedParams, request.getBodyParams());
    }

    // Test getUrl() method
    @Test
    public void getUrl_ValidUrl_ReturnUrl() {
        assertEquals("http://www.example.com", request.getUrl());
    }

    // Test getSanitizedUrl() method
    @Test
    public void getSanitizedUrl_ValidUrl_ReturnSanitizedUrl() {
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    // Test getBodyContents() method
    @Test
    public void getBodyContents_ValidBody_ReturnBodyContents() {
        request.addBodyParameter("key", "value");
        assertEquals("key=value", request.getBodyContents());
    }

    // Test getByteBodyContents() method
    @Test
    public void getByteBodyContents_ValidBody_ReturnByteBodyContents() {
        request.addBodyParameter("key", "value");
        byte[] expectedContents = "key=value".getBytes();
        assertArrayEquals(expectedContents, request.getByteBodyContents());
    }

    // Test getVerb() method
    @Test
    public void getVerb_ValidVerb_ReturnVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    // Test getHeaders() method
    @Test
    public void getHeaders_NoHeaders_ReturnEmptyHeaders() {
        assertTrue(request.getHeaders().isEmpty());
    }

    // Test getCharset() method
    @Test
    public void getCharset_NoCharset_DefaultCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void getCharset_ValidCharset_ReturnCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    // Test setConnectTimeout() method
    @Test
    public void setConnectTimeout_ValidDurationAndUnit_ConnectTimeoutSet() {
        request.setConnectTimeout(5, TimeUnit.SECONDS);
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }

    // Test setReadTimeout() method
    @Test
    public void setReadTimeout_ValidDurationAndUnit_ReadTimeoutSet() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }

    // Test setCharset() method
    @Test
    public void setCharset_ValidCharset_CharsetSet() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    // Test setConnectionKeepAlive() method
    @Test
    public void setConnectionKeepAlive_True_ConnectionKeepAliveSetToTrue() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    @Test
    public void setConnectionKeepAlive_False_ConnectionKeepAliveSetToFalse() {
        request.setConnectionKeepAlive(false);
        assertFalse(request.isConnectionKeepAlive());
    }

    // Test setConnection() method
    @Test
    public void setConnection_ValidConnection_ConnectionSet() {
        request.setConnection(null);
        assertNull(request.getConnection());
    }

    // Test toString() method
    @Test
    public void toString_ValidRequest_ConvertToString() {
        String expectedString = "@Request(GET http://www.example.com)";
        assertEquals(expectedString, request.toString());
    }

}