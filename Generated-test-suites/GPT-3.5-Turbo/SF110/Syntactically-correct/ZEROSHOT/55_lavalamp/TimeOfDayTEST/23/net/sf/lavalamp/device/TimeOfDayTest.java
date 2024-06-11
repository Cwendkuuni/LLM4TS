
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
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);

        Assert.assertEquals(0, time1.compareTo(time2));
        Assert.assertEquals(-1, time1.compareTo(time3));
        Assert.assertEquals(1, time3.compareTo(time1));
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(8, 15);

        Assert.assertEquals("8:15", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);

        Assert.assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);

        Assert.assertTrue(time1.equals(time2));
        Assert.assertFalse(time1.equals(time3));
    }
}
