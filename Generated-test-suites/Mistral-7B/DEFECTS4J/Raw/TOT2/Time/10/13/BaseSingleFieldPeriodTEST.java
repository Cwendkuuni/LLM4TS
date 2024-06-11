package org.joda.time.base;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests constructor with valid inputs.
     */
    @Test
    public void testConstructorValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
        assertEquals(5, period.getValue());
    }

    /**
     * Tests getFieldType() method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Tests getPeriodType() method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Tests size() method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    /**
     * Tests getValue() method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Tests equals() method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertFalse(period1.equals(period3));

        Object obj = new Object();
        assertFalse(period1.equals(obj));
    }

    /**
     * Tests hashCode() method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period3.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    /**
     * Tests compareTo() method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        int result1 = period1.compareTo(period2);
        assertTrue(result1 > 0);

        int result2 = period1.compareTo(period3);
        assertEquals(0, result2);
    }

    /**
     * Tests get() method with valid inputs.
     */
    @Test
    public void testGetValid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        int result = period.get(fieldType);
        assertEquals(5, result);
    }

    /**
     * Tests get() method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalid() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType invalidFieldType = new DurationFieldType("INVALID");
        period.get(invalidFieldType);
    }

    /**
     * Tests isSupported() method.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    /**
     * Tests toPeriod() method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expected = new Period(5, PeriodUnit.DAYS);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests toMutablePeriod() method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod(5, PeriodUnit.DAYS);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    /**
     * Tests between() method with valid inputs.
     */
    @Test
    public void testBetweenValid() {
        long startMillis = DateTimeUtils.currentMillis();
        ReadableInstant start = DateTimeUtils.toInstant(startMillis, DateTimeUtils.UTC);
        long endMillis = startMillis + 24 * 60 * 60 * 1000;
        ReadableInstant end = DateTimeUtils.toInstant(endMillis, DateTimeUtils.UTC);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);

        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    /**
     * Tests between() method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalid() {
        ReadableInstant start = null;
        ReadableInstant end = new ReadableInstant();
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
    }

    /**
     * Tests standardPeriodIn() method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(5, PeriodUnit.DAYS);
        long millisPerUnit = 86400000L;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(5, result);

        period = new Period(5, PeriodUnit.MONTHS);
        try {
            result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}