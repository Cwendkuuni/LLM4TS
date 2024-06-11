import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testTimeOfDayConstructor1() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testTimeOfDayConstructor2() {
        TimeOfDay time = new TimeOfDay("08:45");
        assertEquals(Integer.valueOf(8), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(Integer.valueOf(15), time.getMinutes());
    }
    
    @Test
    public void testCompareToEarlierTime() {
        TimeOfDay time1 = new TimeOfDay(9, 0);
        TimeOfDay time2 = new TimeOfDay(10, 0);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareToLaterTime() {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(10, 0);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareToSameTime() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        assertTrue(time1.compareTo(time2) == 0);
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(15, 30);
        assertEquals("15:30", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(14, 45);
        assertEquals(Integer.valueOf(1445).hashCode(), time.hashCode());
    }
    
    @Test
    public void testEqualsSameObject() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertTrue(time.equals(time));
    }
    
    @Test
    public void testEqualsNullObject() {
        TimeOfDay time = new TimeOfDay(17, 30);
        assertFalse(time.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        TimeOfDay time = new TimeOfDay(13, 15);
        assertFalse(time.equals("13:15"));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(14, 45);
        assertFalse(time1.equals(time2));
    }

}