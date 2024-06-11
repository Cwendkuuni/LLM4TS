package net.sf.lavalamp.device;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
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
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
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
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(1030).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }
}