import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {
    
    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(10, 30);
        
        Assert.assertEquals(Integer.valueOf(10), time.getHours());
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("08:45");
        
        Assert.assertEquals(Integer.valueOf(8), time.getHours());
        Assert.assertEquals(Integer.valueOf(45), time.getMinutes());
    }
    
    @Test
    public void testCompareTo_SameTime() {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        
        Assert.assertEquals(0, time1.compareTo(time2));
    }
    
    @Test
    public void testCompareTo_DifferentHours() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(15, 0);
        
        Assert.assertTrue(time1.compareTo(time2) < 0);
        Assert.assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testCompareTo_DifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        
        Assert.assertTrue(time1.compareTo(time2) < 0);
        Assert.assertTrue(time2.compareTo(time1) > 0);
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        Assert.assertEquals(Integer.valueOf(9), time.getHours());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(9, 15);
        
        Assert.assertEquals(Integer.valueOf(15), time.getMinutes());
    }
    
    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(5, 0);
        
        Assert.assertEquals("5:0", time.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(7, 30);
        TimeOfDay time2 = new TimeOfDay(7, 30);
        
        Assert.assertEquals(time1.hashCode(), time2.hashCode());
    }
    
    @Test
    public void testEquals_SameObject() {
        TimeOfDay time = new TimeOfDay(6, 45);
        
        Assert.assertEquals(time, time);
    }
    
    @Test
    public void testEquals_NullObject() {
        TimeOfDay time = new TimeOfDay(6, 45);
        
        Assert.assertNotEquals(time, null);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        TimeOfDay time = new TimeOfDay(6, 45);
        Integer integer = new Integer(10);
        
        Assert.assertNotEquals(time, integer);
    }
    
    @Test
    public void testEquals_EqualTimes() {
        TimeOfDay time1 = new TimeOfDay(6, 45);
        TimeOfDay time2 = new TimeOfDay(6, 45);
        
        Assert.assertEquals(time1, time2);
    }
    
    @Test
    public void testEquals_DifferentHours() {
        TimeOfDay time1 = new TimeOfDay(6, 45);
        TimeOfDay time2 = new TimeOfDay(10, 45);
        
        Assert.assertNotEquals(time1, time2);
    }
    
    @Test
    public void testEquals_DifferentMinutes() {
        TimeOfDay time1 = new TimeOfDay(6, 45);
        TimeOfDay time2 = new TimeOfDay(6, 30);
        
        Assert.assertNotEquals(time1, time2);
    }
}