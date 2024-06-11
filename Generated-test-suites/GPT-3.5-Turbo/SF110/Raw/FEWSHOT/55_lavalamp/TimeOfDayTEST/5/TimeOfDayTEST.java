package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeOfDayTest {
    @Test
    public void testConstructorInt() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals(new Integer(12), time1.getHours());
        assertEquals(new Integer(0), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay(0, 30);
        assertEquals(new Integer(0), time2.getHours());
        assertEquals(new Integer(30), time2.getMinutes());
    }

    @Test
    public void testConstructorString() {
        TimeOfDay time1 = new TimeOfDay("12:00");
        assertEquals(new Integer(12), time1.getHours());
        assertEquals(new Integer(0), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay("00:30");
        assertEquals(new Integer(0), time2.getHours());
        assertEquals(new Integer(30), time2.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(0, time1.compareTo(time2));

        TimeOfDay time3 = new TimeOfDay(12, 0);
        TimeOfDay time4 = new TimeOfDay(13, 0);
        assertEquals(-1, time3.compareTo(time4));

        TimeOfDay time5 = new TimeOfDay(12, 0);
        TimeOfDay time6 = new TimeOfDay(11, 0);
        assertEquals(1, time5.compareTo(time6));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals(new Integer(12), time1.getHours());

        TimeOfDay time2 = new TimeOfDay(0, 30);
        assertEquals(new Integer(0), time2.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals(new Integer(0), time1.getMinutes());

        TimeOfDay time2 = new TimeOfDay(0, 30);
        assertEquals(new Integer(30), time2.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals("12:0", time1.toString());

        TimeOfDay time2 = new TimeOfDay(0, 30);
        assertEquals("0:30", time2.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals(new Integer(1200).hashCode(), time1.hashCode());

        TimeOfDay time2 = new TimeOfDay(0, 30);
        assertEquals(new Integer(30).hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(true, time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(12, 0);
        TimeOfDay time4 = new TimeOfDay(13, 0);
        assertEquals(false, time3.equals(time4));

        TimeOfDay time5 = new TimeOfDay(12, 0);
        TimeOfDay time6 = new TimeOfDay(11, 0);
        assertEquals(false, time5.equals(time6));
    }
}