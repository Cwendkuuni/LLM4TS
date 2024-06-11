import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {
  
  private AccessEvent accessEvent;
  
  @Test
  public void testGetTimeStamp() {
    accessEvent = new AccessEvent();
    accessEvent.setTimeStamp(1586258081000L);
    assertEquals(1586258081000L, accessEvent.getTimeStamp().longValue());
  }
  
  @Test
  public void testSetTimeStamp() {
    accessEvent = new AccessEvent();
    accessEvent.setTimeStamp(1586258081000L);
    assertEquals(1586258081000L, accessEvent.getTimeStamp().longValue());
  }
  
  @Test
  public void testGetLoggerContext() {
    accessEvent = new AccessEvent();
    LoggerContext loggerContext = new LoggerContext();
    accessEvent.setLoggerContext(loggerContext);
    assertEquals(loggerContext, accessEvent.getLoggerContext());
  }
  
  @Test
  public void testSetLoggerContext() {
    accessEvent = new AccessEvent();
    LoggerContext loggerContext = new LoggerContext();
    accessEvent.setLoggerContext(loggerContext);
    assertEquals(loggerContext, accessEvent.getLoggerContext());
  }
  
  @Test
  public void testGetRequestURI() {
    accessEvent = new AccessEvent();
    accessEvent.setRequestURI("/index.html");
    assertEquals("/index.html", accessEvent.getRequestURI());
  }
  
  @Test
  public void testSetRequestURI() {
    accessEvent = new AccessEvent();
    accessEvent.setRequestURI("/index.html");
    assertEquals("/index.html", accessEvent.getRequestURI());
  }
  
  @Test
  public void testGetRequestURL() {
    accessEvent = new AccessEvent();
    accessEvent.setRequestURL("http://example.com/index.html");
    assertEquals("http://example.com/index.html", accessEvent.getRequestURL());
  }
  
  @Test
  public void testSetRequestURL() {
    accessEvent = new AccessEvent();
    accessEvent.setRequestURL("http://example.com/index.html");
    assertEquals("http://example.com/index.html", accessEvent.getRequestURL());
  }
  
  @Test
  public void testGetRemoteHost() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteHost("example.com");
    assertEquals("example.com", accessEvent.getRemoteHost());
  }
  
  @Test
  public void testSetRemoteHost() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteHost("example.com");
    assertEquals("example.com", accessEvent.getRemoteHost());
  }
  
  @Test
  public void testGetRemoteUser() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteUser("user@example.com");
    assertEquals("user@example.com", accessEvent.getRemoteUser());
  }
  
  @Test
  public void testSetRemoteUser() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteUser("user@example.com");
    assertEquals("user@example.com", accessEvent.getRemoteUser());
  }
  
  @Test
  public void testGetProtocol() {
    accessEvent = new AccessEvent();
    accessEvent.setProtocol("HTTP/1.1");
    assertEquals("HTTP/1.1", accessEvent.getProtocol());
  }
  
  @Test
  public void testSetProtocol() {
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
  public void testSetMethod() {
    accessEvent = new AccessEvent();
    accessEvent.setMethod("GET");
    assertEquals("GET", accessEvent.getMethod());
  }
  
  @Test
  public void testGetServerName() {
    accessEvent = new AccessEvent();
    accessEvent.setServerName("example.com");
    assertEquals("example.com", accessEvent.getServerName());
  }
  
  @Test
  public void testSetServerName() {
    accessEvent = new AccessEvent();
    accessEvent.setServerName("example.com");
    assertEquals("example.com", accessEvent.getServerName());
  }
  
  @Test
  public void testGetRemoteAddress() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteAddress("192.168.0.1");
    assertEquals("192.168.0.1", accessEvent.getRemoteAddress());
  }
  
  @Test
  public void testSetRemoteAddress() {
    accessEvent = new AccessEvent();
    accessEvent.setRemoteAddress("192.168.0.1");
    assertEquals("192.168.0.1", accessEvent.getRemoteAddress());
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
  public void testSetRequestHeaders() {
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
  public void testSetResponseHeaders() {
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
    parameters.put("id", new String[]{"1", "2"});
    accessEvent.setRequestParameters(parameters);
    assertEquals(parameters, accessEvent.getRequestParameters());
  }
  
  @Test
  public void testSetRequestParameters() {
    accessEvent = new AccessEvent();
    Map<String, String[]> parameters = new HashMap<>();
    parameters.put("id", new String[]{"1", "2"});
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
  public void testSetLocalPort() {
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
  
  @Test
  public void testSetStatusCode() {
    accessEvent = new AccessEvent();
    accessEvent.setStatusCode(200);
    assertEquals(200, accessEvent.getStatusCode());
  }
  
  @Test
  public void testEquals() {
    accessEvent = new AccessEvent();
    accessEvent.setTimeStamp(1586258081000L);
    
    AccessEvent accessEvent2 = new AccessEvent();
    accessEvent2.setTimeStamp(1586258081000L);
    
    assertTrue(accessEvent.equals(accessEvent2));
  }
  
  @Test
  public void testHashCode() {
    accessEvent = new AccessEvent();
    accessEvent.setTimeStamp(1586258081000L);
    
    AccessEvent accessEvent2 = new AccessEvent();
    accessEvent2.setTimeStamp(1586258081000L);
    
    assertEquals(accessEvent.hashCode(), accessEvent2.hashCode());
  }
  
  @Test
  public void testToString() {
    accessEvent = new AccessEvent();
    accessEvent.setLoggerContext(new LoggerContext());
    accessEvent.setTimeStamp(1586258081000L);
    
    String expected = "AccessEvent[loggerContext=LoggerContext@1234, timeStamp=1586258081000]";
    assertEquals(expected, accessEvent.toString());
  }
}