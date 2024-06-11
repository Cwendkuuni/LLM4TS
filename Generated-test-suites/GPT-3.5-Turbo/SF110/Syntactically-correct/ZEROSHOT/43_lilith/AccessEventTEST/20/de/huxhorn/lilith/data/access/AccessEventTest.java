import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {

  @Test
  public void testGetSetTimeStamp() {
    AccessEvent accessEvent = new AccessEvent();

    Long timeStamp = new Long(123456789);
    accessEvent.setTimeStamp(timeStamp);

    assertEquals(timeStamp, accessEvent.getTimeStamp());
  }

  @Test
  public void testGetSetLoggerContext() {
    AccessEvent accessEvent = new AccessEvent();

    LoggerContext loggerContext = new LoggerContext();
    accessEvent.setLoggerContext(loggerContext);

    assertEquals(loggerContext, accessEvent.getLoggerContext());
  }

  @Test
  public void testGetSetRequestURI() {
    AccessEvent accessEvent = new AccessEvent();

    String requestURI = "http://example.com";
    accessEvent.setRequestURI(requestURI);

    assertEquals(requestURI, accessEvent.getRequestURI());
  }

  @Test
  public void testGetSetRequestURL() {
    AccessEvent accessEvent = new AccessEvent();

    String requestURL = "http://example.com";
    accessEvent.setRequestURL(requestURL);

    assertEquals(requestURL, accessEvent.getRequestURL());
  }

  @Test
  public void testGetSetRemoteHost() {
    AccessEvent accessEvent = new AccessEvent();

    String remoteHost = "localhost";
    accessEvent.setRemoteHost(remoteHost);

    assertEquals(remoteHost, accessEvent.getRemoteHost());
  }

  @Test
  public void testGetSetRemoteUser() {
    AccessEvent accessEvent = new AccessEvent();

    String remoteUser = "user";
    accessEvent.setRemoteUser(remoteUser);

    assertEquals(remoteUser, accessEvent.getRemoteUser());
  }

  @Test
  public void testGetSetProtocol() {
    AccessEvent accessEvent = new AccessEvent();

    String protocol = "HTTP/1.1";
    accessEvent.setProtocol(protocol);

    assertEquals(protocol, accessEvent.getProtocol());
  }

  @Test
  public void testGetSetMethod() {
    AccessEvent accessEvent = new AccessEvent();

    String method = "GET";
    accessEvent.setMethod(method);

    assertEquals(method, accessEvent.getMethod());
  }

  @Test
  public void testGetSetServerName() {
    AccessEvent accessEvent = new AccessEvent();

    String serverName = "localhost";
    accessEvent.setServerName(serverName);

    assertEquals(serverName, accessEvent.getServerName());
  }

  @Test
  public void testGetSetRemoteAddress() {
    AccessEvent accessEvent = new AccessEvent();

    String remoteAddress = "127.0.0.1";
    accessEvent.setRemoteAddress(remoteAddress);

    assertEquals(remoteAddress, accessEvent.getRemoteAddress());
  }

  @Test
  public void testGetSetRequestHeaders() {
    AccessEvent accessEvent = new AccessEvent();

    Map<String, String> requestHeaders = new HashMap<>();
    requestHeaders.put("Content-Type", "application/json");
    accessEvent.setRequestHeaders(requestHeaders);

    assertEquals(requestHeaders, accessEvent.getRequestHeaders());
  }

  @Test
  public void testGetSetResponseHeaders() {
    AccessEvent accessEvent = new AccessEvent();

    Map<String, String> responseHeaders = new HashMap<>();
    responseHeaders.put("Content-Type", "application/json");
    accessEvent.setResponseHeaders(responseHeaders);

    assertEquals(responseHeaders, accessEvent.getResponseHeaders());
  }

  @Test
  public void testGetSetRequestParameters() {
    AccessEvent accessEvent = new AccessEvent();

    Map<String, String[]> requestParameters = new HashMap<>();
    requestParameters.put("param", new String[] { "value" });
    accessEvent.setRequestParameters(requestParameters);

    assertEquals(requestParameters, accessEvent.getRequestParameters());
  }

  @Test
  public void testGetSetLocalPort() {
    AccessEvent accessEvent = new AccessEvent();

    int localPort = 8080;
    accessEvent.setLocalPort(localPort);

    assertEquals(localPort, accessEvent.getLocalPort());
  }

  @Test
  public void testGetSetStatusCode() {
    AccessEvent accessEvent = new AccessEvent();

    int statusCode = 200;
    accessEvent.setStatusCode(statusCode);

    assertEquals(statusCode, accessEvent.getStatusCode());
  }

  @Test
  public void testEquals() {
    AccessEvent event1 = new AccessEvent();
    event1.setLocalPort(8080);

    AccessEvent event2 = new AccessEvent();
    event2.setLocalPort(8080);

    assertEquals(event1, event2);
  }

  @Test
  public void testHashCode() {
    AccessEvent event = new AccessEvent();
    event.setLocalPort(8080);
    event.setStatusCode(200);

    int hashCode = ((8080 * 31) + 200);

    assertEquals(hashCode, event.hashCode());
  }

  @Test
  public void testToString() {
    AccessEvent accessEvent = new AccessEvent();
    accessEvent.setLoggerContext(new LoggerContext());
    accessEvent.setTimeStamp(new Long(123456789));

    String expected = "AccessEvent[loggerContext=" + accessEvent.getLoggerContext() + ", timeStamp=" + accessEvent.getTimeStamp() + "]";

    assertEquals(expected, accessEvent.toString());
  }
}