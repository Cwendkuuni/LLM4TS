package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(1, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(12, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 0);
        TimeOfDay time3 = new TimeOfDay(1, 30);
        
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time3));
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals("10:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals(1015, time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        
        assertEquals(true, time1.equals(time2));
        assertEquals(false, time1.equals(time3));
    }
}