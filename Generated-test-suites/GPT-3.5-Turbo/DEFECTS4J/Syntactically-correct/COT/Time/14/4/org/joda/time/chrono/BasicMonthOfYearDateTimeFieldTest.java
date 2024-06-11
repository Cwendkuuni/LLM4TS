import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(chronology, 6);

    @Test
    public void testGet() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        Assert.assertEquals(7, monthField.get(instant));
    }

    @Test
    public void testAdd() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        instant = monthField.add(instant, 2);
        Assert.assertEquals(9, monthField.get(instant));
    }

    @Test
    public void testAddWrapField() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        instant = monthField.addWrapField(instant, 7);
        Assert.assertEquals(2, monthField.get(instant));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = chronology.getYearMonthDayMillis(2022, 6, 15);
        long subtrahendInstant = chronology.getYearMonthDayMillis(2021, 5, 10);
        Assert.assertEquals(13L, monthField.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testSet() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        instant = monthField.set(instant, 5);
        Assert.assertEquals(5, monthField.get(instant));
    }

    @Test
    public void testIsLeap() {
        long instant = chronology.getYearMonthDayMillis(2020, 6, 15);
        Assert.assertTrue(monthField.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = chronology.getYearMonthDayMillis(2020, 6, 15);
        Assert.assertEquals(1, monthField.getLeapAmount(instant));
    }

    @Test
    public void testGetMinimumValue() {
        Assert.assertEquals(DateTimeFieldType.monthOfYear().getMinimumValue(), monthField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        Assert.assertEquals(DateTimeFieldType.monthOfYear().getMaximumValue(), monthField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        long rounded = monthField.roundFloor(instant);
        Assert.assertEquals(chronology.getYearMonthDayMillis(2021, 7, 1), rounded);
    }

    @Test
    public void testRemainder() {
        long instant = chronology.getYearMonthDayMillis(2021, 7, 15);
        long remainder = monthField.remainder(instant);
        Assert.assertEquals(15 * chronology.getMillisInDay(), remainder);
    }

}