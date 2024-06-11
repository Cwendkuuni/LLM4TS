package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        assertEquals(Integer.valueOf(5), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(Integer.valueOf(12), time2.getHours());
        assertEquals(Integer.valueOf(0), time2.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time1 = new TimeOfDay("05:30");
        assertEquals(Integer.valueOf(5), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay("12:00");
        assertEquals(Integer.valueOf(12), time2.getHours());
        assertEquals(Integer.valueOf(0), time2.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        TimeOfDay time3 = new TimeOfDay(5, 30);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(time3));
    }

    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        assertEquals("5:30", time1.toString());

        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertEquals("10:15", time2.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        TimeOfDay time2 = new TimeOfDay(5, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        assertEquals(time1.hashCode(), time2.hashCode());
        assertFalse(time1.hashCode() == time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        TimeOfDay time2 = new TimeOfDay(5, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals("5:30"));
        assertFalse(time1.equals(null));
    }
}