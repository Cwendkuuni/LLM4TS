import static org.junit.Assert.*;
import org.junit.*;

public class AccessEventTest {

    private AccessEvent event;
    
    @Before
    public void setUp() {
        event = new AccessEvent();
    }
    
    @Test
    public void testGetTimeStamp() {
        event.setTimeStamp(1234567890L);
        assertEquals(1234567890L, event.getTimeStamp().longValue());
    }
    
    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }
    
    @Test
    public void testGetRequestURI() {
        event.setRequestURI("/example");
        assertEquals("/example", event.getRequestURI());
    }
    
    @Test
    public void testGetRequestURL() {
        event.setRequestURL("http://www.example.com");
        assertEquals("http://www.example.com", event.getRequestURL());
    }
    
    @Test
    public void testGetRemoteHost() {
        event.setRemoteHost("localhost");
        assertEquals("localhost", event.getRemoteHost());
    }
    
    @Test
    public void testGetRemoteUser() {
        event.setRemoteUser("user");
        assertEquals("user", event.getRemoteUser());
    }
    
    @Test
    public void testGetProtocol() {
        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());
    }
    
    @Test
    public void testGetMethod() {
        event.setMethod("GET");
        assertEquals("GET", event.getMethod());
    }
    
    @Test
    public void testGetServerName() {
        event.setServerName("example.com");
        assertEquals("example.com", event.getServerName());
    }
    
    @Test
    public void testGetRemoteAddress() {
        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());
    }
    
    @Test
    public void testGetRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        event.setRequestHeaders(headers);
        assertEquals(headers, event.getRequestHeaders());
    }
    
    @Test
    public void testGetResponseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        event.setResponseHeaders(headers);
        assertEquals(headers, event.getResponseHeaders());
    }
    
    @Test
    public void testGetRequestParameters() {
        Map<String, String[]> parameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        parameters.put("param", values);
        event.setRequestParameters(parameters);
        assertEquals(parameters, event.getRequestParameters());
    }
    
    @Test
    public void testGetLocalPort() {
        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());
    }
    
    @Test
    public void testGetStatusCode() {
        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setTimeStamp(1234567890L);
        
        AccessEvent event2 = new AccessEvent();
        event2.setTimeStamp(1234567890L);
        
        AccessEvent event3 = new AccessEvent();
        event3.setTimeStamp(1234567891L);
        
        assertTrue(event1.equals(event2));
        assertFalse(event1.equals(event3));
    }
    
    @Test
    public void testHashCode() {
        event.setTimeStamp(1234567890L);
        assertEquals(1234567890L, event.hashCode());
    }
    
    @Test
    public void testToString() {
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(1234567890L);
        
        assertEquals("AccessEvent[loggerContext=" + loggerContext.toString() + ", timeStamp=1234567890]", event.toString());
    }
}