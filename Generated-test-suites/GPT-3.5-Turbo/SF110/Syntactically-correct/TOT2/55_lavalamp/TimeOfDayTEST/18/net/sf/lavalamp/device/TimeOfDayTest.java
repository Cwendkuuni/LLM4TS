package net.sf.lavalamp.device;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithInt() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay time = new TimeOfDay("08:45");
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(15, 45);
        assertEquals(Integer.valueOf(15), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 20);
        assertEquals(Integer.valueOf(20), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 0);
        assertEquals("5:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(9, 25);
        assertEquals(new Integer(925).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 15);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentObjects() {
        TimeOfDay time = new TimeOfDay(4, 30);
        assertFalse(time.equals(new Object()));
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTimes() {
        TimeOfDay time1 = new TimeOfDay(6, 45);
        TimeOfDay time2 = new TimeOfDay(7, 20);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsReturnsFalseForNull() {
        TimeOfDay time = new TimeOfDay(1, 0);
        assertFalse(time.equals(null));
    }
}