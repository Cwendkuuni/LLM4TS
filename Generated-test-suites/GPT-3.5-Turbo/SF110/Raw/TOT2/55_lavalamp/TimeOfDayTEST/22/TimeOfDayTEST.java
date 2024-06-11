import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        assertEquals(new Integer(9), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        assertEquals(new Integer(15), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        assertEquals("9:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        assertEquals(915, time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(9, 15);
        TimeOfDay time2 = new TimeOfDay(9, 15);
        
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testConstructorWithTimeString() {
        TimeOfDay time = new TimeOfDay("10:30");
        
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
}