package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithHoursAndMinutes() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        assertEquals(Integer.valueOf(8), time1.getHours());
        assertEquals(Integer.valueOf(45), time1.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time1 = new TimeOfDay("08:45");
        assertEquals(Integer.valueOf(8), time1.getHours());
        assertEquals(Integer.valueOf(45), time1.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        TimeOfDay time2 = new TimeOfDay(7, 30);
        TimeOfDay time3 = new TimeOfDay(8, 45);
        
        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(time3));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        assertEquals(Integer.valueOf(8), time1.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        assertEquals(Integer.valueOf(45), time1.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        assertEquals("8:45", time1.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        TimeOfDay time3 = new TimeOfDay(7, 30);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        TimeOfDay time3 = new TimeOfDay(7, 30);
        
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}