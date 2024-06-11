
package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AccessEventTest {
    
    private AccessEvent accessEvent;
    
    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }
    
    @Test
    public void testGetSetTimeStamp() {
        Long timeStamp = 1234567890L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testGetSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    @Test
    public void testGetSetRequestURI() {
        String requestURI = "/api/getInfo";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetSetRequestURL() {
        String requestURL = "http://example.com/api/getInfo";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetSetRemoteHost() {
        String remoteHost = "example.com";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetSetRemoteUser() {
        String remoteUser = "john.doe";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }
    
    @Test
    public void testGetSetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }
    
    @Test
    public void testGetSetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }
    
    @Test
    public void testGetSetServerName() {
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
    
    @Test
    public void testGetSetRemoteAddress() {
        String remoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        requestHeaders.put("Authorization", "Bearer token");
        
        accessEvent.setRequestHeaders(requestHeaders);
        
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        
        accessEvent.setResponseHeaders(responseHeaders);
        
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[] { "value1", "value2" });
        requestParameters.put("param2", new String[] { "value3", "value4" });
        
        accessEvent.setRequestParameters(requestParameters);
        
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }
    
    @Test
    public void testGetSetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }
    
    @Test
    public void testGetSetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1, event2);
        
        event1.setTimeStamp(1234567890L);
        
        assertNotEquals(event1, event2);
        
        event2.setTimeStamp(1234567890L);
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        
        assertEquals(event1.hashCode(), event2.hashCode());
        
        event1.setTimeStamp(1234567890L);
        
        assertNotEquals(event1.hashCode(), event2.hashCode());
        
        event2.setTimeStamp(1234567890L);
        
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        assertNotNull(accessEvent.toString());
    }
}

