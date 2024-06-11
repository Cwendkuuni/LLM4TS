import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.HashMap;

public class AccessEventTest {
  
  @Test
  public void testGetTimeStamp() {
    AccessEvent event = new AccessEvent();
    Long timeStamp = 10L;
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
    String requestURI = "/test";
    event.setRequestURI(requestURI);
    assertEquals(requestURI, event.getRequestURI());
  }
  
  @Test
  public void testGetRequestURL() {
    AccessEvent event = new AccessEvent();
    String requestURL = "https://example.com/test";
    event.setRequestURL(requestURL);
    assertEquals(requestURL, event.getRequestURL());
  }
  
  @Test
  public void testGetRemoteHost() {
    AccessEvent event = new AccessEvent();
    String remoteHost = "example.com";
    event.setRemoteHost(remoteHost);
    assertEquals(remoteHost, event.getRemoteHost());
  }
  
  @Test
  public void testGetRemoteUser() {
    AccessEvent event = new AccessEvent();
    String remoteUser = "user";
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
    requestParameters.put("param1", new String[]{"value1", "value2"});
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
    AccessEvent event2 = new AccessEvent();
    
    // Testing equal objects
    assertEquals(event1, event2);
    
    // Modifying one property
    event2.setRequestURI("/test");
    assertNotEquals(event1, event2);
    
    // Modifying multiple properties
    event2.setRequestURI("/test");
    event2.setProtocol("HTTP/1.1");
    assertNotEquals(event1, event2);
  }
  
  @Test
  public void testHashCode() {
    AccessEvent event1 = new AccessEvent();
    AccessEvent event2 = new AccessEvent();
    
    // Testing equal objects
    assertEquals(event1.hashCode(), event2.hashCode());
    
    // Modifying one property
    event2.setRequestURI("/test");
    assertNotEquals(event1.hashCode(), event2.hashCode());
    
    // Modifying multiple properties
    event2.setRequestURI("/test");
    event2.setProtocol("HTTP/1.1");
    assertNotEquals(event1.hashCode(), event2.hashCode());
  }
  
  @Test
  public void testToString() {
    AccessEvent event = new AccessEvent();
    LoggerContext loggerContext = new LoggerContext();
    event.setLoggerContext(loggerContext);
    event.setTimeStamp(10L);
    
    String expected = "AccessEvent[loggerContext=" + loggerContext.toString() + ", timeStamp=10]";
    assertEquals(expected, event.toString());
  }
}