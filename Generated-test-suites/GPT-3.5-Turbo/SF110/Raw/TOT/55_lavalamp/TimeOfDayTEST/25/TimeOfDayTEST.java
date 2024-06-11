import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {
    
    private TimeOfDay time1;
    private TimeOfDay time2;
    private TimeOfDay time3;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(5, 30);
        time2 = new TimeOfDay("12:45");
        time3 = new TimeOfDay("08:15");
    }
    
    @Test
    public void testConstructorWithInt() {
        assertEquals(Integer.valueOf(5), time1.getHours());
        assertEquals(Integer.valueOf(30), time1.getMinutes());
        assertEquals(Integer.valueOf(12), time2.getHours());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
        assertEquals(Integer.valueOf(8), time3.getHours());
        assertEquals(Integer.valueOf(15), time3.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time4 = new TimeOfDay("00:00");
        assertEquals(Integer.valueOf(0), time4.getHours());
        assertEquals(Integer.valueOf(0), time4.getMinutes());
        
        TimeOfDay time5 = new TimeOfDay("23:59");
        assertEquals(Integer.valueOf(23), time5.getHours());
        assertEquals(Integer.valueOf(59), time5.getMinutes());
    }
    
    @Test
    public void testCompareTo() {
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertEquals(0, time3.compareTo(time3));
    }
    
    @Test
    public void testGetHours() {
        assertEquals(Integer.valueOf(5), time1.getHours());
        assertEquals(Integer.valueOf(12), time2.getHours());
        assertEquals(Integer.valueOf(8), time3.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        assertEquals(Integer.valueOf(30), time1.getMinutes());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
        assertEquals(Integer.valueOf(15), time3.getMinutes());
    }
    
    @Test
    public void testToString() {
        assertEquals("5:30", time1.toString());
        assertEquals("12:45", time2.toString());
        assertEquals("8:15", time3.toString());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(time1.hashCode(), time1.hashCode());
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(time1.equals(time1));
        assertFalse(time1.equals(time2));
        assertFalse(time2.equals(null));
        assertFalse(time2.equals("12:45"));
        assertFalse(time2.equals(new TimeOfDay("12:40")));
        assertFalse(time3.equals(new TimeOfDay("08:20")));
    }
}