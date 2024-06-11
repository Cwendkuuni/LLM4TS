
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
        TimeOfDay time = new TimeOfDay("10:30");
        Assert.assertEquals(Integer.valueOf(10), time.getHours());
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        TimeOfDay time3 = new TimeOfDay(10, 45);

        Assert.assertTrue(time1.compareTo(time2) < 0);
        Assert.assertTrue(time2.compareTo(time1) > 0);
        Assert.assertTrue(time1.compareTo(time3) < 0);
        Assert.assertTrue(time3.compareTo(time1) > 0);
        Assert.assertTrue(time1.compareTo(time1) == 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(10), time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(30), time.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Assert.assertEquals("10:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(1030).hashCode(), time.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 30);

        Assert.assertTrue(time1.equals(time2));
        Assert.assertFalse(time1.equals(time3));
        Assert.assertFalse(time1.equals(null));
        Assert.assertFalse(time1.equals(new Object()));
    }

}
