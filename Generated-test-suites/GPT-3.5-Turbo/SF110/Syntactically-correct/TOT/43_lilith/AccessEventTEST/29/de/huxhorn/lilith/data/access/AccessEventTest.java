import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    private AccessEvent accessEvent;
    
    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }
    
    @Test
    public void testGetTimeStamp() {
        Long timeStamp = 1000L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testSetTimeStamp() {
        Long timeStamp = 2000L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
    
    @Test
    public void testSetRequestURI() {
        String requestURI = "/test";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
    
    @Test
    public void testSetRequestURL() {
        String requestURL = "http://test.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        String remoteHost = "example.com";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
    
    @Test
    public void testSetRemoteHost() {
        String remoteHost = "test.com";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        String remoteUser = "user1";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }
    
    @Test
    public void testSetRemoteUser() {
        String remoteUser = "user2";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }
    
    @Test
    public void testSetProtocol() {
        String protocol = "HTTP/2.0";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }
    
    @Test
    public void testSetMethod() {
        String method = "POST";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
    
    @Test
    public void testSetServerName() {
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testSetRemoteAddress() {
        String remoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeader(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/xml");
        accessEvent.setRequestHeader(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/xml");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param1", values);
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value3", "value4"};
        requestParameters.put("param2", values);
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }
    
    @Test
    public void testSetLocalPort() {
        int localPort = 8888;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }
    
    @Test
    public void testSetStatusCode() {
        int statusCode = 404;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1000L);
        assertTrue(event1.equals(event2));
    }
    
    @Test
    public void testEquals_SameObject() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        assertTrue(event1.equals(event1));
    }
    
    @Test
    public void testEquals_NullObject() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        assertFalse(event1.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        assertFalse(event1.equals("test"));
    }
    
    @Test
    public void testEquals_DifferentTimeStamp() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(2000L);
        assertFalse(event1.equals(event2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1000L);
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1000L);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(1000L);
        assertEquals("AccessEvent[loggerContext=com.example.LoggerContext@1234, timeStamp=1000]", event.toString());
    }
}