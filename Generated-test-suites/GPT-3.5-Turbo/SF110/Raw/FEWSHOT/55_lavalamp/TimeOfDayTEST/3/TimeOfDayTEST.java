package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInt() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(Integer.valueOf(1), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("01:30");
        assertEquals(Integer.valueOf(1), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 0);
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time1));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(Integer.valueOf(1), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals("1:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(Integer.valueOf(130), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        TimeOfDay time3 = new TimeOfDay(2, 0);
        assertEquals(true, time1.equals(time2));
        assertEquals(false, time1.equals(time3));
        assertEquals(true, time2.equals(time1));
        assertEquals(false, time2.equals(time3));
        assertEquals(false, time3.equals(time1));
        assertEquals(false, time3.equals(time2));
    }
}