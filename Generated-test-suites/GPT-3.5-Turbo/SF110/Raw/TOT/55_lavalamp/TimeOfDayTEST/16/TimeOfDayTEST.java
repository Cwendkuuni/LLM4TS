import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructor1() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testConstructor2() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(9, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);

        assertTrue(time1.compareTo(time1) == 0); // Same time
        assertTrue(time1.compareTo(time2) < 0); // time1 is earlier than time2
        assertTrue(time1.compareTo(time3) > 0); // time1 is later than time3
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertEquals("8:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);

        // Same times should produce same hash code
        assertTrue(time1.hashCode() == time2.hashCode());

        // Different times should produce different hash code
        assertTrue(time1.hashCode() != time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);

        assertTrue(time1.equals(time1)); // Same instance
        assertTrue(time1.equals(time2)); // Same times
        assertFalse(time1.equals(time3)); // Different times
        assertFalse(time1.equals(null)); // Null object
        assertFalse(time1.equals("8:30")); // Different class
    }

}