package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.util.BigMicro;

import java.io.Serializable;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @org.junit.Test
    public void testBetween() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(1972, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(1972, 1, 2, 0, 0, 0, 0, chrono).toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @org.junit.Test
    public void testBetween2() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadablePartial start = new org.joda.time.LocalDate(1972, 1, 1).toReadablePartial();
        ReadablePartial end = new org.joda.time.LocalDate(1972, 1, 2).toReadablePartial();
        ReadablePeriod zeroInstance = Period.ZERO;
        int result = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(1, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @org.junit.Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("PT1H");
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60 * 60 * 24L);
        assertEquals(1, result);
    }

    /**
     * Test constructor {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}.
     */
    @org.junit.Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @org.junit.Test
    public void testValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @org.junit.Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType expected = DurationFieldType.DAYS;
        assertEquals(expected, period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        PeriodType expected = PeriodType.standard();
        assertEquals(expected, period.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        int expected = 1;
        assertEquals(expected, period.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getFieldType(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.getValue(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @org.junit.Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int result = period.get(fieldType);
        assertEquals(1, result);

        fieldType = DurationFieldType.HOURS;
        result = period.get(fieldType);
        assertEquals(0, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @org.junit.Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertTrue(period.isSupported(fieldType));

        fieldType = DurationFieldType.HOURS;
        assertFalse(period.isSupported(fieldType));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        Period result = period.toPeriod();
        assertEquals(1, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(1, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        assertTrue(period1.equals(period2));

        Object other = new Object();
        assertFalse(period1.equals(other));

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        assertFalse(period1.equals(period3));

        ReadablePeriod period4 = Period.parse("PT1H");
        assertFalse(period1.equals(period4));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        assertEquals(period1.hashCode(), period2.hashCode());

        Object other = new Object();
        int hashCodeOther = other.hashCode();
        int hashCodePeriod1 = period1.hashCode();
        assertEquals(hashCodePeriod1, hashCodeOther); // should not happen

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);
        int hashCodePeriod3 = period3.hashCode();
        assertNotEquals(hashCodePeriod1, hashCodePeriod3);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @org.junit.Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);

        period2.setValue(1);
        result = period1.compareTo(period2);
        assertEquals(0, result);

        period1.setValue(2);
        result = period1.compareTo(period2);
        assertEquals(1, result);
    }

    /**
     * Test edge cases.
     */
    @org.junit.Test
    public void testEdgeCases() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, period.getValue());

        period = new BaseSingleFieldPeriod(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, period.getValue());
    }

    /**
     * Test exception handling.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        period.setValue(-1);
    }
}