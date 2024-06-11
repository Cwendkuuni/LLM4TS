package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getTimeStamp());
        
        accessEvent.setTimeStamp(12345L);
        assertEquals(12345L, accessEvent.getTimeStamp().longValue());
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getLoggerContext());
        
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestURI());
        
        accessEvent.setRequestURI("/home");
        assertEquals("/home", accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestURL());
        
        accessEvent.setRequestURL("http://example.com");
        assertEquals("http://example.com", accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteHost());
        
        accessEvent.setRemoteHost("example.com");
        assertEquals("example.com", accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteUser());
        
        accessEvent.setRemoteUser("user1");
        assertEquals("user1", accessEvent.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getProtocol());
        
        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getMethod());
        
        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getServerName());
        
        accessEvent.setServerName("example.com");
        assertEquals("example.com", accessEvent.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteAddress());
        
        accessEvent.setRemoteAddress("192.168.0.1");
        assertEquals("192.168.0.1", accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestHeaders());
        
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getResponseHeaders());
        
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestParameters());
        
        Map<String, String[]> requestParams = new HashMap<>();
        requestParams.put("param1", new String[] {"value1", "value2"});
        accessEvent.setRequestParameters(requestParams);
        assertEquals(requestParams, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
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
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(12345L);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(12345L);

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(54321L);

        AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(null);

        // testing equals method
        assertEquals(true, event1.equals(event1)); // same object
        assertEquals(true, event1.equals(event2)); // same attributes
        assertEquals(false, event1.equals(event3)); // different attributes
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(12345L);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(12345L);

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(54321L);

        AccessEvent event4 = new AccessEvent();
        event4.setTimeStamp(null);

        // testing hashCode method
        assertEquals(true, event1.hashCode() == event2.hashCode()); // same attributes
        assertEquals(false, event1.hashCode() == event3.hashCode()); // different attributes
    }
    
    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setTimeStamp(12345L);

        assertEquals("AccessEvent[loggerContext=org.apache.commons.logging.impl.Log4JLogger@12345, timeStamp=12345]", accessEvent.toString());
    }
}