import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        assertEquals(Integer.valueOf(9), time.getHours());
        assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareToEarlierTime() {
        TimeOfDay earlier = new TimeOfDay(10, 15);
        TimeOfDay later = new TimeOfDay(12, 30);
        assertTrue(earlier.compareTo(later) < 0);
    }
    
    @Test
    public void testCompareToLaterTime() {
        TimeOfDay earlier = new TimeOfDay(10, 15);
        TimeOfDay later = new TimeOfDay(12, 30);
        assertTrue(later.compareTo(earlier) > 0);
    }
    
    @Test
    public void testCompareToEqualTime() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(9, 30);
        assertEquals("9:30", time.toString());
    }
    
    @Test
    public void testHashCodeEqualObjects() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentObject() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEqualsSameObject() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertEquals(time, time);
    }
    
    @Test
    public void testEqualsNullObject() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertNotEquals(time, null);
    }
    
    @Test
    public void testEqualsDifferentClass() {
        TimeOfDay time = new TimeOfDay(10, 15);
        assertNotEquals(10, time);
    }
    
    @Test
    public void testEqualsDifferentObjectsWithSameValues() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(10, 15);
        assertEquals(time1, time2);
    }
    
    @Test
    public void testEqualsDifferentObjectsWithDifferentValues() {
        TimeOfDay time1 = new TimeOfDay(10, 15);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertNotEquals(time1, time2);
    }
    
    // Add more test cases to cover edge and error scenarios
    
}