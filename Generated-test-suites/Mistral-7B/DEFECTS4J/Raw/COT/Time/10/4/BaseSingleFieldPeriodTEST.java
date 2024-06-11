package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @org.junit.Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseInstant("2016-01-01T00:00:00.000Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2016-01-02T00:00:00.000Z");
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @org.junit.Test
    public void testBetweenPartial() {
        ReadablePartial start = new org.joda.time.local.LocalDate(2016, 1, 1);
        ReadablePartial end = new org.joda.time.local.LocalDate(2016, 1, 2);
        int period = BaseSingleFieldPeriod.between(start, end, new Period(1, 0, 0, 0));
        assertEquals(1, period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @org.junit.Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(1, 2, 3, 4, 5, 6, 7);
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L * 60L * 60L);
        assertEquals(24 * 2 + 3, hours);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}.
     */
    @org.junit.Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @org.junit.Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNotNull(period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNotNull(period.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @org.junit.Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.get(DurationFieldType.days());
        assertEquals(5, result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @org.junit.Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(new Period(5, 0, 0, 0, 0, 0, 0), result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(new MutablePeriod(5, 0, 0, 0, 0, 0, 0), result);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals(""));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @org.junit.Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
        int result1 = period1.compareTo(period2);
        assertEquals(-1, result1);
        int result2 = period1.compareTo(period3);
        assertEquals(0, result2);
    }

}