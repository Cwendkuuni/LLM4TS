package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.joda.time.BaseSingleFieldPeriod;
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
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
    }

    /**
     * Test getting the value of a supported duration field.
     */
    @Test
    public void testGetSupportedFieldValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.DAYS));
        assertEquals(0, period.get(DurationFieldType.HOURS));
    }

    /**
     * Test getting the value of an unsupported duration field.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnsupportedFieldValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.get(DurationFieldType.HOURS);
    }

    /**
     * Test that the period is immutable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableField() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(6);
    }

    /**
     * Test converting to a Period object.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period p = period.toPeriod();
        assertEquals(5, p.getYears());
        assertEquals(0, p.getMonths());
        assertEquals(0, p.getDays());
        assertEquals(0, p.getHours());
        assertEquals(0, p.getMinutes());
        assertEquals(0, p.getSeconds());
        assertEquals(0, p.getMillis());
    }

    /**
     * Test converting to a MutablePeriod object.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(5, mp.getStandardValue(DurationFieldType.DAYS));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNull(period1.equals(period4));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;

        int hash1 = period1.hashCode();
        int hash2 = period2.hashCode();
        int hash3 = period3.hashCode();
        int hash4 = period4.hashCode();

        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertNull(period1.hashCode());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        assertEquals(0, period1.compareTo(period1));
        assertEquals(1, period1.compareTo(period2));
        assertEquals(-1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(period3));
    }

    /**
     * Test between method with Instant objects.
     */
    @Test
    public void testBetweenInstant() {
        Instant start = Instant.now();
        Instant end = start.plus(Duration.ofDays(5));

        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(5, result);
    }

    /**
     * Test between method with ReadableInstant objects.
     */
    @Test
    public void testBetweenReadableInstant() {
        ReadableInstant start = ISOChronology.getInstanceUTC().instant();
        ReadableInstant end = start.plus(new Duration(5, ChronoUnit.DAYS));

        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(5, result);
    }

    /**
     * Test between method with ReadablePartial objects.
     */
    @Test
    public void testBetweenReadablePartial() {
        ReadablePeriod zero = Period.ZERO;

        ReadablePartial start = new MutablePeriod(zero, ChronoUnit.DAYS, 0).toReadablePartial();
        ReadablePartial end = start.plus(Duration.ofDays(5));

        int result = BaseSingleFieldPeriod.between(start, end, zero.getPeriodType());
        assertEquals(5, result);
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod(PeriodType.standard(), 5, DurationFieldType.DAYS);

        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1000 * 60 * 60 * 24);
        assertEquals(5, result);
    }
}