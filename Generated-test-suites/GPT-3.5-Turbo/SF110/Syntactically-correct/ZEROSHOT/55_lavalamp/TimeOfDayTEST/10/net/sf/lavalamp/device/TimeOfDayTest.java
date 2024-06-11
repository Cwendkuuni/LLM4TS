package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {

    private TimeOfDay timeOfDay;

    @Before
    public void setUp() {
        timeOfDay = new TimeOfDay(12, 30);
    }

    @Test
    public void testConstructorWithIntegers() {
        assertNotNull(timeOfDay);
        assertEquals(Integer.valueOf(12), timeOfDay.getHours());
        assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDayString = new TimeOfDay("12:30");
        assertNotNull(timeOfDayString);
        assertEquals(Integer.valueOf(12), timeOfDayString.getHours());
        assertEquals(Integer.valueOf(30), timeOfDayString.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay other = new TimeOfDay(12, 45);
        assertTrue(timeOfDay.compareTo(other) < 0);

        other = new TimeOfDay(12, 15);
        assertTrue(timeOfDay.compareTo(other) > 0);

        other = new TimeOfDay(12, 30);
        assertEquals(0, timeOfDay.compareTo(other));

        other = new TimeOfDay(11, 30);
        assertTrue(timeOfDay.compareTo(other) > 0);

        other = new TimeOfDay(13, 30);
        assertTrue(timeOfDay.compareTo(other) < 0);
    }

    @Test
    public void testToString() {
        String expected = "12:30";
        assertEquals(expected, timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        int expected = 1230;
        assertEquals(expected, timeOfDay.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay other = new TimeOfDay(12, 30);
        assertTrue(timeOfDay.equals(other));
        
        other = new TimeOfDay(12, 45);
        assertFalse(timeOfDay.equals(other));
        
        assertFalse(timeOfDay.equals(null));
        assertFalse(timeOfDay.equals(new Object()));
    }
}