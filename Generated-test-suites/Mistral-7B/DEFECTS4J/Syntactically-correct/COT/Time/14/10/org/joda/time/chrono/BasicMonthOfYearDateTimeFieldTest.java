import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Millisecond;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicConstruction() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        assertNotNull(field);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals(-8258715387168736L, field.getSerialVersionUID());
        assertEquals(chronology, field.getChronology());
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO-8601-1997", new Locale("en", "US")), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        int expected = 1;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-29T12:00:00.000Z").toInstant().getMillis();
        int expected = 1;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidMonths() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        field.add(instant, -13);
    }

    @Test
    public void testAdd() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2012-02-28T12:00:00.000Z").toInstant().getMillis();
        long actual = field.add(instant, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWithLeapYear() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-29T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2012-03-01T12:00:00.000Z").toInstant().getMillis();
        long actual = field.add(instant, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWithNegativeMonths() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2012-01-30T12:00:00.000Z").toInstant().getMillis();
        long actual = field.add(instant, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWithNegativeLeapMonths() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-28T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2012-02-27T12:00:00.000Z").toInstant().getMillis();
        long actual = field.add(instant, -1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithTooManyMonths() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        field.add(instant, 123456);
    }

    @Test
    public void testAddWrapField() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2011-12-31T12:00:00.000Z").toInstant().getMillis();
        long actual = field.addWrapField(instant, 12);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long minuendInstant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long subtrahendInstant = LocalDateTime.parse("2012-02-28T12:00:00.000Z").toInstant().getMillis();
        long expected = 1;
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        int month = 2;
        long expected = LocalDateTime.parse("2012-02-28T12:00:00.000Z").toInstant().getMillis();
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidMonth() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        int month = 13;
        long expected = instant;
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRangeDurationField() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        DurationField expected = new Millisecond();
        DurationField actual = field.getRangeDurationField();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeap() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-29T12:00:00.000Z").toInstant().getMillis();
        boolean expected = true;
        boolean actual = field.isLeap(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeapWithNonLeapYear() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.FEBRUARY);
        long instant = LocalDateTime.parse("2012-02-28T12:00:00.000Z").toInstant().getMillis();
        boolean expected = false;
        boolean actual = field.isLeap(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO-8601-1997", new Locale("en", "US")), DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.JANUARY;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("ISO-8601-1997", new Locale("en", "US")), DateTimeConstants.JANUARY);
        int expected = DateTimeConstants.DECEMBER;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long expected = LocalDateTime.parse("2012-01-01T00:00:00.000Z").toInstant().getMillis();
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:00:00.000Z").toInstant().getMillis();
        long expected = 0L;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainderWithNonZeroValue() {
        Chronology chronology = new BasicChronology("ISO-8601-1997", new Locale("en", "US"));
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        long instant = LocalDateTime.parse("2012-01-31T12:01:00.001Z").toInstant().getMillis();
        long expected = 1L;
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }
}