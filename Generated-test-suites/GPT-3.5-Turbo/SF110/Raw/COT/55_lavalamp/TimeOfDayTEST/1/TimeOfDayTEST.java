import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithValidValues() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(0), time.getMinutes());
    }

    // Test cases for TimeOfDay constructor
    // Test invalid input: negative values
    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithNegativeHours() {
        new TimeOfDay(-1, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithNegativeMinutes() {
        new TimeOfDay(12, -30);
    }

    // Test invalid input: hours and minutes exceeding valid range
    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours() {
        new TimeOfDay(24, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes() {
        new TimeOfDay(12, 60);
    }

    // Test invalid input: invalid time format
    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructorWithInvalidTimeFormat() {
        new TimeOfDay("12:00PM");
    }

    @Test
    public void testTimeOfDayCompareToWithDifferentTimes() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }

    @Test
    public void testTimeOfDayCompareToWithSameTimes() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(0, time1.compareTo(time2));
        assertEquals(0, time2.compareTo(time1));
    }

    @Test
    public void testTimeOfDayGetHours() {
        TimeOfDay time = new TimeOfDay(12, 0);
        assertEquals(Integer.valueOf(12), time.getHours());
    }

    @Test
    public void testTimeOfDayGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testTimeOfDayToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testTimeOfDayHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertEquals(time1.hashCode(), time2.hashCode());

        TimeOfDay time3 = new TimeOfDay(12, 30);
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testTimeOfDayEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        assertTrue(time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(12, 30);
        assertFalse(time1.equals(time3));
    }

}



// All the test cases listed above go here.

