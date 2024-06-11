import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(30, time.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_SameObject() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertTrue(time.equals(time));
    }
    
    @Test
    public void testEquals_NullObject() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertFalse(time.equals("10:30"));
    }
    
    @Test
    public void testEquals_DifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertFalse(time1.equals(time2));
    }
    
    @Test
    public void testEquals_DifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        assertFalse(time1.equals(time2));
    }
    
    @Test
    public void testEquals_EqualObjects() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testCompareTo_LessThan() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareTo_GreaterThan() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareTo_Equal() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        assertEquals(0, time1.compareTo(time2));
    }
}