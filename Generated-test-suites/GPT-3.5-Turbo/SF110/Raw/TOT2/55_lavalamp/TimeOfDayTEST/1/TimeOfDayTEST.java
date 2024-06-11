import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(8, 30);
        assertEquals(8, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(12, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 15);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 0);
        assertEquals("5:0", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(2, 45);
        assertEquals(new Integer(245).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(3, 30);
        TimeOfDay time2 = new TimeOfDay(3, 30);
        assertTrue(time1.equals(time2));
    }
    
}