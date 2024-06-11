import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithValidValues() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidHours() {
        new TimeOfDay(-1, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinutes() {
        new TimeOfDay(12, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringConstructorWithInvalidFormat() {
        new TimeOfDay("12:30:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringConstructorWithInvalidValues() {
        new TimeOfDay("25:30");
    }

    // Add more test cases for constructor edge cases...

    @Test
    public void testCompareToWithDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(15, 30);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }

    @Test
    public void testCompareToWithDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
    }

    // Add more test cases for compareTo edge cases...

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(12), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testToStringWithSingleDigitHours() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals("3:45", time.toString());
    }

    @Test
    public void testToStringWithSingleDigitMinutes() {
        TimeOfDay time = new TimeOfDay(12, 5);
        assertEquals("12:05", time.toString());
    }

    // Add more test cases for toString edge cases...

    @Test
    public void testEqualsWithNullObject() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertFalse(time.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassObject() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertFalse(time.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentHours() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(15, 30);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsWithDifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsWithSameValues() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));
    }

    // Add more test cases for equals edge cases...

    // Add more test cases for hashCode...

    // Add more test cases for exceptions...

    // 

    // Add all the individual test cases here...

    // 
}