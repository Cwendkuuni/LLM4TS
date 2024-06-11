import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        Long timeStamp = 123456789L;
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
        String requestURI = "/home";
        event.setRequestURI(requestURI);
        assertEquals(requestURI, event.getRequestURI());
    }
    
    // Test cases for other public methods goes here
    
    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        event1.setRequestURI("/test");
        event1.setLocalPort(8080);
        
        AccessEvent event2 = new AccessEvent();
        event2.setRequestURI("/test");
        event2.setLocalPort(8000);
        
        assertFalse(event1.equals(event2));
    }
    
    @Test
    public void testHashCode() {
        AccessEvent event = new AccessEvent();
        event.setRequestURI("/test");
        
        Map<AccessEvent, String> map = new HashMap<>();
        map.put(event, "test");
        
        assertNotNull(map.get(event));
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