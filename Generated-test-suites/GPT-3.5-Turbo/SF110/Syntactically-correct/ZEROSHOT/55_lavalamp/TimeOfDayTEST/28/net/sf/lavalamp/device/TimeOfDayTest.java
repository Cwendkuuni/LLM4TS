package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(new Integer(12), time.getHours());
        Assert.assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("09:45");
        Assert.assertEquals(new Integer(9), time.getHours());
        Assert.assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(9, 45);
        Assert.assertEquals(1, time1.compareTo(time2)); // time1 should be later than time2
        Assert.assertEquals(-1, time2.compareTo(time1)); // time2 should be earlier than time1
        Assert.assertEquals(0, time1.compareTo(time1)); // time1 should be equal to time1
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(new Integer(12), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(new Integer(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals("12:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        Assert.assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(9, 45);
        Assert.assertTrue(time1.equals(time2)); // time1 should be equal to time2
        Assert.assertFalse(time1.equals(time3)); // time1 should not be equal to time3
        Assert.assertFalse(time1.equals(null)); // time1 should not be equal to null
        Assert.assertFalse(time1.equals("12:30")); // time1 should not be equal to a different object type
    }
}