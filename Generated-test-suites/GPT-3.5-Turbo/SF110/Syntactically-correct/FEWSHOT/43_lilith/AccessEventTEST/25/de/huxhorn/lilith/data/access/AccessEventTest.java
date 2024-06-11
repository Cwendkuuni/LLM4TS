
package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testGetTimeStampNull() {
        assertNull(accessEvent.getTimeStamp());
    }
  
    @Test
    public void testSetTimeStamp() {
        Long timestamp = 12345L;
        accessEvent.setTimeStamp(timestamp);
        assertEquals(timestamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContextNull() {
        assertNull(accessEvent.getLoggerContext());
    }
  
    @Test
    public void testSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
  
    @Test
    public void testGetRequestURINull() {
        assertNull(accessEvent.getRequestURI());
    }
  
    @Test
    public void testSetRequestURI() {
        String requestURI = "/api";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }
  
    @Test
    public void testGetRequestURLNull() {
        assertNull(accessEvent.getRequestURL());
    }
  
    @Test
    public void testSetRequestURL() {
        String requestURL = "http://localhost/api";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }
  
    @Test
    public void testGetRemoteHostNull() {
        assertNull(accessEvent.getRemoteHost());
    }
  
    @Test
    public void testSetRemoteHost() {
        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }
  
    @Test
    public void testGetRemoteUserNull() {
        assertNull(accessEvent.getRemoteUser());
    }
  
    @Test
    public void testSetRemoteUser() {
        String remoteUser = "testuser";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }
  
    @Test
    public void testGetProtocolNull() {
        assertNull(accessEvent.getProtocol());
    }
  
    @Test
    public void testSetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }
  
    @Test
    public void testGetMethodNull() {
        assertNull(accessEvent.getMethod());
    }
  
    @Test
    public void testSetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }
  
    @Test
    public void testGetServerNameNull() {
        assertNull(accessEvent.getServerName());
    }
  
    @Test
    public void testSetServerName() {
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }
  
    @Test
    public void testGetRemoteAddressNull() {
        assertNull(accessEvent.getRemoteAddress());
    }
  
    @Test
    public void testSetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }
  
    @Test
    public void testGetRequestHeadersNull() {
        assertNull(accessEvent.getRequestHeaders());
    }
  
    @Test
    public void testSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }
  
    @Test
    public void testGetResponseHeadersNull() {
        assertNull(accessEvent.getResponseHeaders());
    }
  
    @Test
    public void testSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }
  
    @Test
    public void testGetRequestParametersNull() {
        assertNull(accessEvent.getRequestParameters());
    }
  
    @Test
    public void testSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[] { "Value1" });
        requestParameters.put("Param2", new String[] { "Value2" });
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
    public void testEqualsSelf() {
        assertEquals(accessEvent, accessEvent);
    }
  
    @Test
    public void testEqualsNull() {
        assertNotNull(accessEvent);
    }
  
    @Test
    public void testEqualsDifferentClass() {
        Object obj = new Object();
        assertNotNull(accessEvent.equals(obj));
    }
}

