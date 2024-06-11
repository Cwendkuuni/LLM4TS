import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals(10, time1.getHours().intValue());
        assertEquals(30, time1.getMinutes().intValue());
        
        TimeOfDay time2 = new TimeOfDay(0, 0);
        assertEquals(0, time2.getHours().intValue());
        assertEquals(0, time2.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time1 = new TimeOfDay("10:30");
        assertEquals(10, time1.getHours().intValue());
        assertEquals(30, time1.getMinutes().intValue());
        
        TimeOfDay time2 = new TimeOfDay("00:00");
        assertEquals(0, time2.getHours().intValue());
        assertEquals(0, time2.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        TimeOfDay time3 = new TimeOfDay(10, 45);
        TimeOfDay time4 = new TimeOfDay(10, 30);
        
        assertEquals(1, time1.compareTo(time2));
        assertEquals(-1, time2.compareTo(time1));
        assertEquals(1, time1.compareTo(time3));
        assertEquals(0, time1.compareTo(time4));
    }
    
    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        assertEquals("10:30", time1.toString());
        
        TimeOfDay time2 = new TimeOfDay(0, 0);
        assertEquals("0:0", time2.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);
        
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);
        
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}