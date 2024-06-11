package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.field.LocalMonth;
import org.joda.time.test.TestBase;

/**
 * Test class for {@link BasicMonthOfYearDateTimeField}.
 */
public class BasicMonthOfYearDateTimeFieldTest extends TestBase {

    @Test
    public void testNewInstance() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(gregorian, field.getChronology());
        assertEquals(31, field.getMaximumValue());
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        int month = field.get(instant);
        assertEquals(2, month);
    }

    @Test
    public void testAdd() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        long result = field.add(instant, 1);
        long expected = gregorian.getMillis("2005-03-28T12:34:56.789Z");
        assertEquals(expected, result);

        instant = gregorian.getMillis("2005-12-31T12:34:56.789Z");
        result = field.add(instant, -2);
        expected = gregorian.getMillis("2005-11-30T12:34:56.789Z");
        assertEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("1999-12-31T12:34:56.789Z");
        long result = field.addWrapField(instant, 12);
        long expected = gregorian.getMillis("2000-01-31T12:34:56.789Z");
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant1 = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        long instant2 = gregorian.getMillis("2005-03-31T12:34:56.789Z");
        long result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(30, result);

        instant1 = gregorian.getMillis("2005-11-30T12:34:56.789Z");
        instant2 = gregorian.getMillis("2005-12-31T12:34:56.789Z");
        result = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(-31, result);
    }

    @Test
    public void testSet() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        long result = field.set(instant, 3);
        long expected = gregorian.getMillis("2005-03-28T12:34:56.789Z");
        assertEquals(expected, result);

        instant = gregorian.getMillis("2005-02-29T12:34:56.789Z"); // leap year
        result = field.set(instant, 2);
        expected = gregorian.getMillis("2005-01-29T12:34:56.789Z"); // leap year
        assertEquals(expected, result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        DurationField range = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), range.getFieldType());
        assertEquals(field, range.getField());
    }

    @Test
    public void testIsLeap() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        assertFalse(field.isLeap(instant));

        instant = gregorian.getMillis("2000-02-29T12:34:56.789Z"); // leap year
        assertTrue(field.isLeap(instant));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long instant = gregorian.getMillis("2005-02-28T12:34:56.789Z");
        assertNull(field.getLeapAmount(instant));

        instant = gregorian.getMillis("2000-02-29T12:34:56.789Z"); // leap year
        assertEquals(1, field.getLeapAmount(instant));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        DurationField leap = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leap.getFieldType());
        assertEquals(field, leap.getField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        long instant1 = gregorian.getMillis("2005-03-01T12:34:56.789Z");
        long instant2 = gregorian.getMillis("2005-03-02T12:34:56.789Z");
        long instant3 = gregorian.getMillis("2005-03-01T13:34:56.789Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long floor1 = field.roundFloor(instant1);
        long floor2 = field.roundFloor(instant2);
        long floor3 = field.roundFloor(instant3);
        assertEquals(instant1, floor1);
        assertEquals(instant1, floor2);
        assertEquals(instant1, floor3);
    }

    @Test
    public void testRemainder() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        long instant = gregorian.getMillis("2005-03-01T12:34:56.789Z");
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long remainder = field.remainder(instant);
        assertEquals(0, remainder);

        instant = gregorian.getMillis("2005-03-01T13:34:56.789Z");
        remainder = field.remainder(instant);
        assertEquals(0, remainder);

        instant = gregorian.getMillis("2005-02-29T12:34:56.789Z"); // leap year
        remainder = field.remainder(instant);
        assertEquals(0, remainder);
    }

    @Test
    public void testRoundTrip() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        long instant = gregorian.getMillis("2005-01-31T12:34:56.789Z"); // leap year
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        long roundTrip = field.set(instant, 1).getMillis();
        assertEquals(instant, roundTrip);
    }

    @Test
    public void testSerializable() {
        BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(field, field.readResolve());
    }

    @Test
    public void testNewInstanceWithLeapMonthsInWrongOrder() {
        // Testing a constructor with leap month specified in wrong order.
        // This should throw an exception as it is not supported.
        try {
            BasicChronology gregorian = DateTimeUtils.getLocalGregorianChronology();
            BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.FEBRUARY);
            fail();
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

}