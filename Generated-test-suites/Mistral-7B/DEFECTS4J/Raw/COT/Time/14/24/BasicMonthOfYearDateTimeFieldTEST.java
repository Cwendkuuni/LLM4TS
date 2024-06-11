import org.joda.time.BaseTestCase;
import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.chrono.BasicGregorianChronology;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    private static final BasicChronology CHRONOLOGY = new BasicGregorianChronology();

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T00:00:00.000Z").getMillis();
        int expected = DateTimeConstants.JANUARY;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T00:00:00.000Z").getMillis();
        int months = 1;
        long expected = LocalDateTime.parse("2012-02-29T00:00:00.000Z").getMillis();
        long actual = field.add(instant, months);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-12-31T00:00:00.000Z").getMillis();
        int months = 1;
        long expected = LocalDateTime.parse("2013-01-31T00:00:00.000Z").getMillis();
        long actual = field.addWrapField(instant, months);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long minuendInstant = LocalDateTime.parse("2012-01-31T00:00:00.000Z").getMillis();
        long subtrahendInstant = LocalDateTime.parse("2011-12-31T00:00:00.000Z").getMillis();
        int expected = 11;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T00:00:00.000Z").getMillis();
        int month = DateTimeConstants.FEBRUARY;
        long expected = LocalDateTime.parse("2012-02-29T00:00:00.000Z").getMillis();
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T00:00:00.000Z").getMillis();
        int month = -1;
        field.set(instant, month);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        DurationField expected = new org.joda.time.Days(30);
        DurationField actual = field.getRangeDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-29T00:00:00.000Z").getMillis();
        assertTrue(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-29T00:00:00.000Z").getMillis();
        int expected = 1;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.FEBRUARY);
        DurationField expected = new org.joda.time.Days(29);
        DurationField actual = field.getLeapDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.DECEMBER;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDate.parse("2012-01-31").toMillis();
        long expected = LocalDate.parse("2012-01-01").toMillis();
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = LocalDate.parse("2012-01-31").toMillis();
        long expected = 0;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapFieldWithContiguousPartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        ReadablePartial partial = new LocalDate().toDateTimeAtStartOfDay().toLocalDate().toLocalDateTime().toLocalDateTime().toDateTime();
        int fieldIndex = ImpreciseDateTimeField.MONTH_OF_YEAR;
        int[] values = new int[]{DateTimeConstants.FEBRUARY};
        int valueToAdd = 1;
        int[] expectedValues = new int[]{DateTimeConstants.MARCH};
        int[] actualValues = field.add(partial, fieldIndex, values, valueToAdd);
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldWithNonContiguousPartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);
        ReadablePartial partial = new LocalTime().toLocalDateTime();
        int fieldIndex = ImpreciseDateTimeField.MONTH_OF_YEAR;
        int[] values = new int[]{DateTimeConstants.FEBRUARY};
        int valueToAdd = 1;
        field.add(partial, fieldIndex, values, valueToAdd);
        fail("Expected IllegalArgumentException");
    }
}