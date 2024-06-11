package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructor() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 15);

        assertEquals(0, time1.compareTo(time2));
        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
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

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }

    @Test
    public void testConstructorWithEmptyArguments() {
        TimeOfDay time = new TimeOfDay(0, 0);
        assertEquals(new Integer(0), time.getHours());
        assertEquals(new Integer(0), time.getMinutes());

        time = new TimeOfDay(23, 59);
        assertEquals(new Integer(23), time.getHours());
        assertEquals(new Integer(59), time.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testConstructorWithStringInvalidFormat() {
        TimeOfDay time = new TimeOfDay("12:63");
    }

    @Test(expected = NumberFormatException.class)
    public void testConstructorWithStringInvalidFormat2() {
        TimeOfDay time = new TimeOfDay("abc:45");
    }
}