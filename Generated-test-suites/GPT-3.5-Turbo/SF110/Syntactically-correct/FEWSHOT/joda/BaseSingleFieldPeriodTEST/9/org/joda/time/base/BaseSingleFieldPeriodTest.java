package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_InstantDurationFieldType() {
        DateTime start = new DateTime(2000, 1, 1, 0, 0, 0);
        DateTime end = new DateTime(2000, 1, 1, 0, 30, 0);
        assertEquals(30, BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_InstantDurationFieldType_NullInstant1() {
        DateTime end = new DateTime(2000, 1, 1, 0, 30, 0);
        BaseSingleFieldPeriod.between(null, end, DurationFieldType.minutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_InstantDurationFieldType_NullInstant2() {
        DateTime start = new DateTime(2000, 1, 1, 0, 0, 0);
        BaseSingleFieldPeriod.between(start, null, DurationFieldType.minutes());
    }

    @Test
    public void testBetween_PartialPartialReadablePeriod() {
        ReadablePartial start = new DateTime(2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePartial end = new DateTime(2000, 1, 2, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period().withYears(1).withMonths(6);
        assertEquals(547, BaseSingleFieldPeriod.between(start, end, period));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_PartialPartialReadablePeriod_NullPartial1() {
        ReadablePartial end = new DateTime(2000, 1, 2, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period().withYears(1).withMonths(6);
        BaseSingleFieldPeriod.between(null, end, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_PartialPartialReadablePeriod_NullPartial2() {
        ReadablePartial start = new DateTime(2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period().withYears(1).withMonths(6);
        BaseSingleFieldPeriod.between(start, null, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_PartialPartialReadablePeriod_MismatchedSize() {
        ReadablePartial start = new DateTime(2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePartial end = new DateTime(2000, 1, 2, 0, 0, 0).toLocalTime();
        ReadablePeriod period = new Period().withYears(1).withMonths(6);
        BaseSingleFieldPeriod.between(start, end, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_PartialPartialReadablePeriod_MismatchedFields() {
        ReadablePartial start = new DateTime(2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePartial end = new DateTime(2000, 1, 2, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period().withYears(1).withSeconds(6);
        BaseSingleFieldPeriod.between(start, end, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_PartialPartialReadablePeriod_NotContiguous() {
        ReadablePartial start = new DateTime(2000, 1, 1, 0, 0, 0).toLocalDate();
        ReadablePartial end = new DateTime(2000, 1, 3, 0, 0, 0).toLocalDate();
        ReadablePeriod period = new Period().withYears(1).withMonths(6);
        BaseSingleFieldPeriod.between(start, end, period);
    }

    @Test
    public void testStandardPeriodIn_ReadablePeriod_long() {
        ReadablePeriod period = new Period().withMinutes(10);
        assertEquals(10, BaseSingleFieldPeriod.standardPeriodIn(period, Minutes.ONE.toStandardDuration().getMillis()));
    }

    @Test
    public void testStandardPeriodIn_ReadablePeriod_long_WithZeroReadables() {
        ReadablePeriod period = new Period();
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(period, Minutes.ONE.toStandardDuration().getMillis()));
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, Minutes.ONE.toStandardDuration().getMillis()));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new Minutes(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertEquals(DurationFieldType.minutes(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new Minutes(10);
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new Minutes(10);
        period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertEquals(10, period.get(DurationFieldType.minutes()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new Minutes(10);
        assertTrue(period.isSupported(DurationFieldType.minutes()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new Minutes(10);
        Period expectedPeriod = Period.minutes(10);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new Minutes(10);
        Period expectedPeriod = Period.minutes(10);
        assertEquals(expectedPeriod, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new Minutes(10);
        BaseSingleFieldPeriod period2 = new Minutes(10);
        BaseSingleFieldPeriod period3 = new Minutes(20);
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new Minutes(10);
        int expectedHashCode = 27 * (27 * 17 + 10) + DurationFieldType.minutes().hashCode();
        assertEquals(expectedHashCode, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new Minutes(10);
        BaseSingleFieldPeriod period2 = new Minutes(20);
        BaseSingleFieldPeriod period3 = new Minutes(20);
        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertTrue(period2.compareTo(period3) == 0);
    }
}