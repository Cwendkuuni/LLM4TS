
import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(8, 30);
        Assert.assertEquals(new Integer(8), time.getHours());
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
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        Assert.assertEquals(0, time1.compareTo(time2));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(14, 30);
        Assert.assertEquals(new Integer(14), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(15, 45);
        Assert.assertEquals(new Integer(45), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(18, 0);
        Assert.assertEquals("18:0", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(20, 15);
        Assert.assertEquals(2015, time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        Assert.assertTrue(time1.equals(time2));
        Assert.assertFalse(time1.equals(null));
        Assert.assertFalse(time1.equals("10:30"));
    }
}
