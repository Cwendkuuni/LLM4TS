import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {
    
    private TimeOfDay time1;
    private TimeOfDay time2;
    private TimeOfDay time3;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(10, 30);
        time2 = new TimeOfDay(15, 45);
        time3 = new TimeOfDay(10, 30);
    }
    
    @Test
    public void testConstructorWithIntegers() {
        assertEquals(Integer.valueOf(10), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        assertEquals(Integer.valueOf(10), time3.getHours());
        assertEquals(Integer.valueOf(30), time3.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time1.compareTo(time3));
    }
    
    @Test
    public void testGetHours() {
        assertEquals(Integer.valueOf(10), time1.getHours());
        assertEquals(Integer.valueOf(15), time2.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        assertEquals(Integer.valueOf(30), time1.getMinutes());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
    }
    
    @Test
    public void testToString() {
        assertEquals("10:30", time1.toString());
        assertEquals("15:45", time2.toString());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(time1.hashCode(), time3.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(time1.equals(time3));
        assertFalse(time1.equals(time2));
        assertFalse(time1.equals(null));
        assertFalse(time1.equals("10:30"));
    }
}