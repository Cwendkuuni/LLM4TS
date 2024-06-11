import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGetMinMaxValues() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void testGetDefaultValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.getValue(0));
    }

    @Test
    public void testGetFieldType() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
    }

    @Test
    public void testGetAverageMillisPerDay() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(31L * DateTimeConstants.MILLIS_PER_DAY, field.getAverageMillisPerDay());
    }

    @Test
    public void testGetLeapMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.getLeapMonth());
    }

    @Test
    public void testGetInstance() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        assertEquals(field, BasicMonthOfYearDateTimeField.getInstance(new BasicChronology("UTC"), DateTimeConstants.JANUARY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLeapMonth() {
        new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLeapMonthBeyondMaxValue() {
        new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY + 1);
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        assertEquals(DateTimeConstants.JANUARY, field.get(instant));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.add(instant, 1);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));
    }

    @Test
    public void testAddNegativeMonths() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.add(instant, -1);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
    }

    @Test
    public void testAddLargeNumberOfMonths() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.add(instant, Integer.MAX_VALUE);
        assertEquals(DateTimeConstants.DECEMBER, field.get(result));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.addWrapField(instant, 13);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long minuendInstant = BaseDateTime.UTC.getMillis();
        long subtrahendInstant = BaseDateTime.UTC.getMillis() + 31 * DateTimeConstants.MILLIS_PER_MONTH;
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        field.set(instant, DateTimeConstants.JANUARY - 1);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        DurationField durationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), durationField.getFieldType());
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        assertFalse(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.FEBRUARY);
        long instant = BaseDateTime.UTC.getMillis();
        assertEquals(FieldUtils.isLeapYear(iChronology.getYear(instant)) ? 1 : 0, field.getLeapAmount(instant));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.FEBRUARY);
        DurationField durationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), durationField.getFieldType());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.roundFloor(instant);
        assertEquals(iChronology.getYearMonthMillis(iChronology.getYear(instant), iChronology.getMonthOfYear(instant)), result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology("UTC"), DateTimeConstants.JANUARY);
        long instant = BaseDateTime.UTC.getMillis();
        long result = field.remainder(instant);
        assertEquals(instant % (iChronology.getMillisPerDay() * iMax), result);
    }
}