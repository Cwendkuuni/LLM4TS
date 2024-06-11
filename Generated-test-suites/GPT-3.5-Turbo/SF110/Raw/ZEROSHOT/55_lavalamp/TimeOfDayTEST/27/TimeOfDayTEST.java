package net.sf.lavalamp.device;

import org.junit.Before;
import org.junit.Test;
import java.util.Objects;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay time1;
    private TimeOfDay time2;
    private TimeOfDay time3;

    @Before
    public void setUp() {
        time1 = new TimeOfDay(9, 30);
        time2 = new TimeOfDay("10:45");
        time3 = new TimeOfDay("12:00");
    }

    @Test
    public void testGetHours() {
        assertEquals(Integer.valueOf(9), time1.getHours());
        assertEquals(Integer.valueOf(10), time2.getHours());
        assertEquals(Integer.valueOf(12), time3.getHours());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(Integer.valueOf(30), time1.getMinutes());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
        assertEquals(Integer.valueOf(0), time3.getMinutes());
    }

    @Test
    public void testToString() {
        assertEquals("9:30", time1.toString());
        assertEquals("10:45", time2.toString());
        assertEquals("12:0", time3.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(2810, time1.hashCode());
        assertEquals(1045, time2.hashCode());
        assertEquals(1200, time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay sameTime1 = new TimeOfDay(9, 30);
        TimeOfDay sameTime2 = new TimeOfDay("10:40");
        TimeOfDay differentTime = new TimeOfDay("11:15");

        assertTrue(time1.equals(sameTime1));
        assertTrue(sameTime1.equals(time1));
        assertFalse(time1.equals(sameTime2));
        assertFalse(time1.equals(differentTime));

        assertTrue(time2.equals(sameTime2));
        assertTrue(sameTime2.equals(time2));
        assertFalse(time2.equals(sameTime1));
        assertFalse(time2.equals(differentTime));

        assertTrue(time3.equals(differentTime));
        assertTrue(differentTime.equals(time3));
        assertFalse(time3.equals(sameTime1));
        assertFalse(time3.equals(sameTime2));
    }

    @Test
    public void testCompareTo() {
        TimeOfDay earlierTime = new TimeOfDay(9, 15);
        TimeOfDay laterTime = new TimeOfDay("10:0");

        assertTrue(time1.compareTo(earlierTime) > 0);
        assertTrue(time1.compareTo(laterTime) < 0);
        assertTrue(time1.compareTo(time1) == 0);

        assertTrue(time2.compareTo(earlierTime) > 0);
        assertTrue(time2.compareTo(laterTime) < 0);
        assertTrue(time2.compareTo(time2) == 0);

        assertTrue(time3.compareTo(earlierTime) > 0);
        assertTrue(time3.compareTo(laterTime) < 0);
        assertTrue(time3.compareTo(time3) == 0);
    }
}