package de.huxhorn.lilith.data.access;
import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {
    
    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 12345L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }
    
    @Test
    public void testGetLoggerContext() {
        AccessEvent accessEvent = new AccessEvent();
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }
    
    // Other methods tests will be added here...
}