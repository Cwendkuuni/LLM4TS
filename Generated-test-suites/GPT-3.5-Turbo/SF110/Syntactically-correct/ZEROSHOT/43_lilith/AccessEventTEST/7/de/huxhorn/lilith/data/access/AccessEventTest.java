
package de.huxhorn.lilith.data.access;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {
    
    private AccessEvent accessEvent;
    
    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }
    
    @Test
    public void testGetSetTimeStamp() {
        Long timeStamp = 123456789L;
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
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
    
    @Test
    public void testGetSetRequestURL() {
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
    
    @Test
    public void testGetSetRemoteHost() {
        String remoteHost = "localhost";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
    
    @Test
    public void testGetSetRemoteUser() {
        String remoteUser = "user";
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
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
    
    @Test
    public void testGetSetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
    
    @Test
    public void testGetSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("User-Agent", "Mozilla/5.0");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
    
    @Test
    public void testGetSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "text/html");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
    
    @Test
    public void testGetSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param", values);
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
        assertTrue(event1.equals(event2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertEquals(event1.hashCode(), event2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "AccessEvent[loggerContext=null, timeStamp=null]";
        assertEquals(expected, accessEvent.toString());
    }
    
}
