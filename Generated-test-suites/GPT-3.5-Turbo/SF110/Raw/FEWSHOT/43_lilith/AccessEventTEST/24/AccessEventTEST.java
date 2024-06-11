package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.HashMap;


public class AccessEventTest {
    
    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();
        
        Long timeStamp = 123456789L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
        
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
        
        String requestURI = "http://example.com";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
        
        String requestURL = "http://example.com/index.html";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
        
        String remoteHost = "example.com";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
        
        String remoteUser = "user";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
        
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
        
        String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
        
        String serverName = "example.com";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
        
        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
        
        Map<String, String> requestHeaders = new HashMap<>();
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
        
        Map<String, String> responseHeaders = new HashMap<>();
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
        
        Map<String, String[]> requestParameters = new HashMap<>();
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
        
        int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
        
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        // Test equal objects
        assertEquals(event1, event2);
        
        // Test with different values
        event1.setTimeStamp(123456789L);
        event2.setTimeStamp(987654321L);
        assertEquals(event1, event2);
        
        // Test with null values
        event1.setRequestURI(null);
        event2.setRequestURI(null);
        assertEquals(event1, event2);
        
        event1.setRequestURI("http://example.com");
        assertEquals(false, event1.equals(null));
        assertEquals(false, event1.equals("string"));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        // Test equal objects
        assertEquals(event1.hashCode(), event2.hashCode());
        
        // Test with different values
        event1.setTimeStamp(123456789L);
        event2.setTimeStamp(987654321L);
        assertEquals(event1.hashCode(), event2.hashCode());
        
        // Test with null values
        event1.setRequestURI(null);
        event2.setRequestURI(null);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String toString = event.toString();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", toString);
    }
}