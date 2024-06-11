
package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructor() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        assertEquals(new Integer(12), time1.getHours());
        assertEquals(new Integer(0), time1.getMinutes());
        
        TimeOfDay time2 = new TimeOfDay("09:30");
        assertEquals(new Integer(9), time2.getHours());
        assertEquals(new Integer(30), time2.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(6, 0);
        TimeOfDay time2 = new TimeOfDay(6, 30);
        TimeOfDay time3 = new TimeOfDay(7, 0);
        
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time2.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time2) > 0);
        assertTrue(time1.compareTo(time1) == 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 45);
        assertEquals("9:45", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        TimeOfDay time3 = new TimeOfDay(12, 30);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("12:00"));
    }
}
