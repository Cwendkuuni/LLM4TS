import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorInt() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorString() {
        TimeOfDay time = new TimeOfDay("10:15");
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(15), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertTrue(time1.compareTo(time2) > 0);
        assertTrue(time2.compareTo(time1) < 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(6, 0);
        assertEquals("6:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(9, 45);
        assertEquals(new Integer(945).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        assertTrue(time1.equals(time2));
    }

}