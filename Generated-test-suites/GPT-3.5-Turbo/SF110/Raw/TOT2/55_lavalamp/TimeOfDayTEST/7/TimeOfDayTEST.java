import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        
        assertTrue(time1.compareTo(time2) > 0);
        assertTrue(time2.compareTo(time1) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(8, 45);
        
        assertEquals(new Integer(8), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(8, 45);
        
        assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 0);
        
        assertEquals("5:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 15);
        
        assertEquals(new Integer(1015).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(7, 20);
        TimeOfDay time2 = new TimeOfDay(7, 20);
        TimeOfDay time3 = new TimeOfDay(8, 30);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}