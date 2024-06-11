package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;

import org.junit.Test;

/**
 * Unit tests for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Tests the standard period creation method.
     */
    @Test
    public void testStandardPeriodCreation() {
        // Create test periods
        ReadablePeriod pd1 = new Period(2, Chronology.ISO_INSTANT);
        ReadablePeriod pd2 = new Period(5, Chronology.ISO_INSTANT);
        ReadablePeriod pd3 = new Period(3, Chronology.ISO_DATE);

        // Test standard period creation
        int hours = BaseSingleFieldPeriod.standardPeriodIn(pd1, Chronology.ISO_INSTANT.getMillisPerDay());
        assertEquals(pd1.getMillis(), hours * Chronology.ISO_INSTANT.getMillisPerDay());
        assertEquals(hours, BaseSingleFieldPeriod.standardPeriodIn(pd1, Chronology.ISO_INSTANT.getMillisPerDay()).getValue());
        assertEquals(pd1.getFieldType(), BaseSingleFieldPeriod.standardPeriodIn(pd1, Chronology.ISO_INSTANT.getMillisPerDay()).getFieldType());

        hours = BaseSingleFieldPeriod.standardPeriodIn(pd2, Chronology.ISO_INSTANT.getMillisPerDay());
        assertEquals(pd2.getMillis(), hours * Chronology.ISO_INSTANT.getMillisPerDay());
        assertEquals(hours, BaseSingleFieldPeriod.standardPeriodIn(pd2, Chronology.ISO_INSTANT.getMillisPerDay()).getValue());
        assertEquals(pd2.getFieldType(), BaseSingleFieldPeriod.standardPeriodIn(pd2, Chronology.ISO_INSTANT.getMillisPerDay()).getFieldType());

        int days = BaseSingleFieldPeriod.standardPeriodIn(pd3, Chronology.ISO_DATE.getMillisPerDay());
        assertEquals(pd3.getMillis(), days * Chronology.ISO_DATE.getMillisPerDay());
        assertEquals(days, BaseSingleFieldPeriod.standardPeriodIn(pd3, Chronology.ISO_DATE.getMillisPerDay()).getValue());
        assertEquals(pd3.getFieldType(), BaseSingleFieldPeriod.standardPeriodIn(pd3, Chronology.ISO_DATE.getMillisPerDay()).getFieldType());
    }

    /**
     * Tests the between method for instant cases.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();

        // Test valid instant cases
        ReadableInstant start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(2012, 1, 1, 3, 0, 0, 0, chrono).toInstant();
        int hours = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(hours, 3);

        start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        end = new BaseDateTime(2012, 1, 2, 0, 0, 0, 0, chrono).toInstant();
        hours = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(hours, 24);

        start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        end = new BaseDateTime(2012, 1, 1, 0, 0, 0, 1, chrono).toInstant();
        assertEquals(BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLISECONDS), 1000);

        // Test null instant cases
        start = null;
        end = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        assertNull(BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));

        end = null;
        start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toInstant();
        assertNull(BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS));
    }

    /**
     * Tests the between method for partial cases.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = ISOChronology.getInstanceUTC().withUTC();

        // Test valid partial cases
        ReadablePartial start = new BaseDateTime(2012, 1, 1, 0, 1, 0, 0, chrono).toLocalDate().toLocalDate();
        ReadablePartial end = new BaseDateTime(2012, 1, 2, 0, 0, 0, 0, chrono).toLocalDate().toLocalDate();
        int days = BaseSingleFieldPeriod.between(start, end, new Period(0, Chronology.ISO_DATE));
        assertEquals(days, 1);

        start = new BaseDateTime(2012, 1, 1, 0, 1, 0, 0, chrono).toLocalDateTime().toLocalDateTime();
        end = new BaseDateTime(2012, 1, 1, 0, 2, 0, 0, chrono).toLocalDateTime().toLocalDateTime();
        assertEquals(BaseSingleFieldPeriod.between(start, end, new Duration(0, Chronology.ISO_DATE, DurationFieldType.HOURS)), 1);

        // Test null partial cases
        start = null;
        end = new BaseDateTime(2012, 1, 1, 0, 1, 0, 0, chrono).toLocalDate().toLocalDate();
        assertEquals(BaseSingleFieldPeriod.between(start, end, new Period(0, Chronology.ISO_DATE)), 0);

        end = null;
        start = new BaseDateTime(2012, 1, 1, 0, 1, 0, 0, chrono).toLocalDate().toLocalDate();
        assertEquals(BaseSingleFieldPeriod.between(start, end, new Period(0, Chronology.ISO_DATE)), 0);
    }

    /**
     * Tests the getValue method.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestPeriod(42);
        assertEquals(42, period.getValue());
    }

    /**
     * Tests the getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestPeriod(42);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestPeriod(15);
        BaseSingleFieldPeriod period2 = new TestPeriod(15);
        BaseSingleFieldPeriod period3 = new TestPeriod(20);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    /**
     * Tests the hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new TestPeriod(15);
        BaseSingleFieldPeriod period2 = new TestPeriod(15);
        BaseSingleFieldPeriod period3 = new TestPeriod(20);

        assertEquals(period1.hashCode(), period1.hashCode());
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotSame(period1.hashCode(), period3.hashCode());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new TestPeriod(15);
        BaseSingleFieldPeriod period2 = new TestPeriod(15);
        BaseSingleFieldPeriod period3 = new TestPeriod(20);

        assertEquals(0, period1.compareTo(period1));
        assertEquals(0, period1.compareTo(period2));
        assertEquals(-1, period1.compareTo(period3));
    }

    /**
     * A test period class extending BaseSingleFieldPeriod.
     */
    static class TestPeriod extends BaseSingleFieldPeriod {

        TestPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

}