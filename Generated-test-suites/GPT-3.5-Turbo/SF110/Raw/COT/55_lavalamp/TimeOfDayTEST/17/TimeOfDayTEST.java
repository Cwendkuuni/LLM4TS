import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    
  
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals(9, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
  
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:30");
        assertEquals(9, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
  
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(10, 0);
        
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(new TimeOfDay(9, 30)));
    }
  
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals(9, time.getHours().intValue());
    }
  
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals(30, time.getMinutes().intValue());
    }
  
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals("9:30", time.toString());
    }
  
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
  
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        TimeOfDay time3 = new TimeOfDay(10, 0);
        
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("9:30"));
    }
    
    
}