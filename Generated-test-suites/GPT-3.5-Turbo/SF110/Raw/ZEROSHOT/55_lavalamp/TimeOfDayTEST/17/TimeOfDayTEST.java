import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithInt() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(10, timeOfDay.getHours().intValue());
        Assert.assertEquals(30, timeOfDay.getMinutes().intValue());
    }
    
    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("08:45");
        Assert.assertEquals(8, timeOfDay.getHours().intValue());
        Assert.assertEquals(45, timeOfDay.getMinutes().intValue());
    }
    
    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 45);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) > 0);
        Assert.assertTrue(timeOfDay2.compareTo(timeOfDay1) < 0);
        Assert.assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
    }
    
    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(9, 15);
        Assert.assertEquals(9, timeOfDay.getHours().intValue());
    }
    
    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(11, 20);
        Assert.assertEquals(20, timeOfDay.getMinutes().intValue());
    }
    
    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(14, 0);
        Assert.assertEquals("14:0", timeOfDay.toString());
    }
    
    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        Assert.assertEquals(timeOfDay1, timeOfDay2);
    }
}