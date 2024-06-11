
import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1234567890L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String requestURI = "/example";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://example.com";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String remoteHost = "127.0.0.1";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String remoteUser = "admin";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String serverName = "example.com";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param", values);
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertTrue(event1.equals(event2));
        
        event1.setLocalPort(8080);
        assertFalse(event1.equals(event2));
        
        event2.setLocalPort(8080);
        assertTrue(event1.equals(event2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertEquals(event1.hashCode(), event2.hashCode());
        
        event1.setLocalPort(8080);
        assertNotEquals(event1.hashCode(), event2.hashCode());
        
        event2.setLocalPort(8080);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String expectedString = "AccessEvent[loggerContext=null, timeStamp=null]";
        assertEquals(expectedString, event.toString());
        
        LoggerContext loggerContext = new LoggerContext();
        Long timeStamp = 1234567890L;
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(timeStamp);
        expectedString = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timeStamp + "]";
        assertEquals(expectedString, event.toString());
    }
}
