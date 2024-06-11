package de.huxhorn.lilith.data.access;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 1234567890L;
        accessEvent.setTimeStamp(timeStamp);

        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);

        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);

        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "http://www.example.com";
        accessEvent.setRequestURL(requestURL);

        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);

        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "user";
        accessEvent.setRemoteUser(remoteUser);

        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);

        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "POST";
        accessEvent.setMethod(method);

        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "localhost";
        accessEvent.setServerName(serverName);

        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "192.168.0.1";
        accessEvent.setRemoteAddress(remoteAddress);

        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        requestHeaders.put("Authorization", "Bearer token");
        accessEvent.setRequestHeaders(requestHeaders);

        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        responseHeaders.put("Cache-Control", "no-cache");
        accessEvent.setResponseHeaders(responseHeaders);

        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("username", new String[]{"john"});
        requestParameters.put("password", new String[]{"secret"});
        accessEvent.setRequestParameters(requestParameters);

        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);

        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);

        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1234567890L);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(1234567890L);

        assertEquals(accessEvent1, accessEvent2);
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(1234567890L);
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(1234567890L);

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        LoggerContext loggerContext = new LoggerContext();
        loggerContext.setName("MyLogger");
        accessEvent.setLoggerContext(loggerContext);

        assertEquals("AccessEvent[loggerContext=LoggerContext[name=MyLogger], timeStamp=1234567890]", accessEvent.toString());
    }
}