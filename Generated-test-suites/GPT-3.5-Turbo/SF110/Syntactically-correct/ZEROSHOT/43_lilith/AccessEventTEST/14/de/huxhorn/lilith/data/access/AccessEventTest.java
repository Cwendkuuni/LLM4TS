import org.junit.Test;
import static org.junit.Assert.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 123456789L;
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
        String requestURI = "www.example.com";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        String requestURL = "http://www.example.com";
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
        String remoteUser = "admin";
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
        String serverName = "localhost";
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
        requestHeaders.put("Content-Type", "application/json");
        event.setRequestHeaders(requestHeaders);
        assertEquals(requestHeaders, event.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        event.setResponseHeaders(responseHeaders);
        assertEquals(responseHeaders, event.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        requestParameters.put("param1", new String[]{"value1"});
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
        event1.setLocalPort(8080);
        event1.setStatusCode(200);
        event1.setLoggerContext(new LoggerContext());
        event1.setMethod("GET");
        event1.setProtocol("HTTP/1.1");
        event1.setRemoteAddress("127.0.0.1");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("admin");
        event1.setRequestHeaders(new HashMap<>());
        event1.setRequestURI("www.example.com");
        event1.setRequestURL("http://www.example.com");
        event1.setResponseHeaders(new HashMap<>());
        event1.setServerName("localhost");
        event1.setTimeStamp(123456789L);
        
        AccessEvent event2 = new AccessEvent();
        event2.setLocalPort(8080);
        event2.setStatusCode(200);
        event2.setLoggerContext(new LoggerContext());
        event2.setMethod("GET");
        event2.setProtocol("HTTP/1.1");
        event2.setRemoteAddress("127.0.0.1");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("admin");
        event2.setRequestHeaders(new HashMap<>());
        event2.setRequestURI("www.example.com");
        event2.setRequestURL("http://www.example.com");
        event2.setResponseHeaders(new HashMap<>());
        event2.setServerName("localhost");
        event2.setTimeStamp(123456789L);
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        int hashCode = event.hashCode();
        
        assertEquals(hashCode, event.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        String loggerContextString = new LoggerContext().toString();
        String expected = "AccessEvent[loggerContext=" + loggerContextString + ", timeStamp=null]";
        
        assertEquals(expected, event.toString());
    }
}