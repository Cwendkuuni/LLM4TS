import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;

public class RequestTest {
    
    @Test
    public void testSend_withValidUrlAndVerb_ReturnsResponseObject() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        Response response = request.send();
        
        // Assert
        assertNotNull(response);
    }
    
    @Test(expected = OAuthException.class)
    public void testSend_withInvalidUrl_ThrowsOAuthException() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example#invalid");
        
        // Act
        Response response = request.send();
        
        // Assert
        // Exception is expected to be thrown
    }
    
    @Test(expected = OAuthException.class)
    public void testSend_withIOException_ThrowsOAuthException() {
        // Arrange
        Request request = new Request(Verb.GET, "http://invalidurl.com");
        
        // Act
        Response response = request.send();
        
        // Assert
        // Exception is expected to be thrown
    }
    
    @Test
    public void testAddHeader_withValidKeyAndValue_AddsHeaderToRequest() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.addHeader("Authorization", "Bearer token");
        
        // Assert
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter_withValidKeyAndValue_AddsBodyParameterToRequest() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        
        // Act
        request.addBodyParameter("username", "testuser");
        
        // Assert
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }
    
    @Test
    public void testAddQuerystringParameter_withValidKeyAndValue_AddsQuerystringParameterToRequest() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.addQuerystringParameter("page", "1");
        
        // Assert
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertTrue(queryStringParams.containsKey("page"));
        assertEquals("1", queryStringParams.get("page"));
    }
    
    @Test
    public void testAddPayload_withValidPayload_SetsPayloadInRequest() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        
        // Act
        request.addPayload("payload data");
        
        // Assert
        assertEquals("payload data", request.getPayload());
    }
    
    @Test
    public void testAddPayload_withValidBytePayload_SetsBytePayloadInRequest() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] payload = { 1, 2, 3 };
        
        // Act
        request.addPayload(payload);
        
        // Assert
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams_withValidUrl_ReturnsQueryStringParams() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com?page=1");
        request.addQuerystringParameter("size", "10");
        
        // Act
        Map<String, String> queryStringParams = request.getQueryStringParams();
        
        // Assert
        assertEquals(2, queryStringParams.size());
        assertTrue(queryStringParams.containsKey("page"));
        assertTrue(queryStringParams.containsKey("size"));
        assertEquals("1", queryStringParams.get("page"));
        assertEquals("10", queryStringParams.get("size"));
    }
    
    @Test
    public void testGetBodyParams_withValidBodyParams_ReturnsBodyParams() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("username", "testuser");
        
        // Act
        Map<String, String> bodyParams = request.getBodyParams();
        
        // Assert
        assertEquals(1, bodyParams.size());
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }
    
    @Test
    public void testGetUrl_withValidUrl_ReturnsUrl() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        String url = request.getUrl();
        
        // Assert
        assertEquals("http://example.com", url);
    }
    
    @Test
    public void testGetSanitizedUrl_withValidUrl_ReturnsSanitizedUrl() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com:8080/page?param=value");
        
        // Act
        String sanitizedUrl = request.getSanitizedUrl();
        
        // Assert
        assertEquals("http://example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents_withValidCharset_ReturnsBodyContents() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("username", "testuser");
        
        // Act
        String bodyContents = request.getBodyContents();
        
        // Assert
        assertEquals("username=testuser", bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents_withValidPayload_ReturnsByteBodyContents() {
        // Arrange
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("payload data");
        
        // Act
        byte[] byteBodyContents = request.getByteBodyContents();
        
        // Assert
        assertArrayEquals("payload data".getBytes(), byteBodyContents);
    }
    
    @Test
    public void testGetVerb_withValidVerb_ReturnsVerb() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        Verb verb = request.getVerb();
        
        // Assert
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetHeaders_withValidHeaders_ReturnsHeaders() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Authorization", "Bearer token");
        
        // Act
        Map<String, String> headers = request.getHeaders();
        
        // Assert
        assertEquals(1, headers.size());
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testGetCharset_withValidCharset_ReturnsCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        
        // Act
        String charset = request.getCharset();
        
        // Assert
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testSetConnectTimeout_withValidDurationAndUnit_SetsConnectTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        // Assert
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout_withValidDurationAndUnit_SetsReadTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        // Assert
        assertEquals(5000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset_withValidCharsetName_SetsCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.setCharset("UTF-8");
        
        // Assert
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive_withValidKeepAlive_SetsConnectionKeepAlive() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        
        // Act
        request.setConnectionKeepAlive(true);
        
        // Assert
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection_withValidConnection_SetsConnection() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com"));
        
        // Act
        request.setConnection(connection);
        
        // Assert
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString_returnsCorrectStringRepresentation() {
        // Arrange
        Request request = new Request(Verb.GET, "http://example.com");
        String expectedString = "@Request(GET http://example.com)";
        
        // Act
        String toString = request.toString();
        
        // Assert
        assertEquals(expectedString, toString);
    }
}