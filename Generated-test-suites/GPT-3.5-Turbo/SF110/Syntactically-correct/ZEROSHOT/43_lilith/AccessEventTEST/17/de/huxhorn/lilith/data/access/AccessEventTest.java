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
        assertNull(accessEvent.getTimeStamp());

        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);

        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        assertNull(accessEvent.getLoggerContext());

        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);

        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        assertNull(accessEvent.getRequestURI());

        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);

        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        assertNull(accessEvent.getRequestURL());

        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);

        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        assertNull(accessEvent.getRemoteHost());

        String remoteHost = "example.com";
        accessEvent.setRemoteHost(remoteHost);

        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        assertNull(accessEvent.getRemoteUser());

        String remoteUser = "user123";
        accessEvent.setRemoteUser(remoteUser);

        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        assertNull(accessEvent.getProtocol());

        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);

        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        assertNull(accessEvent.getMethod());

        String method = "GET";
        accessEvent.setMethod(method);

        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        assertNull(accessEvent.getServerName());

        String serverName = "localhost";
        accessEvent.setServerName(serverName);

        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        assertNull(accessEvent.getRemoteAddress());

        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);

        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        assertNull(accessEvent.getRequestHeaders());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);

        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        assertNull(accessEvent.getResponseHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Length", "100");
        accessEvent.setResponseHeaders(responseHeaders);

        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        assertNull(accessEvent.getRequestParameters());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("filter", new String[]{"abc", "123"});
        accessEvent.setRequestParameters(requestParameters);

        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        assertEquals(0, accessEvent.getLocalPort());

        int localPort = 8080;
        accessEvent.setLocalPort(localPort);

        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(0, accessEvent.getStatusCode());

        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);

        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertTrue(event1.equals(event2));

        event1.setLocalPort(8080);
        assertFalse(event1.equals(event2));

        event2.setLocalPort(8080);
        assertTrue(event1.equals(event2));

        event1.setStamp(123456789L);
        assertFalse(event1.equals(event2));

        event2.setStamp(123456789L);
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURI("/example");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRequestURI("/example");
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "AccessEvent[loggerContext=null, timeStamp=null]";

        assertEquals(expected, accessEvent.toString());
    }
}