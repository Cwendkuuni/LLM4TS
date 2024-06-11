package net.sf.lavalamp.device;
import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(10, (int)timeOfDay.getHours());
        assertEquals(30, (int)timeOfDay.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("06:45");
        assertEquals(6, (int)timeOfDay.getHours());
        assertEquals(45, (int)timeOfDay.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 20);
        assertEquals("15:20", timeOfDay.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 0);
        assertEquals(new Integer(800).hashCode(), timeOfDay.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }
}