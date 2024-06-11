package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.TrueChronology;

import java.io.Serializable;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)} .
     */
    @org.junit.Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()} .
     */
    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)} .
     */
    @org.junit.Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()} .
     */
    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()} .
     */
    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()} .
     */
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)} .
     */
    @org.junit.Test
    public void testGetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
        try {
            period.getValue(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)} .
     */
    @org.junit.Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        try {
            period.getFieldType(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)} .
     */
    @org.junit.Test
    public void testBetweenInstantInstant() {
        BaseDateTime start = new BaseDateTime(1526352000000L, ISOChronology.getInstanceUTC());
        BaseDateTime end = new BaseDateTime(1526355200000L, ISOChronology.getInstanceUTC());
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(30, period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)} .
     */
    @org.junit.Test
    public void testBetweenPartialPartial() {
        MutablePeriod period = new MutablePeriod();
        period.setField(DurationFieldType.DAYS, 3);
        ReadablePeriod zeroInstance = Period.ZERO;
        int between = BaseSingleFieldPeriod.between(period.toReadablePartial(), zeroInstance.toReadablePartial(), zeroInstance);
        assertEquals(3, between);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)} .
     */
    @org.junit.Test
    public void testStandardPeriodInPeriodLong() {
        MutablePeriod period = new MutablePeriod();
        period.setField(DurationFieldType.HOURS, 2);
        period.setField(DurationFieldType.MINUTES, 30);
        int standardPeriodIn
                = BaseSingleFieldPeriod.standardPeriodIn(period, FieldUtils.ONE_HOUR.getMillis());

        assertEquals(3, standardPeriodIn);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)} .
     */
    @org.junit.Test
    public void testEqualsObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = period1;
        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertTrue(period1.equals(period4));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()} .
     */
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        int hashCode3 = period3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(Object)} .
     */
    @org.junit.Test
    public void testCompareToObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5);
        assertEquals(0, period1.compareTo(period1));
        assertEquals(0, period1.compareTo(period2));
        assertEquals(-1, period2.compareTo(period1));
        assertEquals(1, period1.compareTo(period3));
        assertEquals(0, period1.compareTo(period4));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()} .
     */
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(PeriodType.standard(), result.getPeriodType());
        assertEquals(5, result.getFields(DurationFieldType.DAYS)[0]);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()} .
     */
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(5, result.getField(DurationFieldType.DAYS));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(ReadablePeriod)} .
     */
    @org.junit.Test
    public void testEqualsReadablePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        ReadablePeriod period4 = new BaseSingleFieldPeriod(5).toPeriod();
        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertTrue(period1.equals(period4));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toSerializable()} .
     */
    @org.junit.Test
    public void testToSerializable() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Serializable serializable = period.toSerializable();
        assertNotNull(serializable);
    }

    /**
     * Test method for negative edge case - between with end < start.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeBetweenInstantInstant() {
        BaseDateTime start = new BaseDateTime(1526352000000L, ISOChronology.getInstanceUTC());
        BaseDateTime end = new BaseDateTime(1526351999999L, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
    }

    /**
     * Test method for invalid edge case - between with zero length period.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidBetweenPartialPartial() {
        ReadablePeriod zeroInstance = Period.ZERO;
        BaseSingleFieldPeriod.between(new MutablePeriod(), zeroInstance, zeroInstance);
    }

    /**
     * Test method for invalid edge case - standardPeriodIn with zero length period.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardPeriodInPeriodLong() {
        MutablePeriod period = new MutablePeriod();
        BaseSingleFieldPeriod.standardPeriodIn(period, 0L);
    }

    /**
     * Test method for null edge case - getFieldType.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(null);
    }

    /**
     * Test method for null edge case - size.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.size(null);
    }

    /**
     * Test method for null edge case - getValue.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(null);
    }

    /**
     * Test method for null edge case - getValueInt.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullGetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValueInt(null);
    }

    /**
     * Test method for null edge case - getFieldTypeInt.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldTypeInt(null);
    }

    /**
     * Test method for index out of bounds edge case - getValue.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    /**
     * Test method for index out of bounds edge case - getFieldType.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    /**
     * Test method for index out of bounds edge case - getValueInt.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIntIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValueInt(2);
    }

    /**
     * Test method for index out of bounds edge case - getFieldTypeInt.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeIntIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldTypeInt(2);
    }

    /**
     * Test method for incorrect type edge case - equals.
     */
    @org.junit.Test
    public void testIncorrectTypeEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = "not a period";
        assertFalse(period1.equals(period2));
    }

    /**
     * Test method for incorrect type edge case - hashCode.
     */
    @org.junit.Test
    public void testIncorrectTypeHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = "not a period";
        assertNotEquals(period1.hashCode(), period2.hashCode());
    }

    /**
     * Test method for incorrect type edge case - compareTo.
     */
    @org.junit.Test
    public void testIncorrectTypeCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = "not a period";
        assertFalse(period1.equals(period2));
        try {
            period1.compareTo(period2);
            fail();
        } catch (ClassCastException e) {
            // expected
        }
    }

    /**
     * Test method for incorrect type edge case - toPeriod.
     */
    @org.junit.Test
    public void testIncorrectTypeToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Object result = period.toPeriod();
        assertFalse(result instanceof Period);
    }

    /**
     * Test method for incorrect type edge case - toMutablePeriod.
     */
    @org.junit.Test
    public void testIncorrectTypeToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Object result = period.toMutablePeriod();
        assertFalse(result instanceof MutablePeriod);
    }
}