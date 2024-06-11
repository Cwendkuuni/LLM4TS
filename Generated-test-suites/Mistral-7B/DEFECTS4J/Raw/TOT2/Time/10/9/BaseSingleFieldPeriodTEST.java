package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.base.BasicDate;
import org.joda.time.base.BasicDateTime;
import org.joda.time.base.BasicPeriod;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructor_WithValidPeriodValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod expected = new BaseSingleFieldPeriod(5);
        assertEquals(expected, period);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        int expected = 7;
        int actual = period.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        int expected = 10;
        int actual = period.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType expected = DurationFieldType.DAYS;
        DurationFieldType actual = period.getFieldType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType expected = PeriodType.standard();
        PeriodType actual = period.getPeriodType();
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 1;
        int actual = period.size();
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_WithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_WithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    @Test
    public void testGet_WithSupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 5;
        int actual = period.get(DurationFieldType.DAYS);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean expected = true;
        DurationFieldType fieldType = DurationFieldType.DAYS;
        boolean actual = period.isSupported(fieldType);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupported_WithUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean expected = false;
        DurationFieldType fieldType = DurationFieldType.MONTHS;
        boolean actual = period.isSupported(fieldType);
        assertEquals(expected, actual);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expected = new Period(DurationFieldType.DAYS, 5);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod(DurationFieldType.DAYS, 5);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_WithSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean expected = true;
        boolean actual = period.equals(period);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_WithDifferentInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        boolean expected = false;
        boolean actual = period1.equals(period2);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals_WithDifferentType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Object other = new Object();
        boolean expected = false;
        boolean actual = period.equals(other);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 1059841607;
        int actual = period.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo_WithEqualPeriods() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int expected = 0;
        int actual = period1.compareTo(period2);
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo_WithGreaterPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        int expected = 1;
        int actual = period1.compareTo(period2);
        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo_WithLesserPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        int expected = -1;
        int actual = period1.compareTo(period2);
        assertEquals(expected, actual);
    }

    @Test
    public void testBetween() {
        ReadableInstant start = new BasicDateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BasicDateTime(2000, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int actual = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullStart() {
        ReadableInstant start = null;
        ReadableInstant end = new BasicDateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int actual = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithNullEnd() {
        ReadableInstant start = new BasicDateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = null;
        int actual = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetween_WithInvalidFieldType() {
        ReadableInstant start = new BasicDateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BasicDateTime(2000, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int actual = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MONTHS);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new BasicPeriod(DurationFieldType.DAYS, 2, PeriodType.standard());
        long expected = 48 * 60 * 60 * 1000L * 2;
        long actual = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodIn_WithInvalidPeriod() {
        ReadablePeriod period = new BasicPeriod(DurationFieldType.MONTHS, 2, PeriodType.standard());
        long actual = BaseSingleFieldPeriod.standardPeriodIn(period, 1000L);
    }
}