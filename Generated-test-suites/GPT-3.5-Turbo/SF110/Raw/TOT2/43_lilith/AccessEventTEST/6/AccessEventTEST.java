import static org.junit.Assert.*;
import org.junit.Test;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testEquals() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setTimeStamp(123456789L);
        accessEvent2.setTimeStamp(123456789L);
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testHashCode() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(123456789L);
        assertEquals(123456789, accessEvent.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setTimeStamp(123456789L);
        assertEquals("AccessEvent[loggerContext=LoggerContext[], timeStamp=123456789]", accessEvent.toString());
    }
}