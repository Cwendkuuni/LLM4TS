package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(Integer.valueOf(1), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 30);
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time1));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals(Integer.valueOf(5), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals("5:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        assertEquals(true, time1.equals(time2));
        assertEquals(false, time1.equals(null));
        assertEquals(false, time1.equals("1:30"));
        assertEquals(false, time1.equals(new TimeOfDay(2, 30)));
    }
}