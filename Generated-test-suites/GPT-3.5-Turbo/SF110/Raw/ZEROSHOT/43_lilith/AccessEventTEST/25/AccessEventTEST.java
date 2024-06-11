import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGetAndSetTimeStamp() {
        Long timeStamp = 12345L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetAndSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetAndSetRequestURI() {
        String requestURI = "/path/endpoint";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetAndSetRequestURL() {
        String requestURL = "http://www.example.com/path/endpoint";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetAndSetRemoteHost() {
        String remoteHost = "127.0.0.1";
        accessEvent.setRemoteHost(remoteHost);
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetAndSetRemoteUser() {
        String remoteUser = "admin";
        accessEvent.setRemoteUser(remoteUser);
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetAndSetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetAndSetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetAndSetServerName() {
        String serverName = "localhost";
        accessEvent.setServerName(serverName);
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetAndSetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetAndSetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetAndSetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetAndSetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1", "Value2"});
        requestParameters.put("Param2", new String[]{"Value3"});
        accessEvent.setRequestParameters(requestParameters);
        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetAndSetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetAndSetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(12345L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/path/endpoint");
        event1.setRequestURL("http://www.example.com/path/endpoint");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("admin");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(12345L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/path/endpoint");
        event2.setRequestURL("http://www.example.com/path/endpoint");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("admin");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
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
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(12345L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/path/endpoint");
        event1.setRequestURL("http://www.example.com/path/endpoint");
        event1.setRemoteHost("127.0.0.1");
        event1.setRemoteUser("admin");
        event1.setProtocol("HTTP/1.1");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRequestHeaders(new HashMap<>());
        event1.setResponseHeaders(new HashMap<>());
        event1.setRequestParameters(new HashMap<>());
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(12345L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/path/endpoint");
        event2.setRequestURL("http://www.example.com/path/endpoint");
        event2.setRemoteHost("127.0.0.1");
        event2.setRemoteUser("admin");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setTimeStamp(12345L);

        String expected = "AccessEvent[loggerContext=de.huxhorn.lilith.data.access.LoggerContext@dummy, timeStamp=12345]";
        assertEquals(expected, accessEvent.toString());
    }
}