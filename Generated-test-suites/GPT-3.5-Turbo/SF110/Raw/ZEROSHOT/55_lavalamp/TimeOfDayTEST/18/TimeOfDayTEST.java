import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(Integer.valueOf(12), time.getHours());
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay time = new TimeOfDay("12:30");
        Assert.assertEquals(Integer.valueOf(12), time.getHours());
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        TimeOfDay time3 = new TimeOfDay(12, 45);

        Assert.assertTrue(time1.compareTo(time2) < 0);
        Assert.assertTrue(time2.compareTo(time1) > 0);
        Assert.assertTrue(time1.compareTo(time3) < 0);
        Assert.assertTrue(time3.compareTo(time1) > 0);
        Assert.assertTrue(time2.compareTo(time3) > 0);
        Assert.assertTrue(time3.compareTo(time2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(Integer.valueOf(12), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
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
        TimeOfDay time3 = new TimeOfDay(13, 30);

        Assert.assertEquals(time1.hashCode(), time2.hashCode());
        Assert.assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 30);
        TimeOfDay time4 = new TimeOfDay(12, 45);

        Assert.assertTrue(time1.equals(time2));
        Assert.assertFalse(time1.equals(time3));
        Assert.assertFalse(time1.equals(time4));
    }
}