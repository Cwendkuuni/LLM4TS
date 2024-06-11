import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.NotFoundException;
import org.joda.time.test.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new JulianChronology(), DateTimeConstants.FEBRUARY);

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        int expected = DateTimeConstants.MARCH;
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNegative() {
        field.get(-1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetMaxPlusOne() {
        field.get(Long.MAX_VALUE);
    }

    @Test
    public void testAddZero() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = instant;
        long actual = field.add(instant, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddOne() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = instant + DateTimeConstants.MILLIS_PER_DAY;
        long actual = field.add(instant, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddNegativeOne() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = instant - DateTimeConstants.MILLIS_PER_DAY;
        long actual = field.add(instant, -1);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddMaxValue() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = Long.MAX_VALUE;
        long actual = field.add(instant, iMax);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeMaxValue() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        field.add(instant, -(iMax + 1));
    }

    @Test
    public void testAddWrapField() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = BaseDateTime.UNIX_UTC.getMillis() - 1000 + (iMax * DateTimeConstants.MILLIS_PER_DAY) - DateTimeConstants.MILLIS_PER_DAY;
        long actual = field.addWrapField(instant, iMax + 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long subtrahendInstant = BaseDateTime.UNIX_UTC.getMillis() - 2000; // arbitrary instant
        long expected = ((DateTimeConstants.MAX_YEAR - DateTimeConstants.MIN_YEAR + 1) * iMax + iMax - 1);
        long actual = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        int month = iMax + 1;
        long expected = iChronology.getYearMonthMillis(DateTimeConstants.MAX_YEAR, month);
        long actual = field.set(instant, month);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidMonth() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        int month = iMax + 2;
        field.set(instant, month);
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField actual = field.getRangeDurationField();
        assertNotNull(actual);
        assertEquals(DateTimeFieldType.years(), actual.getFieldType());
    }

    @Test
    public void testIsLeap() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        int expected = iLeapMonth == DateTimeConstants.FEBRUARY ? 1 : 0;
        int actual = field.isLeap(instant) ? 1 : 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        int expected = field.isLeap(instant) ? 1 : 0;
        int actual = field.getLeapAmount(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField actual = field.getLeapDurationField();
        assertNotNull(actual);
        assertEquals(DateTimeFieldType.days(), actual.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLeapDurationFieldInvalidDateTime() {
        field.getLeapDurationField();
    }

    @Test
    public void testGetMinimumValue() {
        int expected = MIN;
        int actual = field.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        int expected = iMax;
        int actual = field.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = iChronology.getYearMonthMillis(iChronology.getYear(instant), iChronology.getMonthOfYear(instant, iChronology.getYear(instant)));
        long actual = field.roundFloor(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = instant - field.roundFloor(instant);
        long actual = field.remainder(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerialization() {
        long instant = BaseDateTime.UNIX_UTC.getMillis() - 1000; // arbitrary instant
        long expected = field.set(instant, iMax).getMillis();
        field.writeResolve();
        field = (BasicMonthOfYearDateTimeField) TestBase.getObjectFromStream(TestBase.getByteArrayInputStream(TestBase.serialize(field)));
        long actual = field.getMillis();
        assertEquals(expected, actual);
    }
}