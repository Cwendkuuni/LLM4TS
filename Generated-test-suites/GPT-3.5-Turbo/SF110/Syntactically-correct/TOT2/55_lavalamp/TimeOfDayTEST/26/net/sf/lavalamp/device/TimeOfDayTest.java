package net.sf.lavalamp.device;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInts() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(15, 30);
        TimeOfDay time2 = new TimeOfDay(15, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(18, 15);
        assertEquals("18:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 20);
        assertEquals(new Integer(1020).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 20);
        TimeOfDay time2 = new TimeOfDay(10, 20);
        assertTrue(time1.equals(time2));
    }
}