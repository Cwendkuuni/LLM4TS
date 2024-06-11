import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TimeOfDayTest {

    @Test
    public void testConstructor() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("Constructor with hours and minutes failed", 10, time.getHours().intValue());
        assertEquals("Constructor with hours and minutes failed", 30, time.getMinutes().intValue());

        time = new TimeOfDay("09:45");
        assertEquals("Constructor with string failed", 9, time.getHours().intValue());
        assertEquals("Constructor with string failed", 45, time.getMinutes().intValue());

        time = new TimeOfDay("00:00");
        assertEquals("Constructor with string failed", 0, time.getHours().intValue());
        assertEquals("Constructor with string failed", 0, time.getMinutes().intValue());

        time = new TimeOfDay("23:59");
        assertEquals("Constructor with string failed", 23, time.getHours().intValue());
        assertEquals("Constructor with string failed", 59, time.getMinutes().intValue());

        // Edge case: empty string
        try {
            time = new TimeOfDay("");
        } catch (NumberFormatException e) {
            assertTrue("Constructor with empty string failed to throw NumberFormatException", true);
        }
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals("compareTo failed for equal times", 0, time1.compareTo(time2));

        time2 = new TimeOfDay(10, 30);
        assertEquals("compareTo failed for different hours", 1, time1.compareTo(time2));

        time2 = new TimeOfDay(12, 45);
        assertEquals("compareTo failed for different minutes", -1, time1.compareTo(time2));

        // Edge case: null object
        try {
            time1.compareTo(null);
        } catch (NullPointerException e) {
            assertTrue("compareTo failed to throw NullPointerException", true);
        }
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 45);
        assertEquals("toString failed", "9:45", time.toString());

        time = new TimeOfDay(12, 0);
        assertEquals("toString failed for single digit minute", "12:0", time.toString());

        time = new TimeOfDay(0, 0);
        assertEquals("toString failed for 0 hours and 0 minutes", "0:0", time.toString());

        time = new TimeOfDay(23, 59);
        assertEquals("toString failed for 23 hours and 59 minutes", "23:59", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("hashCode failed", 1030, time.hashCode());

        time = new TimeOfDay(0, 0);
        assertEquals("hashCode failed for 0 hours and 0 minutes", 0, time.hashCode());

        time = new TimeOfDay(23, 59);
        assertEquals("hashCode failed for 23 hours and 59 minutes", 2359, time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue("equals failed for equal times", time1.equals(time2));

        time2 = new TimeOfDay(12, 30);
        assertFalse("equals failed for different hours", time1.equals(time2));

        time2 = new TimeOfDay(10, 45);
        assertFalse("equals failed for different minutes", time1.equals(time2));

        time2 = new TimeOfDay(10, 30);
        assertFalse("equals failed for different object type", time1.equals("10:30"));
    }

}