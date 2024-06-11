import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getTimeStamp());

        Long timeStamp = 1234567890L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getLoggerContext());

        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestURI());

        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestURL());

        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteHost());

        String remoteHost = "example.com";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteUser());

        String remoteUser = "user";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getProtocol());

        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getMethod());

        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getServerName());

        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRemoteAddress());

        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestHeaders());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getResponseHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getRequestParameters());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1"});
        requestParameters.put("Param2", new String[]{"Value2"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        assertEquals(0, accessEvent.getLocalPort());

        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        assertEquals(0, accessEvent.getStatusCode());

        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        assertNull(accessEvent1.getTimeStamp());
        assertNull(accessEvent1.getLoggerContext());
        assertNull(accessEvent1.getRequestURI());
        assertNull(accessEvent1.getRequestURL());
        assertNull(accessEvent1.getRemoteHost());
        assertNull(accessEvent1.getRemoteUser());
        assertNull(accessEvent1.getProtocol());
        assertNull(accessEvent1.getMethod());
        assertNull(accessEvent1.getServerName());
        assertNull(accessEvent1.getRemoteAddress());
        assertNull(accessEvent1.getRequestHeaders());
        assertNull(accessEvent1.getResponseHeaders());
        assertNull(accessEvent1.getRequestParameters());
        assertEquals(0, accessEvent1.getLocalPort());
        assertEquals(0, accessEvent1.getStatusCode());

        AccessEvent accessEvent2 = new AccessEvent();
        assertNull(accessEvent2.getTimeStamp());
        assertNull(accessEvent2.getLoggerContext());
        assertNull(accessEvent2.getRequestURI());
        assertNull(accessEvent2.getRequestURL());
        assertNull(accessEvent2.getRemoteHost());
        assertNull(accessEvent2.getRemoteUser());
        assertNull(accessEvent2.getProtocol());
        assertNull(accessEvent2.getMethod());
        assertNull(accessEvent2.getServerName());
        assertNull(accessEvent2.getRemoteAddress());
        assertNull(accessEvent2.getRequestHeaders());
        assertNull(accessEvent2.getResponseHeaders());
        assertNull(accessEvent2.getRequestParameters());
        assertEquals(0, accessEvent2.getLocalPort());
        assertEquals(0, accessEvent2.getStatusCode());

        assertEquals(accessEvent1, accessEvent2);

        accessEvent1.setTimeStamp(1234567890L);
        assertNotEquals(accessEvent1, accessEvent2);

        accessEvent2.setTimeStamp(1234567890L);
        assertEquals(accessEvent1, accessEvent2);
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        assertNull(accessEvent1.getTimeStamp());
        assertNull(accessEvent1.getLoggerContext());
        assertNull(accessEvent1.getRequestURI());
        assertNull(accessEvent1.getRequestURL());
        assertNull(accessEvent1.getRemoteHost());
        assertNull(accessEvent1.getRemoteUser());
        assertNull(accessEvent1.getProtocol());
        assertNull(accessEvent1.getMethod());
        assertNull(accessEvent1.getServerName());
        assertNull(accessEvent1.getRemoteAddress());
        assertNull(accessEvent1.getRequestHeaders());
        assertNull(accessEvent1.getResponseHeaders());
        assertNull(accessEvent1.getRequestParameters());
        assertEquals(0, accessEvent1.getLocalPort());
        assertEquals(0, accessEvent1.getStatusCode());

        AccessEvent accessEvent2 = new AccessEvent();
        assertNull(accessEvent2.getTimeStamp());
        assertNull(accessEvent2.getLoggerContext());
        assertNull(accessEvent2.getRequestURI());
        assertNull(accessEvent2.getRequestURL());
        assertNull(accessEvent2.getRemoteHost());
        assertNull(accessEvent2.getRemoteUser());
        assertNull(accessEvent2.getProtocol());
        assertNull(accessEvent2.getMethod());
        assertNull(accessEvent2.getServerName());
        assertNull(accessEvent2.getRemoteAddress());
        assertNull(accessEvent2.getRequestHeaders());
        assertNull(accessEvent2.getResponseHeaders());
        assertNull(accessEvent2.getRequestParameters());
        assertEquals(0, accessEvent2.getLocalPort());
        assertEquals(0, accessEvent2.getStatusCode());

        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        accessEvent1.setTimeStamp(1234567890L);
        assertNotEquals(accessEvent1.hashCode(), accessEvent2.hashCode());

        accessEvent2.setTimeStamp(1234567890L);
        assertEquals(accessEvent1.hashCode(), accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        assertNull(accessEvent.getTimeStamp());
        assertNull(accessEvent.getLoggerContext());

        String expectedToString = "AccessEvent[loggerContext=null, timeStamp=null]";
        assertEquals(expectedToString, accessEvent.toString());

        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        expectedToString = "AccessEvent[loggerContext=" + loggerContext.toString() + ", timeStamp=null]";
        assertEquals(expectedToString, accessEvent.toString());

    }

}