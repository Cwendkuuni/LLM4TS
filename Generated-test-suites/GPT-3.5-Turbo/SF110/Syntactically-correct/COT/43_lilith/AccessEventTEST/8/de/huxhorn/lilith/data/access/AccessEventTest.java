
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        assert(accessEvent.getTimeStamp() == timeStamp);
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assert(accessEvent.getLoggerContext() == loggerContext);
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assert(accessEvent.getRequestURI().equals(requestURI));
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        assert(accessEvent.getRequestURL().equals(requestURL));
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "localhost";
        accessEvent.setRemoteHost(remoteHost);
        assert(accessEvent.getRemoteHost().equals(remoteHost));
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "admin";
        accessEvent.setRemoteUser(remoteUser);
        assert(accessEvent.getRemoteUser().equals(remoteUser));
    }

    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        assert(accessEvent.getProtocol().equals(protocol));
    }

    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "GET";
        accessEvent.setMethod(method);
        assert(accessEvent.getMethod().equals(method));
    }

    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        assert(accessEvent.getServerName().equals(serverName));
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        assert(accessEvent.getRemoteAddress().equals(remoteAddress));
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        assert(accessEvent.getRequestHeaders().equals(requestHeaders));
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        assert(accessEvent.getResponseHeaders().equals(responseHeaders));
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param", values);
        accessEvent.setRequestParameters(requestParameters);
        assert(accessEvent.getRequestParameters().equals(requestParameters));
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        assert(accessEvent.getLocalPort() == localPort);
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        assert(accessEvent.getStatusCode() == statusCode);
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        assert(accessEvent1.equals(accessEvent1));
        assert(accessEvent1.equals(accessEvent2));

        accessEvent1.setRequestURL("http://example.com");
        assert(!accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();

        assert(accessEvent1.hashCode() == accessEvent2.hashCode());

        accessEvent1.setRequestURI("/example");
        assert(accessEvent1.hashCode() != accessEvent2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setTimeStamp(123456789L);

        String expectedString = "AccessEvent[loggerContext=LoggerContext[], timeStamp=123456789]";
        assert(accessEvent.toString().equals(expectedString));
    }
}
