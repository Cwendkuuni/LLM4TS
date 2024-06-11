import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    // Test getHours() method
    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), timeOfDay.getHours());
    }
    
    // Test getMinutes() method
    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals("10:30", timeOfDay.toString());
    }
    
    // Test hashCode() method
    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(new Integer(1030).hashCode(), timeOfDay.hashCode());
    }
    
    // Test equals() method
    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        assertTrue(timeOfDay1.equals(timeOfDay2));
    }
    
    // Test compareTo() method - hours are different
    @Test
    public void testCompareTo_HoursDifferent() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    // Test compareTo() method - minutes are different
    @Test
    public void testCompareTo_MinutesDifferent() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 45);
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }
    
    // Test compareTo() method - same hours and minutes
    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }
    
    // Test constructor with int parameters
    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(new Integer(10), timeOfDay.getHours());
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }
    
    // Test constructor with string parameter
    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay timeOfDay = new TimeOfDay("10:30");
        assertEquals(new Integer(10), timeOfDay.getHours());
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }
    
    // Add additional test cases for potential edge cases and exception handling scenarios
    
    // Test constructor with invalid string parameter
    @Test(expected = NumberFormatException.class)
    public void testConstructorWithInvalidStringParameter() {
        TimeOfDay timeOfDay = new TimeOfDay("10:3a");
    }
    
    // Test getHours() method with null hours
    @Test
    public void testGetHours_NullHours() {
        TimeOfDay timeOfDay = new TimeOfDay(null, 30);
        assertNull(timeOfDay.getHours());
    }
    
    // Test getMinutes() method with null minutes
    @Test
    public void testGetMinutes_NullMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, null);
        assertNull(timeOfDay.getMinutes());
    }
    
    // Test compareTo() method with null parameter
    @Test(expected = NullPointerException.class)
    public void testCompareTo_NullParameter() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        timeOfDay.compareTo(null);
    }
    
}