package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);

        int result = time1.compareTo(time2);

        assertTrue(result < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 30);

        Integer hours = time.getHours();

        assertEquals(Integer.valueOf(8), hours);
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 30);

        Integer minutes = time.getMinutes();

        assertEquals(Integer.valueOf(30), minutes);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 30);

        String timeToString = time.toString();

        assertEquals("8:30", timeToString);
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(8, 30);

        int hashCode = time.hashCode();

        assertEquals(830, hashCode);
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);
        TimeOfDay time3 = new TimeOfDay(9, 30);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}