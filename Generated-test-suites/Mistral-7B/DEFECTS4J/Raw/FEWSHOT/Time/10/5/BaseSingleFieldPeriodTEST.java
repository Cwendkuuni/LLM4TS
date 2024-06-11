package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.joda.time.field.IntervalUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(2, DurationFieldType.days());
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(chrono, 2012, 1, 1, 0, 0, 0, 0, 0).toInstant();
        ReadableInstant end = new BaseDateTime(chrono, 2012, 1, 3, 0, 0, 0, 0, 0).toInstant();
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
        assertEquals(p1, p1.plus(p1));
        assertEquals(p2, p2.plus(p1));
        assertEquals(p2, p1.plus(p2));
        assertEquals(p1, p2.minus(p1));
        assertEquals(p2, p1.minus(p2));
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
    }

    @Test
    public void testBetweenPartial() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(2, DurationFieldType.days());
        ReadablePeriod zeroInstance = Period.ZERO;
        ReadablePartial start = new BaseDateTime(ISOChronology.getInstanceUTC(), 2012, 1, 1, 0, 0, 0, 1, 0).toLocalDate();
        ReadablePartial end = new BaseDateTime(ISOChronology.getInstanceUTC(), 2012, 1, 3, 0, 0, 0, 1, 0).toLocalDate();
        assertEquals(1, BaseSingleFieldPeriod.between(start, end, zeroInstance));
        assertEquals(p1, p1.plus(p1));
        assertEquals(p2, p2.plus(p1));
        assertEquals(p2, p1.plus(p2));
        assertEquals(p1, p2.minus(p1));
        assertEquals(p2, p1.minus(p2));
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
        // Test with different field types
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, DurationFieldType.milliseconds()));
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours()));
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes()));
        assertEquals(0, BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds()));
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(12, DurationFieldType.hours());
        assertEquals(24 * 24, BaseSingleFieldPeriod.standardPeriodIn(p1, FieldUtils.ONE_DAY.getMillis()));
        assertEquals(12, BaseSingleFieldPeriod.standardPeriodIn(p2, FieldUtils.ONE_HOUR.getMillis()));
        // Test with negative period
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(-1, DurationFieldType.days());
        assertEquals(-24 * 24, BaseSingleFieldPeriod.standardPeriodIn(p3, FieldUtils.ONE_DAY.getMillis()));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        assertEquals(3, p.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(0, DurationFieldType.days());
        p.setValue(3);
        assertEquals(3, p.getValue());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        Period expected = new Period(3, 0, 0, 0);
        assertEquals(expected, p.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        MutablePeriod expected = new MutablePeriod();
        expected.add(DurationFieldType.days(), 3);
        assertEquals(expected, p.toMutablePeriod());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(2, DurationFieldType.days());
        BaseSingleFieldPeriod p3 = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        assertEquals(0, p1.compareTo(p1));
        assertEquals(1, p1.compareTo(p2));
        assertEquals(-1, p2.compareTo(p1));
        assertEquals(0, p1.compareTo(p3));
        assertEquals(0, p3.compareTo(p1));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p3 = getPeriod(2, DurationFieldType.days());
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(p4));
        assertTrue(p2.equals(p2));
        assertTrue(p2.equals(p4));
        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertNull(p1.equals(null));
        assertNull(p1.equals(""));
        assertNull(p1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p3 = getPeriod(2, DurationFieldType.days());
        BaseSingleFieldPeriod p4 = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();
        int h3 = p3.hashCode();
        int h4 = p4.hashCode();
        assertEquals(h1, p1.hashCode());
        assertEquals(h2, p2.hashCode());
        assertEquals(h3, p3.hashCode());
        assertEquals(h4, p4.hashCode());
        assertEquals(h1, p4.hashCode());
    }

    @Test
    public void testToString() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        assertEquals("P3D", p.toString());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        DurationFieldType expected = DurationFieldType.days();
        assertEquals(expected, p.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod p = getPeriod(3, DurationFieldType.days());
        PeriodType expected = PeriodType.standard();
        assertEquals(expected, p.getPeriodType());
    }

    @Test
    public void testPlus() {
        BaseSingleFieldPeriod p1 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(2, DurationFieldType.days());
        BaseSingleFieldPeriod sum = p1.plus(p2);
        assertEquals(p1.getValue() + p2.getValue(), sum.getValue());
    }

    @Test
    public void testMinus() {
        BaseSingleFieldPeriod p1 = getPeriod(3, DurationFieldType.days());
        BaseSingleFieldPeriod p2 = getPeriod(1, DurationFieldType.days());
        BaseSingleFieldPeriod difference = p1.minus(p2);
        assertEquals(p1.getValue() - p2.getValue(), difference.getValue());
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        DurationFieldType fieldType = DurationFieldType.weeks();
        assertTrue(p.isSupported(DurationFieldType.days()));
        assertFalse(p.isSupported(fieldType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        DurationFieldType fieldType = DurationFieldType.weeks();
        p.get(fieldType);
    }

    @Test
    public void testGetValueWithInvalidFieldType() {
        BaseSingleFieldPeriod p = new BaseSingleFieldPeriod(1, DurationFieldType.days());
        DurationFieldType fieldType = DurationFieldType.weeks();
        assertEquals(0, p.get(fieldType));
    }

    private BaseSingleFieldPeriod getPeriod(int value, DurationFieldType fieldType) {
        return new BaseSingleFieldPeriod(value, fieldType);
    }
}