import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {
    
    private TimeOfDay timeOfDay;
    
    @Before
    public void setUp() {
        timeOfDay = new TimeOfDay(10, 30);
    }
    
    @Test
    public void testConstructorWithIntegers() {
        assertEquals(10, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        timeOfDay = new TimeOfDay("05:45");
        assertEquals(5, timeOfDay.getHours().intValue());
        assertEquals(45, timeOfDay.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay otherTime = new TimeOfDay(10, 45);
        assertTrue(timeOfDay.compareTo(otherTime) < 0);
        
        otherTime = new TimeOfDay(10, 30);
        assertEquals(0, timeOfDay.compareTo(otherTime));
        
        otherTime = new TimeOfDay(10, 15);
        assertTrue(timeOfDay.compareTo(otherTime) > 0);
        
        otherTime = new TimeOfDay(9, 30);
        assertTrue(timeOfDay.compareTo(otherTime) > 0);
        
        otherTime = new TimeOfDay(11, 0);
        assertTrue(timeOfDay.compareTo(otherTime) < 0);
    }
    
    @Test
    public void testToString() {
        assertEquals("10:30", timeOfDay.toString());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 100 * timeOfDay.getHours() + timeOfDay.getMinutes();
        assertEquals(expectedHashCode, timeOfDay.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay sameTimeOfDay = new TimeOfDay(10, 30);
        assertTrue(timeOfDay.equals(sameTimeOfDay));
        
        TimeOfDay differentTimeOfDay = new TimeOfDay(5, 45);
        assertFalse(timeOfDay.equals(differentTimeOfDay));
        
        assertFalse(timeOfDay.equals(null));
        
        assertFalse(timeOfDay.equals("10:30"));
    }
}