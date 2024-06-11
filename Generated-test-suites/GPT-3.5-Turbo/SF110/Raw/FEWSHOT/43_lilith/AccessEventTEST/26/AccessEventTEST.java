package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());
        
        Long timeStamp = 1234567890L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getLoggerContext());
        
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestURI());
        
        String requestURI = "/example";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestURL());
        
        String requestURL = "http://www.example.com";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteHost());
        
        String remoteHost = "localhost";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteUser());
        
        String remoteUser = "user";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getProtocol());
        
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getMethod());
        
        String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getServerName());
        
        String serverName = "localhost";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteAddress());
        
        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestHeaders());
        
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getResponseHeaders());
        
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestParameters());
        
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[] {"value1"});
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.getLocalPort());
        
        int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.getStatusCode());
        
        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1234567890L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");
        event1.setRequestURL("http://www.example.com");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<String, String>());
        event1.setResponseHeaders(new HashMap<String, String>());
        event1.setRequestParameters(new HashMap<String, String[]>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1234567890L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");
        event2.setRequestURL("http://www.example.com");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<String, String>());
        event2.setResponseHeaders(new HashMap<String, String>());
        event2.setRequestParameters(new HashMap<String, String[]>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.hashCode());
        
        Long timeStamp = 1234567890L;
        event.setTimeStamp(timeStamp);
        
        int hashCode = timeStamp.hashCode() * 31;
        assertEquals(hashCode, event.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(1234567890L);
        
        String expected = "AccessEvent[loggerContext=" + event.getLoggerContext() + ", timeStamp=" + event.getTimeStamp() + "]";
        assertEquals(expected, event.toString());
    }
}