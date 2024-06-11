package net.sf.lavalamp.device;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructor1() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructor2() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(0, time1.compareTo(time2));

        TimeOfDay time3 = new TimeOfDay(10, 45);
        assertEquals(1, time1.compareTo(time3));
        assertEquals(-1, time3.compareTo(time1));

        TimeOfDay time4 = new TimeOfDay(12, 45);
        assertEquals(-1, time1.compareTo(time4));
        assertEquals(1, time4.compareTo(time1));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertEquals(time1.hashCode(), time2.hashCode());

        TimeOfDay time3 = new TimeOfDay(10, 45);
        assertNotEquals(time1.hashCode(), time3.hashCode());

        TimeOfDay time4 = new TimeOfDay(12, 45);
        assertNotEquals(time1.hashCode(), time4.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(10, 45);
        assertFalse(time1.equals(time3));

        TimeOfDay time4 = new TimeOfDay(12, 45);
        assertFalse(time1.equals(time4));
    }
}