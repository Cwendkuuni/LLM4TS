import static org.junit.Assert.*;
import org.junit.Test;
import java.io.Serializable;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());

        Long timeStamp = System.currentTimeMillis();
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

        String requestURI = "/test";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestURL());

        String requestURL = "http://localhost/test";
        event.setRequestURL(requestURL);
        assertEquals(requestURL, event.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRemoteHost());

        String remoteHost = "localhost";
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

        String serverName = "localhost";
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
        requestHeaders.put("Content-Type", "application/json");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getResponseHeaders());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getRequestParameters());

        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param1", values);
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
        assertTrue(event1.equals(event2));

        event1.setTimeStamp(System.currentTimeMillis());
        assertFalse(event1.equals(event2));

        event2.setTimeStamp(event1.getTimeStamp());
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setTimeStamp(System.currentTimeMillis());
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setTimeStamp(event1.getTimeStamp());
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertTrue(event.toString().contains("loggerContext="));
    }
}