import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGetTimeStamp() {
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        String remoteHost = "localhost";
        accessEvent.setRemoteHost(remoteHost);
        
        assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        String remoteUser = "user123";
        accessEvent.setRemoteUser(remoteUser);
        
        assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        
        assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        String method = "GET";
        accessEvent.setMethod(method);
        
        assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        
        assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        
        assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Header1", "Value1");
        requestHeaders.put("Header2", "Value2");
        accessEvent.setRequestHeaders(requestHeaders);
        
        assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Header1", "Value1");
        responseHeaders.put("Header2", "Value2");
        accessEvent.setResponseHeaders(responseHeaders);

        assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("Param1", new String[]{"Value1"});
        requestParameters.put("Param2", new String[]{"Value2", "Value3"});
        accessEvent.setRequestParameters(requestParameters);

        assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        
        assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        
        assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");
        event1.setRequestURL("http://example.com");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user123");
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
        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");
        event2.setRequestURL("http://example.com");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user123");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(987654321L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/example");
        event3.setRequestURL("http://example.com");
        event3.setRemoteHost("localhost");
        event3.setRemoteUser("user123");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("example.com");
        event3.setRemoteAddress("127.0.0.1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertTrue(event1.equals(event2));
        assertFalse(event1.equals(event3));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");
        event1.setRequestURL("http://example.com");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("user123");
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
        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");
        event2.setRequestURL("http://example.com");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("user123");
        event2.setProtocol("HTTP/1.1");
        event2.setMethod("GET");
        event2.setServerName("example.com");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRequestHeaders(new HashMap<>());
        event2.setResponseHeaders(new HashMap<>());
        event2.setRequestParameters(new HashMap<>());
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(987654321L);
        event3.setLoggerContext(new LoggerContext());
        event3.setRequestURI("/example");
        event3.setRequestURL("http://example.com");
        event3.setRemoteHost("localhost");
        event3.setRemoteUser("user123");
        event3.setProtocol("HTTP/1.1");
        event3.setMethod("GET");
        event3.setServerName("example.com");
        event3.setRemoteAddress("127.0.0.1");
        event3.setRequestHeaders(new HashMap<>());
        event3.setResponseHeaders(new HashMap<>());
        event3.setRequestParameters(new HashMap<>());
        event3.setLocalPort(8080);
        event3.setStatusCode(200);

        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    public void testToString() {
        accessEvent.setTimeStamp(123456789L);
        accessEvent.setLoggerContext(new LoggerContext());

        String expected = "AccessEvent[loggerContext=de.huxhorn.lilith.data.access.LoggerContext@<address>, timeStamp=123456789]";
        
        assertEquals(expected, accessEvent.toString());
    }
}