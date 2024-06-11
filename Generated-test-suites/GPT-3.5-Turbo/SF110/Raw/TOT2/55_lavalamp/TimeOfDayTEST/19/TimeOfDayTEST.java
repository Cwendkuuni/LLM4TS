import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInt() {
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
        TimeOfDay time2 = new TimeOfDay(11, 45);
        
        assertTrue(time1.compareTo(time2) > 0);
        assertTrue(time2.compareTo(time1) < 0);
        assertEquals(0, time1.compareTo(new TimeOfDay(12, 30)));
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 15);
        
        assertEquals("10:15", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 15);
        
        assertEquals(new Integer(1015).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(11, 45);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}