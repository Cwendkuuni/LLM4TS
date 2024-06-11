import org.junit.*;
import static org.junit.Assert.*;

public class AccessEventTest {

  private AccessEvent accessEvent;

  @Before
  public void setUp() {
    accessEvent = new AccessEvent();
  }

  @Test
  public void testGetTimeStamp() {
    Long timeStamp = 1234567890L;
    accessEvent.setTimeStamp(timeStamp);
    Long result = accessEvent.getTimeStamp();
    assertEquals(timeStamp, result);
  }

  @Test
  public void testGetLoggerContext() {
    LoggerContext loggerContext = new LoggerContext();
    accessEvent.setLoggerContext(loggerContext);
    LoggerContext result = accessEvent.getLoggerContext();
    assertEquals(loggerContext, result);
  }

  @Test
  public void testGetRequestURI() {
    String requestURI = "/example";
    accessEvent.setRequestURI(requestURI);
    String result = accessEvent.getRequestURI();
    assertEquals(requestURI, result);
  }

  @Test
  public void testGetRequestURL() {
    String requestURL = "http://example.com";
    accessEvent.setRequestURL(requestURL);
    String result = accessEvent.getRequestURL();
    assertEquals(requestURL, result);
  }

  @Test
  public void testGetRemoteHost() {
    String remoteHost = "localhost";
    accessEvent.setRemoteHost(remoteHost);
    String result = accessEvent.getRemoteHost();
    assertEquals(remoteHost, result);
  }

  @Test
  public void testGetRemoteUser() {
    String remoteUser = "user";
    accessEvent.setRemoteUser(remoteUser);
    String result = accessEvent.getRemoteUser();
    assertEquals(remoteUser, result);
  }

  @Test
  public void testGetProtocol() {
    String protocol = "HTTP/1.1";
    accessEvent.setProtocol(protocol);
    String result = accessEvent.getProtocol();
    assertEquals(protocol, result);
  }

  @Test
  public void testGetMethod() {
    String method = "GET";
    accessEvent.setMethod(method);
    String result = accessEvent.getMethod();
    assertEquals(method, result);
  }

  @Test
  public void testGetServerName() {
    String serverName = "example.com";
    accessEvent.setServerName(serverName);
    String result = accessEvent.getServerName();
    assertEquals(serverName, result);
  }

  @Test
  public void testGetRemoteAddress() {
    String remoteAddress = "127.0.0.1";
    accessEvent.setRemoteAddress(remoteAddress);
    String result = accessEvent.getRemoteAddress();
    assertEquals(remoteAddress, result);
  }

  @Test
  public void testGetRequestHeaders() {
    Map<String, String> requestHeaders = new HashMap<>();
    requestHeaders.put("header1", "value1");
    accessEvent.setRequestHeaders(requestHeaders);
    Map<String, String> result = accessEvent.getRequestHeaders();
    assertEquals(requestHeaders, result);
  }

  @Test
  public void testGetResponseHeaders() {
    Map<String, String> responseHeaders = new HashMap<>();
    responseHeaders.put("header1", "value1");
    accessEvent.setResponseHeaders(responseHeaders);
    Map<String, String> result = accessEvent.getResponseHeaders();
    assertEquals(responseHeaders, result);
  }

  @Test
  public void testGetRequestParameters() {
    Map<String, String[]> requestParameters = new HashMap<>();
    requestParameters.put("param1", new String[]{"value1"});
    accessEvent.setRequestParameters(requestParameters);
    Map<String, String[]> result = accessEvent.getRequestParameters();
    assertEquals(requestParameters, result);
  }

  @Test
  public void testGetLocalPort() {
    int localPort = 8080;
    accessEvent.setLocalPort(localPort);
    int result = accessEvent.getLocalPort();
    assertEquals(localPort, result);
  }

  @Test
  public void testGetStatusCode() {
    int statusCode = 200;
    accessEvent.setStatusCode(statusCode);
    int result = accessEvent.getStatusCode();
    assertEquals(statusCode, result);
  }

  @Test
  public void testEquals() {
    AccessEvent event1 = new AccessEvent();
    AccessEvent event2 = new AccessEvent();
    event1.setTimeStamp(1234567890L);
    event1.setLoggerContext(new LoggerContext());
    event1.setRequestURI("/example");
    event1.setRequestURL("http://example.com");
    event1.setRemoteHost("localhost");
    event1.setRemoteUser("user");
    event1.setProtocol("HTTP/1.1");
    event1.setMethod("GET");
    event1.setServerName("example.com");
    event1.setRemoteAddress("127.0.0.1");
    event1.setRequestHeaders(new HashMap<>());
    event1.setResponseHeaders(new HashMap<>());
    event1.setRequestParameters(new HashMap<>());
    event1.setLocalPort(8080);
    event1.setStatusCode(200);

    event2.setTimeStamp(1234567890L);
    event2.setLoggerContext(new LoggerContext());
    event2.setRequestURI("/example");
    event2.setRequestURL("http://example.com");
    event2.setRemoteHost("localhost");
    event2.setRemoteUser("user");
    event2.setProtocol("HTTP/1.1");
    event2.setMethod("GET");
    event2.setServerName("example.com");
    event2.setRemoteAddress("127.0.0.1");
    event2.setRequestHeaders(new HashMap<>());
    event2.setResponseHeaders(new HashMap<>());
    event2.setRequestParameters(new HashMap<>());
    event2.setLocalPort(8080);
    event2.setStatusCode(200);

    boolean result = event1.equals(event2);
    assertTrue(result);

    event2.setRemoteUser("admin");
    result = event1.equals(event2);
    assertFalse(result);
  }

  @Test
  public void testHashCode() {
    AccessEvent event1 = new AccessEvent();
    event1.setTimeStamp(1234567890L);
    event1.setLoggerContext(new LoggerContext());
    event1.setRequestURI("/example");
    event1.setRequestURL("http://example.com");
    event1.setRemoteHost("localhost");
    event1.setRemoteUser("user");
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
    event2.setRequestURI("/example");
    event2.setRequestURL("http://example.com");
    event2.setRemoteHost("localhost");
    event2.setRemoteUser("user");
    event2.setProtocol("HTTP/1.1");
    event2.setMethod("GET");
    event2.setServerName("example.com");
    event2.setRemoteAddress("127.0.0.1");
    event2.setRequestHeaders(new HashMap<>());
    event2.setResponseHeaders(new HashMap<>());
    event2.setRequestParameters(new HashMap<>());
    event2.setLocalPort(8080);
    event2.setStatusCode(200);

    int hashCode1 = event1.hashCode();
    int hashCode2 = event2.hashCode();
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testToString() {
    Long timeStamp = 1234567890L;
    accessEvent.setTimeStamp(timeStamp);
    LoggerContext loggerContext = new LoggerContext();
    accessEvent.setLoggerContext(loggerContext);
    String expected = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timeStamp + "]";
    String result = accessEvent.toString();
    assertEquals(expected, result);
  }

}