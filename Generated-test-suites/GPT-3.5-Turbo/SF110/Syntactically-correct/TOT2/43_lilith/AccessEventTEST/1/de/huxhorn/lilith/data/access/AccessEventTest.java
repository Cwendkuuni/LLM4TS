package de.huxhorn.lilith.data.access;
import org.junit.*;
import static org.junit.Assert.*;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        event.setTimeStamp(123456789L);
        assertEquals(Long.valueOf(123456789L), event.getTimeStamp());
    }
    
    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();
        LoggerContext context = new LoggerContext();
        event.setLoggerContext(context);
        assertEquals(context, event.getLoggerContext());
    }
    
    // Add test cases for other public methods
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setRemoteHost("localhost");
        event1.setMethod("GET");
        
        AccessEvent event2 = new AccessEvent();
        event2.setRemoteHost("localhost");
        event2.setMethod("GET");
        
        assertTrue(event1.equals(event2));
        
        // You can add more tests for different scenarios
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        event.setRemoteHost("localhost");
        event.setMethod("GET");
        
        int expectedHashCode = 31 * (31 * 0 + "localhost".hashCode()) + "GET".hashCode();
        
        assertEquals(expectedHashCode, event.hashCode());
    }
    
    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        LoggerContext context = new LoggerContext();
        event.setLoggerContext(context);
        event.setTimeStamp(123456789L);
        
        String expectedString = "AccessEvent[loggerContext=" + context + ", timeStamp=123456789]";
        
        assertEquals(expectedString, event.toString());
    }
}