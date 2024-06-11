package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithValues() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(new Integer(1), timeOfDay.getHours());
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("01:30");
        assertEquals(new Integer(1), timeOfDay.getHours());
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(1, 45);

        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay1) == 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay3) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(new Integer(1), timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(new Integer(30), timeOfDay.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals("1:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(new Integer(130).hashCode(), timeOfDay.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 30);

        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertTrue(!timeOfDay1.equals(timeOfDay3));
    }
}