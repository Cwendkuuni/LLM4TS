
package de.huxhorn.lilith.data.access;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 1234567890L;
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
        String requestURI = "/test";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://example.com/test";
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
        String remoteUser = "user";
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
        requestHeaders.put("Key", "Value");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Key", "Value");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Key", new String[] {"Value"});
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
        event1.setTimeStamp(1234567890L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://example.com/test");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.getResponseHeaders().put("Key", "Value");
        event1.getRequestParameters().put("Key", new String[]{"Value"});
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1234567890L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://example.com/test");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.getResponseHeaders().put("Key", "Value");
        event2.getRequestParameters().put("Key", new String[]{"Value"});
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
    }


    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1234567890L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("http://example.com/test");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.getResponseHeaders().put("Key", "Value");
        event1.getRequestParameters().put("Key", new String[]{"Value"});
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1234567890L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("http://example.com/test");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.getResponseHeaders().put("Key", "Value");
        event2.getRequestParameters().put("Key", new String[]{"Value"});
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(1234567890L);
        String expected = "AccessEvent[loggerContext=" + event.getLoggerContext()
                + ", timeStamp=" + event.getTimeStamp() + "]";
        assertEquals(expected, event.toString());
    }

}

