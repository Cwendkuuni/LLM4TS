import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.YearMonth;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology CHRONOLOGY = new BasicChronology("TestChronology", DateTimeConstants.MIN_YEAR, DateTimeConstants.MAX_YEAR);
    private static final ImpreciseDateTimeField MONTH_FIELD = new BasicMonthOfYearDateTimeField(CHRONOLOGY, DateTimeConstants.JANUARY);

    @Test
    public void testIsLenient() {
        assertFalse(MONTH_FIELD.isLenient());
    }

    @Test
    public void testGet() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        int expected = 3;
        int actual = MONTH_FIELD.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testAdd() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long expected = LocalDateTime.parse("2011-04-30T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long actual = MONTH_FIELD.add(instant, 12);
        assertEquals(expected, actual);

        expected = LocalDateTime.parse("2009-02-29T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        actual = MONTH_FIELD.add(instant, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        long instant = LocalDateTime.parse("2010-12-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long expected = LocalDateTime.parse("2010-01-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long actual = MONTH_FIELD.addWrapField(instant, 1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTooManyMonths() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        MONTH_FIELD.add(instant, 13);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long instant2 = LocalDateTime.parse("2011-04-30T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long expected = 12 * (long) MONTH_FIELD.getMaximumValue();
        long actual = MONTH_FIELD.getDifferenceAsLong(instant1, instant2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        int month = 5;
        long expected = LocalDateTime.parse("2010-05-30T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long actual = MONTH_FIELD.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        int month = 13;
        MONTH_FIELD.set(instant, month);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField expected = DurationFieldType.years();
        DurationField actual = MONTH_FIELD.getRangeDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        long instant = LocalDateTime.parse("2012-02-29T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        assertTrue(MONTH_FIELD.isLeap(instant));

        instant = LocalDateTime.parse("2011-02-28T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        assertFalse(MONTH_FIELD.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        long instant = LocalDateTime.parse("2012-02-29T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        int expected = 1;
        int actual = MONTH_FIELD.getLeapAmount(instant);
        assertEquals(expected, actual);

        instant = LocalDateTime.parse("2011-02-28T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        actual = MONTH_FIELD.getLeapAmount(instant);
        assertEquals(0, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField expected = DurationFieldType.days();
        DurationField actual = MONTH_FIELD.getLeapDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        int expected = DateTimeConstants.JANUARY;
        int actual = MONTH_FIELD.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = CHRONOLOGY.getMaxMonth();
        int actual = MONTH_FIELD.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long expected = LocalDateTime.parse("2010-03-01T00:00:00.000".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long actual = MONTH_FIELD.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long instant = LocalDateTime.parse("2010-03-31T23:59:59.999".toLocaleString(Locale.US), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toInstant().getMillis();
        long expected = instant % MONTH_FIELD.getDurationField().getMillisPerValue();
        long actual = MONTH_FIELD.remainder(instant);
        assertEquals(expected, actual);
    }
}