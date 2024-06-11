package net.sf.lavalamp.device;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals(Integer.valueOf(5), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithStringParameters() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(5, 30);
        TimeOfDay time2 = new TimeOfDay(6, 15);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(7, 45);
        assertEquals(Integer.valueOf(7), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 20);
        assertEquals(Integer.valueOf(20), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 10);
        assertEquals("10:10", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(11, 45);
        assertEquals(Integer.valueOf(1145).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 30);
        TimeOfDay time2 = new TimeOfDay(8, 30);
        assertEquals(true, time1.equals(time2));
    }
    
}