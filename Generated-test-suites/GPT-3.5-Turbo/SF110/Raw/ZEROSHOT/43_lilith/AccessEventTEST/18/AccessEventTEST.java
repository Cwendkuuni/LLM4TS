import static org.junit.Assert.*;
import org.junit.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());

        Long timeStamp = 123456789L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getLoggerContext());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestURI());

        String requestURI = "http://example.com";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestURL());

        String requestURL = "http://example.com";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteHost());

        String remoteHost = "example.com";
        event.setRemoteHost(remoteHost);
        assertEquals(remoteHost, event.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteUser());

        String remoteUser = "user";
        event.setRemoteUser(remoteUser);
        assertEquals(remoteUser, event.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getProtocol());

        String protocol = "HTTP/1.1";
        event.setProtocol(protocol);
        assertEquals(protocol, event.getProtocol());
    }

    @Test
    public void testGetMethod() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getMethod());

        String method = "GET";
        event.setMethod(method);
        assertEquals(method, event.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getServerName());

        String serverName = "example.com";
        event.setServerName(serverName);
        assertEquals(serverName, event.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteAddress());

        String remoteAddress = "127.0.0.1";
        event.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, event.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestHeaders());

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("header1", "value1");
        requestHeaders.put("header2", "value2");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getResponseHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        responseHeaders.put("header2", "value2");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestParameters());

        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
        requestParameters.put("param2", new String[]{"value2"});
        event.setRequestParameters(requestParameters);
        assertEquals(requestParameters, event.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.getLocalPort());

        int localPort = 8080;
        event.setLocalPort(localPort);
        assertEquals(localPort, event.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.getStatusCode());

        int statusCode = 200;
        event.setStatusCode(statusCode);
        assertEquals(statusCode, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertTrue(event1.equals(event1));

        assertFalse(event1.equals(null));
        assertFalse(event1.equals(new Object()));
        assertFalse(event1.equals(event2));

        event1.setLocalPort(8080);
        assertFalse(event1.equals(event2));
        event2.setLocalPort(8080);
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        assertEquals(0, event.hashCode());

        event.setTimeStamp(123456789L);
        event.setLocalPort(8080);
        assertEquals(2162822, event.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(123456789L);
        assertEquals("AccessEvent[loggerContext=" + loggerContext.toString() + ", timeStamp=123456789]", event.toString());
    }
}