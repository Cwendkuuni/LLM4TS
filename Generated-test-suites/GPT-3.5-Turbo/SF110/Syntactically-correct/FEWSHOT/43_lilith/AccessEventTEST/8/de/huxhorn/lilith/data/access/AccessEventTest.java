package de.huxhorn.lilith.data.access;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long expected = 12345L;
        event.setTimeStamp(expected);
        
        Long actual = event.getTimeStamp();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext expected = new LoggerContext();
        event.setLoggerContext(expected);
        
        LoggerContext actual = event.getLoggerContext();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        String expected = "/example";
        event.setRequestURI(expected);
        
        String actual = event.getRequestURI();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String expected = "http://example.com";
        event.setRequestURL(expected);
        
        String actual = event.getRequestURL();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        String expected = "localhost";
        event.setRemoteHost(expected);
        
        String actual = event.getRemoteHost();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        String expected = "admin";
        event.setRemoteUser(expected);
        
        String actual = event.getRemoteUser();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        String expected = "HTTP/1.1";
        event.setProtocol(expected);
        
        String actual = event.getProtocol();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        String expected = "GET";
        event.setMethod(expected);
        
        String actual = event.getMethod();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        String expected = "localhost";
        event.setServerName(expected);
        
        String actual = event.getServerName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        String expected = "127.0.0.1";
        event.setRemoteAddress(expected);
        
        String actual = event.getRemoteAddress();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expected = new HashMap<>();
        expected.put("Content-Type", "application/json");
        event.setRequestHeaders(expected);
        
        Map<String, String> actual = event.getRequestHeaders();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> expected = new HashMap<>();
        expected.put("Content-Type", "application/json");
        event.setResponseHeaders(expected);
        
        Map<String, String> actual = event.getResponseHeaders();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> expected = new HashMap<>();
        expected.put("query", new String[]{"value"});
        event.setRequestParameters(expected);
        
        Map<String, String[]> actual = event.getRequestParameters();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        int expected = 8080;
        event.setLocalPort(expected);
        
        int actual = event.getLocalPort();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        int expected = 200;
        event.setStatusCode(expected);
        
        int actual = event.getStatusCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        int expected = event.getTimeStamp().hashCode();
        
        int actual = event.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String expected = "AccessEvent[loggerContext=null, timeStamp=null]";
        
        String actual = event.toString();
        assertEquals(expected, actual);
    }
}