import org.joda.time.BaseDateTime;
import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.UnsupportedDateTimeFormatException;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 0, 1).getTimeInMillis();
        int result = field.get(testTime);
        Assert.assertEquals(1, result);

        testTime = new GregorianCalendar(2023, 11, 31).getTimeInMillis();
        result = field.get(testTime);
        Assert.assertEquals(12, result);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 0, 1).getTimeInMillis();

        // Adding 1 month
        long result = field.add(testTime, 1);
        LocalDateTime expected = new LocalDateTime(new LocalDate(2023, 2, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Adding -1 month
        result = field.add(testTime, -1);
        expected = new LocalDateTime(new LocalDate(2023, 1, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Adding 12 months
        result = field.add(testTime, 12);
        expected = new LocalDateTime(new LocalDate(2024, 2, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Adding more than 12 months (should wrap around)
        result = field.add(testTime, 13);
        expected = new LocalDateTime(new LocalDate(2024, 3, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Test with a date outside the minimum and maximum year range (should throw an exception)
        testTime = new GregorianCalendar(1900, 0, 1).getTimeInMillis();
        Assert.assertThrows(IllegalArgumentException.class, () -> field.add(testTime, 1));

        testTime = new GregorianCalendar(2100, 0, 1).getTimeInMillis();
        Assert.assertThrows(IllegalArgumentException.class, () -> field.add(testTime, 1));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 11, 30).getTimeInMillis();

        // Adding 1 month (should wrap around)
        long result = field.addWrapField(testTime, 1);
        LocalDateTime expected = new LocalDateTime(new LocalDate(2023, 12, 31), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Adding -1 month (should wrap around)
        result = field.addWrapField(testTime, -1);
        expected = new LocalDateTime(new LocalDate(2023, 10, 31), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime1 = new GregorianCalendar(2023, 0, 1).getTimeInMillis();
        long testTime2 = new GregorianCalendar(2023, 11, 30).getTimeInMillis();

        long result = field.getDifferenceAsLong(testTime1, testTime2);
        Assert.assertEquals(11, result);

        result = field.getDifferenceAsLong(testTime2, testTime1);
        Assert.assertEquals(-11, result);

        testTime1 = new GregorianCalendar(2023, 0, 31).getTimeInMillis();
        testTime2 = new GregorianCalendar(2023, 11, 31).getTimeInMillis();
        result = field.getDifferenceAsLong(testTime1, testTime2);
        Assert.assertEquals(11, result);

        testTime1 = new GregorianCalendar(2023, 11, 31).getTimeInMillis();
        testTime2 = new GregorianCalendar(2023, 0, 31).getTimeInMillis();
        result = field.getDifferenceAsLong(testTime2, testTime1);
        Assert.assertEquals(-11, result);

        // Test with a date outside the minimum and maximum year range (should throw an exception)
        long testTime3 = new GregorianCalendar(1900, 0, 1).getTimeInMillis();
        long testTime4 = new GregorianCalendar(2100, 0, 1).getTimeInMillis();
        Assert.assertThrows(IllegalArgumentException.class, () -> field.getDifferenceAsLong(testTime3, testTime4));
        Assert.assertThrows(IllegalArgumentException.class, () -> field.getDifferenceAsLong(testTime4, testTime3));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 0, 1).getTimeInMillis();

        long result = field.set(testTime, 13);
        Assert.assertEquals(-1, (int) (result - testTime));

        testTime = new GregorianCalendar(2023, 11, 30).getTimeInMillis();
        result = field.set(testTime, 1);
        LocalDateTime expected = new LocalDateTime(new LocalDate(2023, 12, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        testTime = new GregorianCalendar(2023, 11, 30).getTimeInMillis();
        result = field.set(testTime, 13);
        expected = new LocalDateTime(new LocalDate(2024, 1, 1), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);

        // Test with a date outside the minimum and maximum year range (should throw an exception)
        long testTime5 = new GregorianCalendar(1900, 0, 1).getTimeInMillis();
        Assert.assertThrows(IllegalArgumentException.class, () -> field.set(testTime5, 1));

        testTime5 = new GregorianCalendar(2100, 0, 1).getTimeInMillis();
        Assert.assertThrows(IllegalArgumentException.class, () -> field.set(testTime5, 1));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        DurationField result = field.getRangeDurationField();
        Assert.assertEquals(DurationField.YEARS, result.getFieldType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        long testTime1 = new GregorianCalendar(2023, 1, 1).getTimeInMillis();
        Assert.assertFalse(field.isLeap(testTime1));

        testTime1 = new GregorianCalendar(2024, 1, 1).getTimeInMillis();
        Assert.assertTrue(field.isLeap(testTime1));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        long testTime1 = new GregorianCalendar(2023, 1, 1).getTimeInMillis();
        Assert.assertEquals(0, field.getLeapAmount(testTime1));

        testTime1 = new GregorianCalendar(2024, 1, 1).getTimeInMillis();
        Assert.assertEquals(1, field.getLeapAmount(testTime1));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        DurationField result = field.getLeapDurationField();
        Assert.assertEquals(DurationField.DAYS, result.getFieldType());
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        Assert.assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        Assert.assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 11, 30).getTimeInMillis();
        long result = field.roundFloor(testTime);
        LocalDateTime expected = new LocalDateTime(new LocalDate(2023, 11, 30), new LocalTime(0, 0, 0, 0));
        Assert.assertEquals(expected.toDate().getTime(), result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.JANUARY);
        long testTime = new GregorianCalendar(2023, 11, 30).getTimeInMillis();
        long result = field.remainder(testTime);
        Assert.assertEquals(0, result);

        testTime = new GregorianCalendar(2023, 11, 31).getTimeInMillis();
        result = field.remainder(testTime);
        Assert.assertEquals(1, result);
    }
}