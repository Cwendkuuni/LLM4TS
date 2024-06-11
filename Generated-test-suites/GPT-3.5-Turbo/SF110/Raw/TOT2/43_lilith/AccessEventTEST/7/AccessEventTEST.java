import static org.junit.Assert.*;
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
        assertEquals(loggerContext, event.getLoggerContext());
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();
        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/test");
        event1.setRequestURL("/test");
        event1.setRemoteHost("localhost");
        event1.setRemoteUser("admin");
        event1.setProtocol("http");
        event1.setMethod("GET");
        event1.setServerName("localhost");
        event1.setRemoteAddress("127.0.0.1");
        event1.setLocalPort(8080);
        event1.setStatusCode(200);

        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/test");
        event2.setRequestURL("/test");
        event2.setRemoteHost("localhost");
        event2.setRemoteUser("admin");
        event2.setProtocol("http");
        event2.setMethod("GET");
        event2.setServerName("localhost");
        event2.setRemoteAddress("127.0.0.1");
        event2.setLocalPort(8080);
        event2.setStatusCode(200);

        assertEquals(true, event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(123456789L);
        assertEquals(123456789L, event.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        event.setTimeStamp(123456789L);
        assertEquals("AccessEvent[loggerContext=" + loggerContext + ", timeStamp=123456789]", event.toString());
    }
}