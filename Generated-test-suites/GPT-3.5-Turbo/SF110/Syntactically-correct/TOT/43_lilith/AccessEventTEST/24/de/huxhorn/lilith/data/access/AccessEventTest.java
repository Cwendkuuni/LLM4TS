package de.huxhorn.lilith.data.access;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 123456789L;
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
        String remoteHost = "localhost";
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
        requestHeaders.put("Authorization", "Bearer token");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        responseHeaders.put("Cache-Control", "no-cache");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
        requestParameters.put("param2", new String[]{"value2", "value3"});
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
        event1.setRequestURI("/example");
        event1.setMethod("GET");
        event1.setStatusCode(200);
        
        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/example");
        event2.setMethod("GET");
        event2.setStatusCode(200);
        
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURI("/example");
        event1.setMethod("GET");
        event1.setStatusCode(200);
        
        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/example");
        event2.setMethod("GET");
        event2.setStatusCode(200);
        
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/example");
        event.setMethod("GET");
        
        String expectedString = "AccessEvent[loggerContext=null, timeStamp=null]";
        assertEquals(expectedString, event.toString());
    }
}