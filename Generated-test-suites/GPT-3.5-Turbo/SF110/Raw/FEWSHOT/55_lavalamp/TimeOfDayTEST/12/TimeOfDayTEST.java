package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        TimeOfDay time3 = new TimeOfDay(13, 0);
        
        assertTrue(time1.compareTo(time1) == 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time2.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time2) > 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}