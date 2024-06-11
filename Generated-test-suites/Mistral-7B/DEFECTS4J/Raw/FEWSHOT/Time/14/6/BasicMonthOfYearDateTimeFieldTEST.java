package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertGreaterThan;
import static org.junit.Assert.assertLessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeImpl;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.JulianDays;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.FieldValueSupplier;
import org.joda.time.field.FieldValues;
import org.joda.time.field.LocalField;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final BasicChronology CHRONOLOGY = new BasicGregorianChronology();
    private static final int MAX_MONTH = CHRONOLOGY.getMaxMonth();

    /**
     * Test isLenient()
     */
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        assertFalse(field.isLenient());
    }

    /**
     * Test get(long)
     */
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        // Test valid values
        assertEquals(1, field.get(DateTimeConstants.MIN_VALUE));
        assertEquals(1, field.get(DateTimeConstants.MIN_VALUE_MILLIS));
        assertEquals(1, field.get(DateTimeConstants.MAX_VALUE));
        assertEquals(12, field.get(DateTimeConstants.MAX_VALUE_MILLIS));
        // Test invalid values
        assertNull(field.get(-1));
        assertNull(field.get(Long.MAX_VALUE));
    }

    /**
     * Test add(long, int)
     */
    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test addition to valid values
        assertEquals(2, field.get(field.add(DateTimeConstants.MIN_VALUE, 1)));
        assertEquals(13, field.get(field.add(DateTimeConstants.MAX_VALUE, 1)));

        // Test addition to invalid values
        assertNull(field.get(field.add(-1, 1)));
        assertNull(field.get(field.add(Long.MAX_VALUE, 1)));

        // Test addition with large values
        long value = 5000000000L; // 2038-01-19 12:59:59.999
        assertEquals(2, field.get(field.add(value, 1)));
        assertEquals(13, field.get(field.add(value, 12)));
    }

    /**
     * Test add(long, long)
     */
    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test addition to valid values
        assertEquals(2, field.get(field.add(DateTimeConstants.MIN_VALUE, 1L)));
        assertEquals(13, field.get(field.add(DateTimeConstants.MAX_VALUE, 1L)));

        // Test addition to invalid values
        assertNull(field.get(field.add(-1L, 1L)));
        assertNull(field.get(field.add(Long.MAX_VALUE, 1L)));

        // Test addition with large values
        long value = 50000000000L; // 2038-01-19 12:59:59.999:12:00.000
        assertEquals(2, field.get(field.add(value, 1L)));
        assertEquals(13, field.get(field.add(value, 12L)));
    }

    /**
     * Test add(ReadablePartial, int[], int, int)
     */
    @Test
    public void testAddPartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        ReadablePartial partial = FieldValues.localFieldValues(new Object[]{
                new LocalField(DateTimeFieldType.dayOfMonth(), new MyFieldValueSupplier()),
                new LocalField(DateTimeFieldType.monthOfYear(), new MyFieldValueSupplier())
        }, new Object[]{31, 2}, new DurationFieldType[]{
                DurationFieldType.DAYS, DurationFieldType.MONTHS
        }).toReadablePartial();

        int[] values = new int[]{31, 2};
        int valueToAdd = 1;

        long result = field.add(partial, 1, values, valueToAdd).getMillis();
        long expected = new BaseDateTime(CHRONOLOGY, new JulianDays(JulianDays.fromDate(2000, 2, 29).getMillis() + 86400000L)).getMillis();
        assertEquals(expected, result);
    }

    /**
     * Test addWrapField(long, int)
     */
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test wrapping around within the component
        assertEquals(1, field.get(field.addWrapField(DateTimeConstants.MIN_VALUE, 13)));
        assertEquals(14, field.get(field.addWrapField(DateTimeConstants.MIN_VALUE, -11)));
        assertEquals(1, field.get(field.addWrapField(DateTimeConstants.MAX_VALUE, -11)));
        assertEquals(13, field.get(field.addWrapField(DateTimeConstants.MAX_VALUE, 13)));

        // Test wrapping around with large values
        long value = 50000000000L;
        assertEquals(1, field.get(field.addWrapField(value, 13)));
        assertEquals(14, field.get(field.addWrapField(value, -11)));
        assertEquals(1, field.get(field.addWrapField(value, -11)));
        assertEquals(13, field.get(field.addWrapField(value, 13)));
    }

    /**
     * Test getDifferenceAsLong(long, long)
     */
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        long minuendInstant = 0;
        long subtrahendInstant = Long.MIN_VALUE;
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(12, difference);

        minuendInstant = DateTimeConstants.MIN_VALUE;
        subtrahendInstant = DateTimeConstants.MIN_VALUE_MILLIS;
        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        minuendInstant = DateTimeConstants.MAX_VALUE;
        subtrahendInstant = DateTimeConstants.MAX_VALUE_MILLIS;
        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(0, difference);

        minuendInstant = Long.MAX_VALUE;
        subtrahendInstant = Long.MAX_VALUE;
        difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(0, difference);
    }

    /**
     * Test set(long, int)
     */
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test valid values
        assertEquals(1, field.get(field.set(DateTimeConstants.MIN_VALUE, 12)));
        assertEquals(12, field.get(field.set(DateTimeConstants.MIN_VALUE_MILLIS, 12)));
        assertEquals(1, field.get(field.set(DateTimeConstants.MAX_VALUE, 1)));
        assertEquals(1, field.get(field.set(DateTimeConstants.MAX_VALUE_MILLIS, 1)));

        // Test invalid values
        assertNull(field.get(field.set(-1, 12)));
        assertNull(field.get(field.set(Long.MAX_VALUE, 12)));

        // Test large values
        long value = 50000000000L;
        assertEquals(1, field.get(field.set(value, 1)));
        assertEquals(1, field.get(field.set(value, 12)));
    }

    /**
     * Test getRangeDurationField()
     */
    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        DurationFieldTypeImpl expected = new DurationFieldTypeImpl(DurationFieldType.MONTHS, 1, null);
        assertEquals(expected, field.getRangeDurationField());
    }

    /**
     * Test isLeap(long)
     */
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test leap years
        assertTrue(field.isLeap(DateTimeConstants.MIN_VALUE));
        assertTrue(field.isLeap(DateTimeConstants.MIN_VALUE_MILLIS));
        assertTrue(field.isLeap(DateTimeConstants.MAX_VALUE));
        assertTrue(field.isLeap(DateTimeConstants.MAX_VALUE_MILLIS));

        // Test non-leap years
        assertFalse(field.isLeap(DateTimeConstants.MAX_VALUE - 400));

        // Test large values
        long value = 50000000000L;
        assertTrue(field.isLeap(value));
        assertTrue(field.isLeap(value + 400));
        assertFalse(field.isLeap(value + 401));
    }

    /**
     * Test getLeapAmount(long)
     */
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test leap years
        assertEquals(1, field.getLeapAmount(DateTimeConstants.MIN_VALUE));
        assertEquals(1, field.getLeapAmount(DateTimeConstants.MIN_VALUE_MILLIS));
        assertEquals(1, field.getLeapAmount(DateTimeConstants.MAX_VALUE));
        assertEquals(1, field.getLeapAmount(DateTimeConstants.MAX_VALUE_MILLIS));

        // Test non-leap years
        assertEquals(0, field.getLeapAmount(DateTimeConstants.MAX_VALUE - 400));

        // Test large values
        long value = 50000000000L;
        assertEquals(1, field.getLeapAmount(value));
        assertEquals(0, field.getLeapAmount(value + 401));
    }

    /**
     * Test getLeapDurationField()
     */
    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        DurationFieldTypeImpl expected = new DurationFieldTypeImpl(DurationFieldType.DAYS, 1, null);
        assertEquals(expected, field.getLeapDurationField());
    }

    /**
     * Test getMinimumValue()
     */
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        assertEquals(1, field.getMinimumValue());
    }

    /**
     * Test getMaximumValue()
     */
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);
        assertEquals(12, field.getMaximumValue());
    }

    /**
     * Test roundFloor(long)
     */
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test valid values
        assertEquals(DateTimeConstants.MIN_VALUE, field.roundFloor(DateTimeConstants.MIN_VALUE));
        assertEquals(DateTimeConstants.MAX_VALUE, field.roundFloor(DateTimeConstants.MAX_VALUE));

        // Test invalid values
        assertNull(field.roundFloor(-1));
        assertNull(field.roundFloor(Long.MAX_VALUE));

        // Test large values
        long value = 50000000000L;
        assertEquals(value, field.roundFloor(value));
    }

    /**
     * Test remainder(long)
     */
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 1);

        // Test valid values
        assertEquals(0, field.remainder(DateTimeConstants.MIN_VALUE));
        assertEquals(0, field.remainder(DateTimeConstants.MAX_VALUE));

        // Test invalid values
        assertNull(field.remainder(-1));
        assertNull(field.remainder(Long.MAX_VALUE));

        // Test large values
        long value = 50000000000L;
        assertEquals(0, field.remainder(value));
    }

    private static class MyFieldValueSupplier implements FieldValueSupplier {
        public Object getValue(LocalField field, int value) {
            return field.getValue(value);
        }
    }
}