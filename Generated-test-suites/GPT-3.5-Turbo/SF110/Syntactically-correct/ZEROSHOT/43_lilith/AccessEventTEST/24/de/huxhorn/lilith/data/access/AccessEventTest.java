package de.huxhorn.lilith.data.access;

import org.junit.*;
import java.util.*;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setup() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGetSetTimeStamp() {
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        Long result = accessEvent.getTimeStamp();
        Assert.assertEquals(timeStamp, result);
    }

    @Test
    public void testGetSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        LoggerContext result = accessEvent.getLoggerContext();
        Assert.assertEquals(loggerContext, result);
    }

    @Test
    public void testGetSetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        String result = accessEvent.getRequestURI();
        Assert.assertEquals(requestURI, result);
    }

    @Test
    public void testGetSetRequestURL() {
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        String result = accessEvent.getRequestURL();
        Assert.assertEquals(requestURL, result);
    }

    @Test
    public void testGetSetRemoteHost() {
        String remoteHost = "example.com";
        accessEvent.setRemoteHost(remoteHost);
        String result = accessEvent.getRemoteHost();
        Assert.assertEquals(remoteHost, result);
    }

    @Test
    public void testGetSetRemoteUser() {
        String remoteUser = "user";
        accessEvent.setRemoteUser(remoteUser);
        String result = accessEvent.getRemoteUser();
        Assert.assertEquals(remoteUser, result);
    }

    @Test
    public void testGetSetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        String result = accessEvent.getProtocol();
        Assert.assertEquals(protocol, result);
    }

    @Test
    public void testGetSetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        String result = accessEvent.getMethod();
        Assert.assertEquals(method, result);
    }

    @Test
    public void testGetSetServerName() {
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        String result = accessEvent.getServerName();
        Assert.assertEquals(serverName, result);
    }

    @Test
    public void testGetSetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        String result = accessEvent.getRemoteAddress();
        Assert.assertEquals(remoteAddress, result);
    }

    @Test
    public void testGetSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        Map<String, String> result = accessEvent.getRequestHeaders();
        Assert.assertEquals(requestHeaders, result);
    }

    @Test
    public void testGetSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        Map<String, String> result = accessEvent.getResponseHeaders();
        Assert.assertEquals(responseHeaders, result);
    }

    @Test
    public void testGetSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param1", values);
        accessEvent.setRequestParameters(requestParameters);
        Map<String, String[]> result = accessEvent.getRequestParameters();
        Assert.assertEquals(requestParameters, result);
    }

    @Test
    public void testGetSetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        int result = accessEvent.getLocalPort();
        Assert.assertEquals(localPort, result);
    }

    @Test
    public void testGetSetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        int result = accessEvent.getStatusCode();
        Assert.assertEquals(statusCode, result);
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");
        event1.setRequestURL("http://example.com");
        event1.setRemoteHost("example.com");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");
        event2.setRequestURL("http://example.com");
        event2.setRemoteHost("example.com");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        Assert.assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");
        event1.setRequestURL("http://example.com");
        event1.setRemoteHost("example.com");
        event1.setRemoteUser("user");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");
        event2.setRequestURL("http://example.com");
        event2.setRemoteHost("example.com");
        event2.setRemoteUser("user");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        int hashCode1 = event1.hashCode();
        int hashCode2 = event2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(123456789L);

        String expected = "AccessEvent[loggerContext=" + event.getLoggerContext() + ", timeStamp=" + event.getTimeStamp() + "]";
        String result = event.toString();

        Assert.assertEquals(expected, result);
    }
}