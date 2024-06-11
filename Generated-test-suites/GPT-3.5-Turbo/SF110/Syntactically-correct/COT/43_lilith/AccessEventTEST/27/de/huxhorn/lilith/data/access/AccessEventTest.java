
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {
    private AccessEvent accessEvent;

    @Test
    public void testGetTimeStamp() {
        accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(1234567890L);
        assertSame(1234567890L, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertSame(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        accessEvent = new AccessEvent();
        accessEvent.setRequestURI("/path/to/resource");
        assertEquals("/path/to/resource", accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        accessEvent = new AccessEvent();
        accessEvent.setRequestURL("http://www.example.com/path/to/resource");
        assertEquals("http://www.example.com/path/to/resource", accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        accessEvent = new AccessEvent();
        accessEvent.setRemoteHost("localhost");
        assertEquals("localhost", accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        accessEvent = new AccessEvent();
        accessEvent.setRemoteUser("user");
        assertEquals("user", accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        accessEvent = new AccessEvent();
        accessEvent.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        accessEvent = new AccessEvent();
        accessEvent.setMethod("GET");
        assertEquals("GET", accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        accessEvent = new AccessEvent();
        accessEvent.setServerName("localhost");
        assertEquals("localhost", accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        accessEvent = new AccessEvent();
        accessEvent.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        accessEvent = new AccessEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(headers);
        assertEquals(headers, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        accessEvent = new AccessEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(headers);
        assertEquals(headers, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        accessEvent = new AccessEvent();
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("param1", new String[]{"value1", "value2"});
        accessEvent.setRequestParameters(parameters);
        assertEquals(parameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        accessEvent = new AccessEvent();
        accessEvent.setLocalPort(8080);
        assertEquals(8080, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        accessEvent = new AccessEvent();
        accessEvent.setStatusCode(200);
        assertEquals(200, accessEvent.getStatusCode());
    }
}
