import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithIntParameters() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 30);
        Assert.assertEquals(Integer.valueOf(8), timeOfDay.getHours());
        Assert.assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
    }

    @Test
    public void testConstructorWithStringParameter() {
        TimeOfDay timeOfDay = new TimeOfDay("08:30");
        Assert.assertEquals(Integer.valueOf(8), timeOfDay.getHours());
        Assert.assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
    }

    @Test
    public void testCompareToLessThan() {
        TimeOfDay timeOfDay1 = new TimeOfDay(8, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(9, 0);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    @Test
    public void testCompareToGreaterThan() {
        TimeOfDay timeOfDay1 = new TimeOfDay(9, 0);
        TimeOfDay timeOfDay2 = new TimeOfDay(8, 30);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) > 0);
    }

    @Test
    public void testCompareToEqual() {
        TimeOfDay timeOfDay1 = new TimeOfDay(9, 0);
        TimeOfDay timeOfDay2 = new TimeOfDay(9, 0);
        Assert.assertTrue(timeOfDay1.compareTo(timeOfDay2) == 0);
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 30);
        Assert.assertEquals("8:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(8, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(8, 30);
        Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(8, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(8, 30);
        Assert.assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testEqualsSameInstance() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 30);
        Assert.assertTrue(timeOfDay.equals(timeOfDay));
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 30);
        Assert.assertFalse(timeOfDay.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        TimeOfDay timeOfDay = new TimeOfDay(8, 30);
        Assert.assertFalse(timeOfDay.equals("08:30"));
    }
  
    // Additional test cases for edge cases and exception handling scenarios can be added here

}

