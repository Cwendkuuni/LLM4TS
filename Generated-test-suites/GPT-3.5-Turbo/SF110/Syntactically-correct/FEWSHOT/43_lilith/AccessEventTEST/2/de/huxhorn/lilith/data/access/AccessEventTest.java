package de.huxhorn.lilith.data.access;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AccessEventTest {

  @Test
  public void testGetTimeStamp() {
    AccessEvent event = new AccessEvent();
    event.setTimeStamp(123456789L);
    assertEquals(123456789L, event.getTimeStamp().longValue());
  }

  @Test
  public void testGetLoggerContext() {
    AccessEvent event = new AccessEvent();
    LoggerContext loggerContext = new LoggerContext();
    event.setLoggerContext(loggerContext);
    assertSame(loggerContext, event.getLoggerContext());
  }

  @Test
  public void testGetRequestURI() {
    AccessEvent event = new AccessEvent();
    event.setRequestURI("/test");
    assertEquals("/test", event.getRequestURI());
  }

  @Test
  public void testGetRequestURL() {
    AccessEvent event = new AccessEvent();
    event.setRequestURL("http://example.com/test");
    assertEquals("http://example.com/test", event.getRequestURL());
  }

  @Test
  public void testGetRemoteHost() {
    AccessEvent event = new AccessEvent();
    event.setRemoteHost("localhost");
    assertEquals("localhost", event.getRemoteHost());
  }

  @Test
  public void testGetRemoteUser() {
    AccessEvent event = new AccessEvent();
    event.setRemoteUser("user");
    assertEquals("user", event.getRemoteUser());
  }

  @Test
  public void testGetProtocol() {
    AccessEvent event = new AccessEvent();
    event.setProtocol("HTTP/1.1");
    assertEquals("HTTP/1.1", event.getProtocol());
  }

  @Test
  public void testGetMethod() {
    AccessEvent event = new AccessEvent();
    event.setMethod("GET");
    assertEquals("GET", event.getMethod());
  }

  @Test
  public void testGetServerName() {
    AccessEvent event = new AccessEvent();
    event.setServerName("example.com");
    assertEquals("example.com", event.getServerName());
  }

  @Test
  public void testGetRemoteAddress() {
    AccessEvent event = new AccessEvent();
    event.setRemoteAddress("127.0.0.1");
    assertEquals("127.0.0.1", event.getRemoteAddress());
  }

  @Test
  public void testGetRequestHeaders() {
    AccessEvent event = new AccessEvent();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    event.setRequestHeaders(headers);
    assertSame(headers, event.getRequestHeaders());
  }

  @Test
  public void testGetResponseHeaders() {
    AccessEvent event = new AccessEvent();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    event.setResponseHeaders(headers);
    assertSame(headers, event.getResponseHeaders());
  }

  @Test
  public void testGetRequestParameters() {
    AccessEvent event = new AccessEvent();
    Map<String, String[]> parameters = new HashMap<>();
    parameters.put("param1", new String[] { "value1" });
    event.setRequestParameters(parameters);
    assertSame(parameters, event.getRequestParameters());
  }

  @Test
  public void testGetLocalPort() {
    AccessEvent event = new AccessEvent();
    event.setLocalPort(8080);
    assertEquals(8080, event.getLocalPort());
  }

  @Test
  public void testGetStatusCode() {
    AccessEvent event = new AccessEvent();
    event.setStatusCode(200);
    assertEquals(200, event.getStatusCode());
  }

  @Test
  public void testEquals() {
    AccessEvent event1 = new AccessEvent();
    event1.setRequestURI("/test");
    event1.setMethod("GET");

    AccessEvent event2 = new AccessEvent();
    event2.setRequestURI("/test");
    event2.setMethod("GET");

    AccessEvent event3 = new AccessEvent();
    event3.setRequestURI("/test");
    event3.setMethod("POST");

    assertTrue(event1.equals(event2));
    assertFalse(event1.equals(event3));
  }

  @Test
  public void testHashCode() {
    AccessEvent event1 = new AccessEvent();
    event1.setRequestURI("/test");
    event1.setMethod("GET");

    AccessEvent event2 = new AccessEvent();
    event2.setRequestURI("/test");
    event2.setMethod("GET");

    assertEquals(event1.hashCode(), event2.hashCode());
  }

  @Test
  public void testToString() {
    AccessEvent event = new AccessEvent();
    event.setRequestURI("/test");
    event.setTimeStamp(123456789L);

    assertEquals("AccessEvent[loggerContext=null, timeStamp=123456789]", event.toString());
  }
}