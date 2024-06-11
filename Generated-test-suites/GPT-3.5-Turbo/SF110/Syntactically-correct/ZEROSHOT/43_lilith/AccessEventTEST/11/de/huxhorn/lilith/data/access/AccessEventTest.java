package de.huxhorn.lilith.data.access;

import org.junit.*;
import static org.junit.Assert.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(123456789L);
        assertEquals(123456789L, event.getTimeStamp().longValue());
    }

    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(123456789L);
        assertEquals(123456789L, event.getTimeStamp().longValue());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        assertNotNull(event.getLoggerContext());
    }

    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(new LoggerContext());
        assertNotNull(event.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/test");
        assertEquals("/test", event.getRequestURI());
    }

    @Test
    public void testSetRequestURI() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/test");
        assertEquals("/test", event.getRequestURI());
    }

    @Test
    public void testGetRequestURL() {
        AccessEvent event = new AccessEvent();
        event.setRequestURL("http://localhost:8080/test");
        assertEquals("http://localhost:8080/test", event.getRequestURL());
    }

    @Test
    public void testSetRequestURL() {
        AccessEvent event = new AccessEvent();
        event.setRequestURL("http://localhost:8080/test");
        assertEquals("http://localhost:8080/test", event.getRequestURL());
    }

    @Test
    public void testGetRemoteHost() {
        AccessEvent event = new AccessEvent();
        event.setRemoteHost("localhost");
        assertEquals("localhost", event.getRemoteHost());
    }

    @Test
    public void testSetRemoteHost() {
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
    public void testSetRemoteUser() {
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
    public void testSetProtocol() {
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
    public void testSetMethod() {
        AccessEvent event = new AccessEvent();
        event.setMethod("GET");
        assertEquals("GET", event.getMethod());
    }

    @Test
    public void testGetServerName() {
        AccessEvent event = new AccessEvent();
        event.setServerName("localhost");
        assertEquals("localhost", event.getServerName());
    }

    @Test
    public void testSetServerName() {
        AccessEvent event = new AccessEvent();
        event.setServerName("localhost");
        assertEquals("localhost", event.getServerName());
    }

    @Test
    public void testGetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());
    }

    @Test
    public void testSetRemoteAddress() {
        AccessEvent event = new AccessEvent();
        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());
    }

    @Test
    public void testGetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(new HashMap<String, String>());
        assertNotNull(event.getRequestHeaders());
    }

    @Test
    public void testSetRequestHeaders() {
        AccessEvent event = new AccessEvent();
        event.setRequestHeaders(new HashMap<String, String>());
        assertNotNull(event.getRequestHeaders());
    }

    @Test
    public void testGetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(new HashMap<String, String>());
        assertNotNull(event.getResponseHeaders());
    }

    @Test
    public void testSetResponseHeaders() {
        AccessEvent event = new AccessEvent();
        event.setResponseHeaders(new HashMap<String, String>());
        assertNotNull(event.getResponseHeaders());
    }

    @Test
    public void testGetRequestParameters() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(new HashMap<String, String[]>());
        assertNotNull(event.getRequestParameters());
    }

    @Test
    public void testSetRequestParameters() {
        AccessEvent event = new AccessEvent();
        event.setRequestParameters(new HashMap<String, String[]>());
        assertNotNull(event.getRequestParameters());
    }

    @Test
    public void testGetLocalPort() {
        AccessEvent event = new AccessEvent();
        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());
    }

    @Test
    public void testSetLocalPort() {
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
    public void testSetStatusCode() {
        AccessEvent event = new AccessEvent();
        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURI("/test1");
        event1.setMethod("GET");

        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/test1");
        event2.setMethod("GET");

        AccessEvent event3 = new AccessEvent();
        event3.setRequestURI("/test2");
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
        LoggerContext loggerContext = new LoggerContext();
        AccessEvent event = new AccessEvent();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(123456789L);

        assertEquals("AccessEvent[loggerContext=de.huxhorn.lilith.data.access.LoggerContext@<hashCode>, timeStamp=123456789]", event.toString());
    }
}