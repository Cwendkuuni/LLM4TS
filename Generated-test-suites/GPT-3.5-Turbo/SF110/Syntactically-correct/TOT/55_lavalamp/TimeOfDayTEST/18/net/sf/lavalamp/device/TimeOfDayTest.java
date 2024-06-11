import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testConstructor_withIntParameters() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(Integer.valueOf(10), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructor_withStringParameter() {
        TimeOfDay time = new TimeOfDay("12:45");
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo_greaterThan() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        assertTrue(time1.compareTo(time2) > 0);
    }
    
    @Test
    public void testCompareTo_lessThan() {
        TimeOfDay time1 = new TimeOfDay(7, 20);
        TimeOfDay time2 = new TimeOfDay(9, 10);
        assertTrue(time1.compareTo(time2) < 0);
    }
    
    @Test
    public void testCompareTo_equalTo() {
        TimeOfDay time1 = new TimeOfDay(6, 15);
        TimeOfDay time2 = new TimeOfDay(6, 15);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(4, 30);
        assertEquals(Integer.valueOf(4), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 50);
        assertEquals(Integer.valueOf(50), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(2, 35);
        assertEquals("2:35", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(3, 10);
        TimeOfDay time2 = new TimeOfDay(3, 10);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_withSameObject() {
        TimeOfDay time = new TimeOfDay(1, 25);
        assertEquals(time, time);
    }
    
    @Test
    public void testEquals_withNullObject() {
        TimeOfDay time = new TimeOfDay(5, 40);
        assertNotEquals(time, null);
    }
    
    @Test
    public void testEquals_withDifferentClass() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertNotEquals(time, "12:00");
    }
    
    @Test
    public void testEquals_withDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(5, 55);
        TimeOfDay time2 = new TimeOfDay(7, 55);
        assertNotEquals(time1, time2);
    }
    
    @Test
    public void testEquals_withDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 45);
        assertNotEquals(time1, time2);
    }
    
    @Test
    public void testEquals_withEqualHoursAndMinutes() {
        TimeOfDay time1 = new TimeOfDay(9, 15);
        TimeOfDay time2 = new TimeOfDay(9, 15);
        assertEquals(time1, time2);
    }

}