package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
import org.junit.Test;

/**
 * Test file for class BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        assertEquals(1, period.getValue());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseDays();
        assertEquals(DurationFieldType.dayOfMonth(), period.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseDays();
        assertEquals(PeriodType.dayTime(), period.getPeriodType());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseDays();
        assertEquals(1, period.size());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseDays(7);
        assertEquals(7, period.getValue());
    }

    /**
     * Test setValue method.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseDays();
        period.setValue(3);
        assertEquals(3, period.getValue());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseDays(1);
        BaseSingleFieldPeriod period2 = new BaseDays(1);
        assertTrue(period1.equals(period2));

        period2 = new BaseDays(2);
        assertFalse(period1.equals(period2));

        period1 = null;
        assertFalse(period2.equals(null));

        period2 = null;
        assertFalse(period1.equals(period2));

        ReadablePeriod period3 = new Period(1, DurationFieldType.dayOfMonth(), PeriodType.dayTime());
        assertFalse(period1.equals(period3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseDays(1);
        BaseSingleFieldPeriod period2 = new BaseDays(1);
        assertEquals(period1.hashCode(), period2.hashCode());

        period2 = new BaseDays(2);
        assertNotEquals(period1.hashCode(), period2.hashCode());

        period1 = null;
        assertNotEquals(period2.hashCode(), period1.hashCode());

        period2 = null;
        assertNotEquals(period1.hashCode(), period2.hashCode());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseDays(1);
        BaseSingleFieldPeriod period2 = new BaseDays(2);
        assertTrue(period1.compareTo(period1) == 0);
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertTrue(period1.compareTo(period1) == 0);
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseDays(1);
        Period result = period.toPeriod();
        assertEquals(DurationFieldType.dayOfMonth(), result.getFieldType(0));
        assertEquals(1, result.getValue(0));
        assertEquals(PeriodType.dayTime(), result.getPeriodType());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseDays(1);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(DurationFieldType.dayOfMonth(), result.getFieldType(0));
        assertEquals(1, result.getValue(0));
        assertEquals(PeriodType.dayTime(), result.getPeriodType());
    }

    /**
     * Test between method with Instant objects.
     */
    @Test
    public void testBetweenInstant() {
        Instant start = Instant.parse("2012-01-01T00:00:00.001Z");
        Instant end = Instant.parse("2012-01-01T00:00:01.001Z");
        BaseSingleFieldPeriod period = new BaseDays(1);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.dayOfMonth());
        assertEquals(1, result);
    }

    /**
     * Test between method with ReadableInstant objects.
     */
    @Test
    public void testBetweenReadableInstant() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.001Z", ISOChronology.getInstanceUTC());
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-01T00:00:01.001Z", ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod period = new BaseDays(1);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.dayOfMonth());
        assertEquals(1, result);
    }

    /**
     * Test between method with ReadablePartial objects.
     */
    @Test
    public void testBetweenReadablePartial() {
        ReadablePeriod zero = Period.ZERO;
        ReadablePartial start = zero.withFields(new BaseDays(1));
        ReadablePartial end = zero.withFields(new BaseHours(2));
        BaseSingleFieldPeriod period = new BaseHours(1);
        int result = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(1, result);
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new BaseDays(1234567);
        long millisPerUnit = ChronoUnit.MILLIS.getDuration().toMillis();
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(1234567, result);
    }

}

class BaseDays extends BaseSingleFieldPeriod {
    public BaseDays() {
        super(1);
    }

    public BaseDays(int days) {
        super(days);
    }

    public DurationFieldType getFieldType() {
        return DurationFieldType.dayOfMonth();
    }
}

class BaseHours extends BaseSingleFieldPeriod {
    public BaseHours() {
        super(1);
    }

    public BaseHours(int hours) {
        super(hours);
    }

    public DurationFieldType getFieldType() {
        return DurationFieldType.hourOfDay();
    }
}