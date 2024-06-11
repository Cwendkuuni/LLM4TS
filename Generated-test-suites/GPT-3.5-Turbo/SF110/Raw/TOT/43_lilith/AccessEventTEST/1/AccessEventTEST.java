package de.huxhorn.lilith.data.access;

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
    public void testGetTimeStamp() {
        Long timeStamp = System.currentTimeMillis();
        accessEvent.setTimeStamp(timeStamp);
        assert(accessEvent.getTimeStamp().equals(timeStamp));
    }

    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assert(accessEvent.getLoggerContext().equals(loggerContext));
    }

    @Test
    public void testGetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assert(accessEvent.getRequestURI().equals(requestURI));
    }

    @Test
    public void testGetRequestURL() {
        String requestURL = "http://www.example.com";
        accessEvent.setRequestURL(requestURL);
        assert(accessEvent.getRequestURL().equals(requestURL));
    }

    @Test
    public void testGetRemoteHost() {
        String remoteHost = "www.example.com";
        accessEvent.setRemoteHost(remoteHost);
        assert(accessEvent.getRemoteHost().equals(remoteHost));
    }

    @Test
    public void testGetRemoteUser() {
        String remoteUser = "user123";
        accessEvent.setRemoteUser(remoteUser);
        assert(accessEvent.getRemoteUser().equals(remoteUser));
    }

    @Test
    public void testGetProtocol() {
        String protocol = "http";
        accessEvent.setProtocol(protocol);
        assert(accessEvent.getProtocol().equals(protocol));
    }

    @Test
    public void testGetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assert(accessEvent.getMethod().equals(method));
    }

    @Test
    public void testGetServerName() {
        String serverName = "exampleServer";
        accessEvent.setServerName(serverName);
        assert(accessEvent.getServerName().equals(serverName));
    }

    @Test
    public void testGetRemoteAddress() {
        String remoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assert(accessEvent.getRemoteAddress().equals(remoteAddress));
    }

    @Test
    public void testGetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        assert(accessEvent.getRequestHeaders().equals(requestHeaders));
    }

    @Test
    public void testGetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);
        assert(accessEvent.getResponseHeaders().equals(responseHeaders));
    }

    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("Parameter1", values);
        accessEvent.setRequestParameters(requestParameters);
        assert(accessEvent.getRequestParameters().equals(requestParameters));
    }

    @Test
    public void testGetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assert(accessEvent.getLocalPort() == localPort);
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assert(accessEvent.getStatusCode() == statusCode);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = accessEvent;

        assert(accessEvent.equals(accessEvent)); // Reflexive
        assert(accessEvent.equals(event2) == event2.equals(accessEvent)); // Symmetric
        assert((accessEvent.equals(event2) && event2.equals(event1)) == accessEvent.equals(event1)); // Transitive
        assert(accessEvent.equals(event1)); // Consistent

        event1.setTimeStamp(System.currentTimeMillis());
        assert(!accessEvent.equals(event1)); // Check inequality
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = accessEvent;

        assert(accessEvent.hashCode() == event2.hashCode());

        event1.setTimeStamp(System.currentTimeMillis());
        assert(accessEvent.hashCode() != event1.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "AccessEvent[loggerContext=null, timeStamp=null]";

        assert(accessEvent.toString().equals(expected));
    }
}