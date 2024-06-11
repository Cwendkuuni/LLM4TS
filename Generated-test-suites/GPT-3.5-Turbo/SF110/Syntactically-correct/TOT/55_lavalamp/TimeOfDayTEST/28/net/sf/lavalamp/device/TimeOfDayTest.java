import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(9, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithInvalidString() {
        try {
            TimeOfDay time = new TimeOfDay("abc");
            fail("Expected NumberFormatException was not thrown.");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time1) == 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals("9:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);
        TimeOfDay time4 = new TimeOfDay(11, 30);
        assertTrue(time1.equals(time2));
        assertTrue(time2.equals(time1));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(time4));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }
}