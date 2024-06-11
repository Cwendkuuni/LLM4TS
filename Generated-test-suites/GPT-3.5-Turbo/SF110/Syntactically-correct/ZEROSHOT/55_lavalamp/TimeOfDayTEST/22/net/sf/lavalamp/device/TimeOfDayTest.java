import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay time = new TimeOfDay("08:45");
        assertEquals(new Integer(8), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareTo_DifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testCompareTo_DifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(9), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(15), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(6, 0);
        assertEquals("6:0", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(9, 30);
        TimeOfDay time2 = new TimeOfDay(9, 30);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_SameObject() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertTrue(time.equals(time));
    }
    
    @Test
    public void testEquals_NullObject() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertFalse(time.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        TimeOfDay time = new TimeOfDay(8, 0);
        assertFalse(time.equals("8:0"));
    }
    
    @Test
    public void testEquals_SameTime() {
        TimeOfDay time1 = new TimeOfDay(8, 0);
        TimeOfDay time2 = new TimeOfDay(8, 0);
        assertTrue(time1.equals(time2));
    }
    
    @Test
    public void testEquals_DifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 0);
        TimeOfDay time2 = new TimeOfDay(8, 0);
        assertFalse(time1.equals(time2));
    }
    
    @Test
    public void testEquals_DifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(8, 15);
        TimeOfDay time2 = new TimeOfDay(8, 0);
        assertFalse(time1.equals(time2));
    }
}