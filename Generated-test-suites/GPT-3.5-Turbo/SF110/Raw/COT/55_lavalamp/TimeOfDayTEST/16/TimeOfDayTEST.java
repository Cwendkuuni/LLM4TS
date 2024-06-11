import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    // Test case for TimeOfDay(int hours, int minutes) constructor
    @Test
    public void testTimeOfDayConstructor() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), time.getHours());
        assertEquals(new Integer(30), time.getMinutes());
    }

    // Test case for TimeOfDay(String time) constructor
    @Test
    public void testTimeOfDayStringConstructor() {
        TimeOfDay time = new TimeOfDay("07:45");
        assertEquals(new Integer(7), time.getHours());
        assertEquals(new Integer(45), time.getMinutes());
    }

    // Test case for compareTo(TimeOfDay that) method
    @Test
    public void testTimeOfDayCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(14, 45);
        TimeOfDay time3 = new TimeOfDay(12, 30);

        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time1.compareTo(time3) == 0);
    }

    // Test case for getHours() method
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        assertEquals(new Integer(9), time.getHours());
    }

    // Test case for getMinutes() method
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(11, 55);
        assertEquals(new Integer(55), time.getMinutes());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 0);
        assertEquals("5:0", time.toString());
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);

        assertEquals(time1.hashCode(), time2.hashCode());
    }

    // Test case for equals(Object obj) method
    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(8, 45);
        TimeOfDay time2 = new TimeOfDay(8, 45);
        TimeOfDay time3 = new TimeOfDay(8, 30);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }

    // Additional test case for equals(Object obj) method with different types of objects
    @Test
    public void testEqualsWithDifferentType() {
        TimeOfDay time = new TimeOfDay(6, 15);
        String str = "6:15";

        assertFalse(time.equals(str));
    }

}