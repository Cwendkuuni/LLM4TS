import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology chronology = new ISOChronology();
        BasicMonthOfYearDateTimeField field = (BasicMonthOfYearDateTimeField) BasicMonthOfYearDateTimeField.getInstance(chronology, DateTimeConstants.JANUARY);
        assertEquals("Wrong chronology", chronology, field.getChronology());
        assertEquals("Wrong leap month", DateTimeConstants.JANUARY, field.getLeapMonth());
    }

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertFalse("Should not be lenient", field.isLenient());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNegative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        field.get(-1);
    }

    @Test
    public void testGetZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertEquals("Wrong month for zero", DateTimeConstants.JANUARY, field.get(0));
    }

    @Test
    public void testGetValidPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertEquals("Wrong month for positive", DateTimeConstants.FEBRUARY, field.get(1));
        assertEquals("Wrong month for positive (large)", DateTimeConstants.DECEMBER, field.get(11));
    }

    @Test
    public void testGetInvalidPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        try {
            field.get(13);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Expected exception", "Value is out of bounds (Min=1, Max=12)", e.getMessage());
        }
    }

    @Test
    public void testGetWithLeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        assertEquals("Wrong month for February in leap year", DateTimeConstants.FEBRUARY, field.get(0));
        assertEquals("Wrong month for February in leap year (large)", DateTimeConstants.MARCH, field.get(1));
        assertEquals("Wrong month for February in leap year (large, large)", DateTimeConstants.MARCH, field.get(59));
    }

    @Test
    public void testAddZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.add(original, 0);
        assertEquals("Addition of zero should not change value", original, result);
    }

    @Test
    public void testAddOne() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.add(original, 1);
        assertEquals("Addition should increment month", DateTimeConstants.FEBRUARY, field.get(result));
    }

    @Test
    public void testAddNegative() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.FEBRUARY);
        long original = field.get(0);
        long result = field.add(original, -1);
        assertEquals("Addition should decrement month", DateTimeConstants.JANUARY, field.get(result));
    }

    @Test
    public void testAddValidLarge() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.add(original, 11);
        assertEquals("Addition should increment month by large amount", DateTimeConstants.DECEMBER, field.get(result));
    }

    @Test
    public void testAddInvalidLarge() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.add(original, 13);
        assertEquals("Addition should result in out of bounds value", Long.MIN_VALUE, result);
    }

    @Test
    public void testAddWithLeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        long original = field.get(0);
        long result = field.add(original, 1);
        assertEquals("Addition in leap year should increment month", DateTimeConstants.MARCH, field.get(result));
        result = field.add(original, 58);
        assertEquals("Addition in leap year should increment month by large amount", DateTimeConstants.SEPTEMBER, field.get(result));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.addWrapField(original, 13);
        assertEquals("AddWrapField should wrap around", DateTimeConstants.MARCH, field.get(result));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field1 = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long instant1 = field1.get(0);
        BasicMonthOfYearDateTimeField field2 = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.FEBRUARY);
        long instant2 = field2.get(0);
        long difference = field1.getDifferenceAsLong(instant1, instant2);
        assertEquals("Difference should be positive for earlier instance", 1, difference);
        difference = field2.getDifferenceAsLong(instant2, instant1);
        assertEquals("Difference should be negative for later instance", -1, difference);
    }

    @Test
    public void testSetZero() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        long result = field.set(original, DateTimeConstants.JANUARY);
        assertEquals("Setting to same month should not change value", original, result);
    }

    @Test
    public void testSetValidPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        LocalDateTime dateTime = new LocalDateTime(new LocalDate(2012, 2, 1), new LocalTime());
        long instant = dateTime.toDateTimeInMillis();
        long result = field.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals("Setting to new month should return new value", instant + FieldUtils.getMillisPerDay(field.getChronology()), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidPositive() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long original = field.get(0);
        LocalDateTime dateTime = new LocalDateTime(new LocalDate(2012, 13, 1), new LocalTime());
        long instant = dateTime.toDateTimeInMillis();
        field.set(instant, DateTimeConstants.JANUARY + 2);
    }

    @Test
    public void testSetWithLeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        long original = field.get(0);
        LocalDateTime dateTime = new LocalDateTime(new LocalDate(2012, 2, 29), new LocalTime());
        long instant = dateTime.toDateTimeInMillis();
        long result = field.set(instant, DateTimeConstants.MARCH);
        assertEquals("Setting to new month in leap year should return new value", instant + FieldUtils.getMillisPerDay(field.getChronology()), result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        DurationField range = field.getRangeDurationField();
        assertEquals("Range duration field type should be years", DurationFieldType.YEARS, range.getType());
        long duration = range.getIsogrammicValue();
        assertEquals("Range duration value should be 12", 12, duration);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertFalse("January is not a leap month", field.isLeap(0));
        BasicMonthOfYearDateTimeField leapField = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        assertTrue("February in leap year is a leap month", leapField.isLeap(0));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertEquals("Leap amount for non leap month should be zero", 0, field.getLeapAmount(0));
        BasicMonthOfYearDateTimeField leapField = new BasicMonthOfYearDateTimeField(new BasicGregorianChronology(), DateTimeConstants.FEBRUARY);
        assertEquals("Leap amount for leap month should be one", 1, field.getLeapAmount(0));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        DurationField leap = field.getLeapDurationField();
        assertEquals("Leap duration field type should be days", DurationFieldType.DAYS, leap.getType());
        long duration = leap.getIsogrammicValue();
        assertEquals("Leap duration value should be 1", 1, duration);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertEquals("Minimum value should be January", DateTimeConstants.JANUARY, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        assertEquals("Maximum value should be December", DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = 0;
        long result = field.roundFloor(instant);
        assertEquals("Round floor should return year month millis", 0, result);
        instant -= FieldUtils.getMillisPerDay(field.getChronology()) + 1;
        result = field.roundFloor(instant);
        assertEquals("Round floor should return previous year month millis", result, instant);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        long instant = 0;
        long result = field.remainder(instant);
        assertEquals("Remainder should be zero", 0, result);
        instant += FieldUtils.getMillisPerDay(field.getChronology()) - 1;
        result = field.remainder(instant);
        assertEquals("Remainder should be remainder of previous day", result, FieldUtils.getMillisPerDay(field.getChronology()) - 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInvalidField() {
        BaseDateTime base = new BaseDateTime();
        base.setField(DateTimeFieldType.MILLI_SECONDS, 0);
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
        field.add(base, DateTimeFieldType.MILLI_SECONDS, 1, 1);
    }
}