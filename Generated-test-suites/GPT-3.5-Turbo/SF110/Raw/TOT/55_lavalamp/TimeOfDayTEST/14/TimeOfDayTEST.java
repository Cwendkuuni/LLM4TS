
package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotNull(time);
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("15:45");
        assertNotNull(time);
        assertEquals(15, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(9, 0);
        TimeOfDay time2 = new TimeOfDay(9, 0);
        assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testCompareTo_SameHourDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_SameMinutesDifferentHour() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testCompareTo_DifferentHourAndMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(5, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 15);
        assertEquals("8:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 10);
        TimeOfDay time2 = new TimeOfDay(9, 10);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals_SameTime() {
        TimeOfDay time1 = new TimeOfDay(11, 45);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEquals_DifferentTime() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertFalse(time1.equals(time2));
    }
}

