package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AccessEventTest {
    private AccessEvent event;
    
    @Before
    public void setUp() {
        event = new AccessEvent();
    }
    
    @Test
    public void testGetTimeStamp() {
        Long timeStamp = 123456789L;
        event.setTimeStamp(timeStamp);
        
        assertEquals(timeStamp, event.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        
        assertEquals(loggerContext, event.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        String requestURI = "/example";
        event.setRequestURI(requestURI);
        
        assertEquals(requestURI, event.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        String requestURL = "https://www.example.com";
        event.setRequestURL(requestURL);
        
        assertEquals(requestURL, event.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        String remoteHost = "127.0.0.1";
        event.setRemoteHost(remoteHost);
        
        assertEquals(remoteHost, event.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        String remoteUser = "user";
        event.setRemoteUser(remoteUser);
        
        assertEquals(remoteUser, event.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        
        assertEquals(protocol, event.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        String method = "GET";
        event.setMethod(method);
        
        assertEquals(method, event.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        String serverName = "example.com";
        event.setServerName(serverName);
        
        assertEquals(serverName, event.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        
        assertEquals(remoteAddress, event.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        requestHeaders.put("header2", "value2");
        event.setRequestHeaders(requestHeaders);
        
        assertEquals(requestHeaders, event.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        responseHeaders.put("header2", "value2");
        event.setResponseHeaders(responseHeaders);
        
        assertEquals(responseHeaders, event.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[] {"value1"});
        requestParameters.put("param2", new String[] {"value1", "value2"});
        event.setRequestParameters(requestParameters);
        
        assertEquals(requestParameters, event.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        int localPort = 8080;
        event.setLocalPort(localPort);
        
        assertEquals(localPort, event.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        event.setStatusCode(statusCode);
        
        assertEquals(statusCode, event.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());
    }
}