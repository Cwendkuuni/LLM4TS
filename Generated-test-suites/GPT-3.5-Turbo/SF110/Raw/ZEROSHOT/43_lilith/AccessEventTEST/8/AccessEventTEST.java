
package de.huxhorn.lilith.data.access;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccessEventTest {
    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGetTimeStamp() {
        Long timeStamp = 123456789L;
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
    public void testGetRequestURI() {
        String requestURI = "/path";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        String requestURL = "http://example.com/path";
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
    public void testGetRemoteUser() {
        String remoteUser = "user";
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
    public void testGetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        String remoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1"});
        requestParameters.put("Param2", new String[]{"Value2a", "Value2b"});
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
    public void testGetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURI("/path");

        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/path");

        AccessEvent event3 = new AccessEvent();
        event3.setRequestURI("/differentPath");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURI("/path");

        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/path");

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/path");
        event.setTimeStamp(123456789L);

        String expected = "AccessEvent[loggerContext=null, timeStamp=123456789]";
        assertEquals(expected, event.toString());
    }
}
