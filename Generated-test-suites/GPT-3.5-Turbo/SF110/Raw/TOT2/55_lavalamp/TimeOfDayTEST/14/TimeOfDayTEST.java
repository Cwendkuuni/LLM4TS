import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        assertEquals(1, time1.compareTo(time2));
        assertEquals(-1, time2.compareTo(time1));
        assertEquals(0, time1.compareTo(new TimeOfDay(10, 30)));
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals("9:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(Integer.valueOf(915).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(8, 45);
        
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}