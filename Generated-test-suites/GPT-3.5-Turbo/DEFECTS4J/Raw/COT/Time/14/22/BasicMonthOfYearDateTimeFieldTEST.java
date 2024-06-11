import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);

    @Test
    public void testIsLenient() {
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = 0;
        int month = field.get(instant);
        Assert.assertEquals(1, month);
    }

    @Test
    public void testAdd() {
        long instant = 0;
        long newInstant = field.add(instant, 1);
        Assert.assertEquals(field.get(instant) + 1, field.get(newInstant));
    }

    @Test
    public void testAddWrapField() {
        long instant = 0;
        long newInstant = field.addWrapField(instant, 1);
        Assert.assertEquals(field.get(instant) + 1, field.get(newInstant));
        newInstant = field.addWrapField(instant, -1);
        Assert.assertEquals(field.get(instant) - 1, field.get(newInstant));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = 0;
        long instant2 = 1000;
        long difference = field.getDifferenceAsLong(instant1, instant2);
        Assert.assertEquals(field.get(instant2) - field.get(instant1), difference);
    }

    @Test
    public void testSet() {
        long instant = 0;
        long newInstant = field.set(instant, 2);
        Assert.assertEquals(2, field.get(newInstant));
    }

    @Test
    public void testIsLeap() {
        long instant = 0;
        Assert.assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = 0;
        int leapAmount = field.getLeapAmount(instant);
        Assert.assertEquals(0, leapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        Assert.assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        Assert.assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = 0;
        long roundedFloor = field.roundFloor(instant);
        Assert.assertEquals(0, field.remainder(roundedFloor));
    }

    @Test
    public void testRemainder() {
        long instant = 0;
        long remainder = field.remainder(instant);
        Assert.assertEquals(0, remainder);
    }
}