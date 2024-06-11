import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {

    private TimeOfDay time1;
    private TimeOfDay time2;
    private TimeOfDay time3;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(15, 30);
        time2 = new TimeOfDay("09:45");
        time3 = new TimeOfDay(15, 30);
    }
    
    @Test
    public void testGetHours() {
        assertEquals(Integer.valueOf(15), time1.getHours());
        assertEquals(Integer.valueOf(9), time2.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        assertEquals(Integer.valueOf(30), time1.getMinutes());
        assertEquals(Integer.valueOf(45), time2.getMinutes());
    }
    
    @Test
    public void testToString() {
        assertEquals("15:30", time1.toString());
        assertEquals("9:45", time2.toString());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(new Integer(1530).hashCode(), time1.hashCode());
        assertEquals(new Integer(945).hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(time1.equals(time3));
        assertFalse(time1.equals(time2));
        assertFalse(time2.equals(null));
        assertFalse(time2.equals("09:45"));
    }
    
    @Test
    public void testCompareTo() {
        assertEquals(0, time1.compareTo(time3));
        assertEquals(-1, time2.compareTo(time1));
        assertEquals(1, time1.compareTo(time2));
    }

}