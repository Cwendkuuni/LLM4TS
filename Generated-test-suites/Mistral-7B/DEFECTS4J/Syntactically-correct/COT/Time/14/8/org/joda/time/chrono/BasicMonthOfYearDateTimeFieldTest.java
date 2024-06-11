import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        assertEquals(DateTimeConstants.JANUARY, field.get(testTime));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        long expectedTime = DateTimeConstants.SECOND_OF_DAY;

        long result = field.add(testTime, 1);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.DAY_OF_MONTH;
        expectedTime = DateTimeConstants.DAY_OF_MONTH + 30;
        result = field.add(testTime, 1);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.DAY_OF_MONTH;
        expectedTime = DateTimeConstants.DAY_OF_MONTH - 30;
        result = field.add(testTime, -1);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.DAY_OF_MONTH;
        expectedTime = DateTimeConstants.DAY_OF_MONTH + 12;
        result = field.add(testTime, 12);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.DAY_OF_MONTH;
        expectedTime = DateTimeConstants.DAY_OF_MONTH - 12;
        result = field.add(testTime, -12);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.MINUTE_OF_HOUR;
        expectedTime = DateTimeConstants.HOUR_OF_DAY + 1;
        result = field.add(testTime, 1);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L;
        expectedTime = testTime + (12 * 30 * 24 * 60 * 60 * 1000L);
        result = field.add(testTime, 12 * 30);
        assertEquals(expectedTime, result);

        testTime = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L;
        expectedTime = testTime - (12 * 30 * 24 * 60 * 60 * 1000L);
        result = field.add(testTime, -12 * 30);
        assertEquals(expectedTime, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExceptionLargeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        field.add(testTime, Integer.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExceptionNegativeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        field.add(testTime, Integer.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        field.set(testTime, DateTimeConstants.JANUARY - 1);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        long expectedTime = DateTimeConstants.DAY_OF_MONTH;

        testTime = field.set(testTime, DateTimeConstants.JANUARY + 1);
        assertEquals(expectedTime, testTime);

        testTime = field.set(testTime, DateTimeConstants.DECEMBER);
        expectedTime = DateTimeConstants.DAY_OF_MONTH;
        assertEquals(expectedTime, testTime);

        testTime = field.set(testTime, DateTimeConstants.DECEMBER + 1);
        expectedTime = DateTimeConstants.DAY_OF_MONTH + 31;
        assertEquals(expectedTime, testTime);

        testTime = field.set(testTime, DateTimeConstants.JANUARY);
        expectedTime = DateTimeConstants.DAY_OF_MONTH - 31;
        testTime = field.set(testTime, DateTimeConstants.JANUARY);
        assertEquals(expectedTime, testTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidInput() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        field.set(testTime, 13);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;

        testTime = field.addWrapField(testTime, 13);
        assertTrue(testTime > DateTimeConstants.MINUTE_OF_DAY);

        testTime = field.addWrapField(testTime, -13);
        assertTrue(testTime < DateTimeConstants.MINUTE_OF_DAY);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long minuendInstant = DateTimeConstants.MINUTE_OF_DAY;
        long subtrahendInstant = DateTimeConstants.HOUR_OF_DAY;

        assertEquals(1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.DAY_OF_MONTH;
        subtrahendInstant = DateTimeConstants.DAY_OF_MONTH + 30;
        assertEquals(0, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.DAY_OF_MONTH;
        subtrahendInstant = DateTimeConstants.DAY_OF_MONTH - 30;
        assertEquals(-1, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.DAY_OF_MONTH;
        subtrahendInstant = DateTimeConstants.DAY_OF_MONTH + 12;
        assertEquals(11, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.DAY_OF_MONTH;
        subtrahendInstant = DateTimeConstants.DAY_OF_MONTH - 12;
        assertEquals(-11, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.HOUR_OF_DAY;
        subtrahendInstant = DateTimeConstants.MINUTE_OF_HOUR;
        assertEquals(60, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L;
        subtrahendInstant = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L + 12 * 30 * 24 * 60 * 60 * 1000L;
        assertEquals(12 * 30 * 24 * 60 * 60 * 1000L, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));

        minuendInstant = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L;
        subtrahendInstant = DateTimeConstants.HOUR_OF_DAY * 60 * 60 * 1000L - 12 * 30 * 24 * 60 * 60 * 1000L;
        assertEquals(-12 * 30 * 24 * 60 * 60 * 1000L, field.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    @Test
    public void testSetLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;

        assertTrue(field.isLeap(testTime));
        testTime += 86400000L; // Add a day
        assertFalse(field.isLeap(testTime));

        testTime -= 86400000L; // Subtract a day

        BaseDateTime date = new BaseDateTime(1999, 2, 29);
        testTime = date.getMillis();
        assertTrue(field.isLeap(testTime));

        testTime += 86400000L; // Add a day
        assertFalse(field.isLeap(testTime));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfMonthDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.FEBRUARY);
        long testTime = DateTimeConstants.MINUTE_OF_DAY;
        int leapAmount = field.getLeapAmount(testTime);
        assertEquals(0, leapAmount);

        testTime += 86400000L; // Add a day
        leapAmount = field.getLeapAmount(testTime);
        assertEquals(1, leapAmount);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLeapAmountException() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        field.getLeapAmount(DateTimeConstants.MINUTE_OF_DAY);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        DurationField durationField = field.getRangeDurationField();
        assertEquals(DurationField.YEARS, durationField);
    }
}