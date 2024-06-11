package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeOfDayTest {
    
    private TimeOfDay time1;
    private TimeOfDay time2;
    
    @Before
    public void setUp() {
        time1 = new TimeOfDay(10, 30);
        time2 = new TimeOfDay("14:45");
    }
    
    @Test
    public void testConstructorWithHoursAndMinutes() {
        Assert.assertEquals(10, time1.getHours().intValue());
        Assert.assertEquals(30, time1.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        Assert.assertEquals(14, time2.getHours().intValue());
        Assert.assertEquals(45, time2.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        Assert.assertTrue(time1.compareTo(time2) < 0);
        Assert.assertTrue(time2.compareTo(time1) > 0);
        Assert.assertTrue(time1.compareTo(new TimeOfDay(10, 30)) == 0);
    }
    
    @Test
    public void testGetHours() {
        Assert.assertEquals(10, time1.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        Assert.assertEquals(30, time1.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("10:30", time1.toString());
    }
    
    @Test
    public void testHashCode() {
        Assert.assertEquals(1030, time1.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay time3 = new TimeOfDay(10, 30);
        TimeOfDay time4 = new TimeOfDay(12, 45);
        
        Assert.assertTrue(time1.equals(time3));
        Assert.assertFalse(time1.equals(time2));
        Assert.assertFalse(time1.equals(time4));
    }
    
    // Add more test methods as needed
    
}