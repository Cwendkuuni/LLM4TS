package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(8, 30);
        assertNotNull(time);
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertNotNull(time);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(time1));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(Integer.valueOf(9), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(Integer.valueOf(15), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(7, 50);
        assertEquals("7:50", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        TimeOfDay time3 = new TimeOfDay(9, 0);
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 20);
        TimeOfDay time2 = new TimeOfDay(8, 20);
        TimeOfDay time3 = new TimeOfDay(9, 30);
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("8:20"));
    }
}