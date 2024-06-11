import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntegers() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(10), timeOfDay.getHours());
        Assert.assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
    }

    @Test
    public void testConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("12:00");
        Assert.assertEquals(Integer.valueOf(12), timeOfDay.getHours());
        Assert.assertEquals(Integer.valueOf(0), timeOfDay.getMinutes());
    }

    @Test
    public void testCompareTo_SameHoursAndMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        Assert.assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareTo_SameHoursDifferentMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 45);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    @Test
    public void testCompareTo_DifferentHoursSameMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(10), timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        Assert.assertEquals("10:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEquals_True() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        Assert.assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testEquals_False() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        Assert.assertFalse(timeOfDay1.equals(timeOfDay2));
    }
}