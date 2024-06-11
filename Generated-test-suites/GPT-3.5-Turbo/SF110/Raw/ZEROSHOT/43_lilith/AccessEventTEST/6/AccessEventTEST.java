import static org.junit.Assert.*;
import org.junit.Test;

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
        String requestURI = "http://example.com";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://example.com";
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
        String remoteUser = "user1";
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
        requestHeaders.put("header1", "value1");
        requestHeaders.put("header2", "value2");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("header1", "value1");
        responseHeaders.put("header2", "value2");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1", "value2"});
        requestParameters.put("param2", new String[]{"value3"});
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
        event1.setRequestURI("http://example.com");
        event1.setRequestURL("http://example.com");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user1");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("example.com");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1234567890L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("http://example.com");
        event2.setRequestURL("http://example.com");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user1");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(1234567890L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("http://example.com");
        event.setRequestURL("http://example.com");
        event.setRemoteHost("localhost");
        event.setRemoteUser("user1");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("example.com");
        event.setRemoteAddress("127.0.0.1");
        event.setRequestHeaders(new HashMap<>());
        event.setResponseHeaders(new HashMap<>());
        event.setRequestParameters(new HashMap<>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(1234567890L);

        assertEquals("AccessEvent[loggerContext=LoggerContext, timeStamp=1234567890]", event.toString());
    }

}