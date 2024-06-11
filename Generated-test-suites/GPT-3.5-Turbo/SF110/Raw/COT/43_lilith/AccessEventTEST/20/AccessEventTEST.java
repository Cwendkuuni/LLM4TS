
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {
    
    private AccessEvent accessEvent;
    
    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }
    
    @Test
    public void testGetTimeStamp() {
        assertNull(accessEvent.getTimeStamp());
    }
    
    @Test
    public void testSetTimeStamp() {
        Long timeStamp = 1234L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        assertNull(accessEvent.getLoggerContext());
    }
    
    @Test
    public void testSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        assertNull(accessEvent.getRequestURI());
    }
    
    @Test
    public void testSetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        assertNull(accessEvent.getRequestURL());
    }
    
    @Test
    public void testSetRequestURL() {
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        assertNull(accessEvent.getRemoteHost());
    }
    
    @Test
    public void testSetRemoteHost() {
        String remoteHost = "localhost";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        assertNull(accessEvent.getRemoteUser());
    }
    
    @Test
    public void testSetRemoteUser() {
        String remoteUser = "admin";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        assertNull(accessEvent.getProtocol());
    }
    
    @Test
    public void testSetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        assertNull(accessEvent.getMethod());
    }
    
    @Test
    public void testSetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        assertNull(accessEvent.getServerName());
    }
    
    @Test
    public void testSetServerName() {
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        assertNull(accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testSetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        assertNull(accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Accept", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        assertNull(accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "text/html");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        assertNull(accessEvent.getRequestParameters());
    }
    
    @Test
    public void testSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param1", values);
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        assertEquals(0, accessEvent.getLocalPort());
    }
    
    @Test
    public void testSetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(0, accessEvent.getStatusCode());
    }
    
    @Test
    public void testSetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertTrue(event1.equals(event1));
        assertFalse(event1.equals(null));
        assertFalse(event1.equals(new Object()));
        assertTrue(event1.equals(event2));
        
        event1.setLocalPort(8080);
        assertFalse(event1.equals(event2));
        
        event2.setLocalPort(8080);
        assertTrue(event1.equals(event2));
        
        event1.setLoggerContext(new LoggerContext());
        assertFalse(event1.equals(event2));
        
        event2.setLoggerContext(new LoggerContext());
        assertTrue(event1.equals(event2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1.hashCode(), event1.hashCode());
        assertNotEquals(event1.hashCode(), new Object().hashCode());
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(1234L);
        
        String expected = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=1234]";
        assertEquals(expected, event.toString());
    }
}
