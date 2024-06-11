import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        int expected = DateTimeConstants.FEBRUARY;
        int actual = field.get(instant);
        assertEquals(expected, actual);

        expected = DateTimeConstants.DECEMBER;
        instant += DateTimeConstants.MILLIS_PER_DAY * 365;
        actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        int monthsToAdd = 2;
        long expected = new LocalDateTime(2015, 3, 31, 0, 0, 0).toInstant().getMillis();
        long actual = field.add(instant, monthsToAdd);
        assertEquals(expected, actual);

        monthsToAdd = -1;
        expected = new LocalDateTime(2015, 11, 30, 0, 0, 0).toInstant().getMillis();
        actual = field.add(instant, monthsToAdd);
        assertEquals(expected, actual);

        monthsToAdd = 13;
        try {
            field.add(instant, monthsToAdd);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2015, 12, 31, 0, 0, 0).toInstant().getMillis();
        int monthsToAdd = 1;
        long expected = new LocalDateTime(2016, 1, 31, 0, 0, 0).toInstant().getMillis();
        long actual = field.addWrapField(instant, monthsToAdd);
        assertEquals(expected, actual);

        monthsToAdd = 12;
        long expected12 = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        actual = field.addWrapField(instant, monthsToAdd);
        assertEquals(expected12, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long minuendInstant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        long subtrahendInstant = new LocalDateTime(2015, 2, 28, 0, 0, 0).toInstant().getMillis();
        long expected = 1;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);

        long minuendInstant1 = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        long subtrahendInstant1 = new LocalDateTime(2015, 3, 31, 0, 0, 0).toInstant().getMillis();
        long expected1 = 2;
        long actual1 = field.getDifferenceAsLong(minuendInstant1, subtrahendInstant1);
        assertEquals(expected1, actual1);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        int monthToSet = DateTimeConstants.MARCH;
        long expected = new LocalDateTime(2015, 3, 28, 0, 0, 0).toInstant().getMillis();
        long actual = field.set(instant, monthToSet);
        assertEquals(expected, actual);

        monthToSet = DateTimeConstants.FEBRUARY;
        instant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        expected = new LocalDateTime(2015, 2, 29, 0, 0, 0).toInstant().getMillis();
        if (DateTimeConstants.isLeapYear(2015)) {
            actual = field.set(instant, monthToSet);
            assertEquals(expected, actual);
        } else {
            try {
                field.set(instant, monthToSet);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        DurationField durationField = field.getRangeDurationField();
        assertEquals(DurationField.YEARS, durationField);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2016, 2, 29, 0, 0, 0).toInstant().getMillis();
        assertTrue(field.isLeap(instant));

        instant = new LocalDateTime(2015, 2, 28, 0, 0, 0).toInstant().getMillis();
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2016, 2, 29, 0, 0, 0).toInstant().getMillis();
        int expected = 1;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);

        instant = new LocalDateTime(2015, 2, 28, 0, 0, 0).toInstant().getMillis();
        int actual1 = field.getLeapAmount(instant);
        assertEquals(0, actual1);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        DurationField durationField = field.getLeapDurationField();
        assertEquals(DurationField.DAYS, durationField);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        int expected = DateTimeConstants.DECEMBER;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDateTime(2015, 1, 31, 0, 0, 0).toInstant().getMillis();
        long expected = new LocalDateTime(2015, 1, 1, 0, 0, 0).toInstant().getMillis();
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.GREGORIAN, 0);
        long instant = new LocalDate(2015, 1, 31).toInstant().getMillis();
        long expected = 0;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);

        instant += DateTimeConstants.MILLIS_PER_DAY;
        expected = DateTimeConstants.MILLIS_PER_DAY;
        actual = field.remainder(instant);
        assertEquals(expected, actual);
    }
}