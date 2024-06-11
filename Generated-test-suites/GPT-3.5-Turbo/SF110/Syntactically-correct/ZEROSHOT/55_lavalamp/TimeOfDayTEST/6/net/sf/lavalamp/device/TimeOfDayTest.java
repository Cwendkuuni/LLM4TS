import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    private TimeOfDay timeOfDay1;
    private TimeOfDay timeOfDay2;
    
    @Before
    public void setUp() {
        timeOfDay1 = new TimeOfDay(8, 30);
        timeOfDay2 = new TimeOfDay("10:45");
    }
    
    @Test
    public void testConstructorWithIntegers() {
        assertEquals(8, timeOfDay1.getHours().intValue());
        assertEquals(30, timeOfDay1.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        assertEquals(10, timeOfDay2.getHours().intValue());
        assertEquals(45, timeOfDay2.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
        assertEquals(0, timeOfDay1.compareTo(new TimeOfDay(8, 30)));
    }
    
    @Test
    public void testToString() {
        assertEquals("8:30", timeOfDay1.toString());
        assertEquals("10:45", timeOfDay2.toString());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(830, timeOfDay1.hashCode());
        assertEquals(1045, timeOfDay2.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(timeOfDay1.equals(new TimeOfDay(8, 30)));
        assertTrue(timeOfDay2.equals(new TimeOfDay("10:45")));
        assertFalse(timeOfDay1.equals(timeOfDay2));
    }
}