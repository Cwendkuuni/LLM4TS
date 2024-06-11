import org.junit.Test;
import static org.junit.Assert.*;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        assertEquals(1234567890L, accessEvent.getTimeStamp().longValue());
    }
    
    @Test
    public void testSetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        assertEquals(1234567890L, accessEvent.getTimeStamp().longValue());
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testSetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI("/example");
        assertEquals("/example", accessEvent.getRequestURI());
    }
    
    @Test
    public void testSetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURI("/example");
        assertEquals("/example", accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL("http://example.com");
        assertEquals("http://example.com", accessEvent.getRequestURL());
    }
    
    @Test
    public void testSetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRequestURL("http://example.com");
        assertEquals("http://example.com", accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost("localhost");
        assertEquals("localhost", accessEvent.getRemoteHost());
    }
    
    @Test
    public void testSetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteHost("localhost");
        assertEquals("localhost", accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser("admin");
        assertEquals("admin", accessEvent.getRemoteUser());
    }
    
    @Test
    public void testSetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteUser("admin");
        assertEquals("admin", accessEvent.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
    }
    
    @Test
    public void testSetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());
    }
    
    @Test
    public void testSetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName("localhost");
        assertEquals("localhost", accessEvent.getServerName());
    }
    
    @Test
    public void testSetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setServerName("localhost");
        assertEquals("localhost", accessEvent.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testSetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testSetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testSetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testSetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(8080);
        assertEquals(8080, accessEvent.getLocalPort());
    }
    
    @Test
    public void testSetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLocalPort(8080);
        assertEquals(8080, accessEvent.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(200);
        assertEquals(200, accessEvent.getStatusCode());
    }
    
    @Test
    public void testSetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setStatusCode(200);
        assertEquals(200, accessEvent.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1234567890L);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(1234567890L);
        assertTrue(accessEvent1.equals(accessEvent2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        int hashCode = Long.hashCode(1234567890L);
        assertEquals(hashCode, accessEvent.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        assertEquals("AccessEvent[loggerContext=null, timeStamp=1234567890]", accessEvent.toString());
    }

}