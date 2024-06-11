import org.junit.Assert;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        Assert.assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        Assert.assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        Assert.assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent accessEvent = new AccessEvent();
        String requestURL = "http://example.com";
        accessEvent.setRequestURL(requestURL);
        Assert.assertEquals(requestURL, accessEvent.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteHost = "localhost";
        accessEvent.setRemoteHost(remoteHost);
        Assert.assertEquals(remoteHost, accessEvent.getRemoteHost());
    }

    @Test
    public void testGetRemoteUser() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteUser = "user";
        accessEvent.setRemoteUser(remoteUser);
        Assert.assertEquals(remoteUser, accessEvent.getRemoteUser());
    }

    @Test
    public void testGetProtocol() {
        AccessEvent accessEvent = new AccessEvent();
        String protocol = "HTTP/1.1";
        accessEvent.setProtocol(protocol);
        Assert.assertEquals(protocol, accessEvent.getProtocol());
    }

    @Test
    public void testGetMethod() {
        AccessEvent accessEvent = new AccessEvent();
        String method = "GET";
        accessEvent.setMethod(method);
        Assert.assertEquals(method, accessEvent.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent accessEvent = new AccessEvent();
        String serverName = "example.com";
        accessEvent.setServerName(serverName);
        Assert.assertEquals(serverName, accessEvent.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent accessEvent = new AccessEvent();
        String remoteAddress = "127.0.0.1";
        accessEvent.setRemoteAddress(remoteAddress);
        Assert.assertEquals(remoteAddress, accessEvent.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type", "application/json");
        accessEvent.setRequestHeaders(requestHeaders);
        Assert.assertEquals(requestHeaders, accessEvent.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        accessEvent.setResponseHeaders(responseHeaders);
        Assert.assertEquals(responseHeaders, accessEvent.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent accessEvent = new AccessEvent();
        Map<String, String[]> requestParameters = new HashMap<>();
        String[] values = {"value1", "value2"};
        requestParameters.put("param", values);
        accessEvent.setRequestParameters(requestParameters);
        Assert.assertEquals(requestParameters, accessEvent.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent accessEvent = new AccessEvent();
        int localPort = 8080;
        accessEvent.setLocalPort(localPort);
        Assert.assertEquals(localPort, accessEvent.getLocalPort());
    }

    @Test
    public void testGetStatusCode() {
        AccessEvent accessEvent = new AccessEvent();
        int statusCode = 200;
        accessEvent.setStatusCode(statusCode);
        Assert.assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(123456789L);
        accessEvent1.setLoggerContext(new LoggerContext());
        accessEvent1.setRequestURI("/example");
        accessEvent1.setRequestURL("http://example.com");
        accessEvent1.setRemoteHost("localhost");
        accessEvent1.setRemoteUser("user");
        accessEvent1.setProtocol("HTTP/1.1");
        accessEvent1.setMethod("GET");
        accessEvent1.setServerName("example.com");
        accessEvent1.setRemoteAddress("127.0.0.1");
        accessEvent1.setLocalPort(8080);
        accessEvent1.setStatusCode(200);

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(123456789L);
        accessEvent2.setLoggerContext(new LoggerContext());
        accessEvent2.setRequestURI("/example");
        accessEvent2.setRequestURL("http://example.com");
        accessEvent2.setRemoteHost("localhost");
        accessEvent2.setRemoteUser("user");
        accessEvent2.setProtocol("HTTP/1.1");
        accessEvent2.setMethod("GET");
        accessEvent2.setServerName("example.com");
        accessEvent2.setRemoteAddress("127.0.0.1");
        accessEvent2.setLocalPort(8080);
        accessEvent2.setStatusCode(200);

        Assert.assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(123456789L);
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setRequestURI("/example");
        accessEvent.setRequestURL("http://example.com");
        accessEvent.setRemoteHost("localhost");
        accessEvent.setRemoteUser("user");
        accessEvent.setProtocol("HTTP/1.1");
        accessEvent.setMethod("GET");
        accessEvent.setServerName("example.com");
        accessEvent.setRemoteAddress("127.0.0.1");
        accessEvent.setLocalPort(8080);
        accessEvent.setStatusCode(200);

        Assert.assertEquals(accessEvent.hashCode(), accessEvent.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(123456789L);
        accessEvent.setLoggerContext(new LoggerContext());
        Assert.assertEquals("AccessEvent[loggerContext=LoggerContext, timeStamp=123456789]", accessEvent.toString());
    }
}