
package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        TimeOfDay time3 = new TimeOfDay(11, 30);
        
        // compare to should return negative if the invoking object is smaller
        assertEquals(-1, time1.compareTo(time2));
        
        // compare to should return positive if the invoking object is greater
        assertEquals(1, time3.compareTo(time2));
        
        // compare to should return 0 if the invoking object is equal to the argument
        assertEquals(0, time1.compareTo(new TimeOfDay(10, 30)));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(7, 30);
        assertEquals(Integer.valueOf(7), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(7, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(7, 30);
        assertEquals("7:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        // objects with same hours and minutes must have the same hash code
        assertEquals(time1.hashCode(), time2.hashCode());
        
        // objects with different hours and minutes must have different hash codes
        assertFalse(time1.hashCode() == time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);
        
        // objects with same hours and minutes must be equal
        assertEquals(time1, time2);
        
        // objects with different hours and minutes must not be equal
        assertFalse(time1.equals(time3));
    }
}
