package de.huxhorn.lilith.data.access;

import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(loggerContext, event.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/example");
        assertEquals("/example", event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        event.setRequestURL("https://example.com");
        assertEquals("https://example.com", event.getRequestURL());
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
        event.setRemoteUser("admin");
        assertEquals("admin", event.getRemoteUser());
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
        event1.setRequestURL("https://example.com");
        event1.setMethod("GET");

        AccessEvent event2 = new AccessEvent();
        event2.setRequestURL("https://example.com");
        event2.setMethod("GET");

        AccessEvent event3 = new AccessEvent();
        event3.setRequestURL("https://example.org");
        event3.setMethod("POST");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURL("https://example.com");
        event1.setMethod("GET");

        AccessEvent event2 = new AccessEvent();
        event2.setRequestURL("https://example.com");
        event2.setMethod("GET");

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(123456789L);

        String expected = "AccessEvent[loggerContext=" + event.getLoggerContext() + ", timeStamp=" + event.getTimeStamp() + "]";
        assertEquals(expected, event.toString());
    }
}