package org.joda.time.base;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetween() {
        Chronology chrono = DateTimeUtils.getCzech();

        ReadableInstant start = new BaseDateTime(13, 0, 0, 0, 1, 2012, chrono).toInstant();
        ReadableInstant end = new BaseDateTime(23, 45, 60, 1, 2, 2012, chrono).toInstant();

        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(2, period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetweenPartial() {
        Chronology chrono = DateTimeUtils.getCzech();

        ReadablePartial start = new BaseDateTime(2012, 1, 1, 0, 0, 0, 0, chrono).toLocalDate().toLocalDateTime();
        ReadablePartial end = new BaseDateTime(2012, 1, 2, 1, 2, 0, 0, chrono).toLocalDate().toLocalDateTime();

        ReadablePeriod zeroInstance = Period.ZERO;

        int period = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(new Period(1, 2, 0, 0, 0, 0, 0, PeriodType.standard()), period);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(5, 23, 59, 58, 59, 59, 59, PeriodType.standard());
        long millisPerUnit = 1000 * 60 * 60 * 24;

        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(21, hours);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#BaseSingleFieldPeriod(int)}.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(12, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue()}.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(12, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#size()}.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(1, period.size());
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @Test
    public void testGetFieldTypeInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        try {
            period.getFieldType(2);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @Test
    public void testGetValueInt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        try {
            period.getValue(2);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertEquals(12, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.years()));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.years()));
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        Period expected = new Period(12, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        Period actual = period.toPeriod();

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(12);

        MutablePeriod expected = new MutablePeriod();
        expected.setFields(new Period(12, 0, 0, 0, 0, 0, 0, PeriodType.standard()));
        MutablePeriod actual = period.toMutablePeriod();

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(12);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(12);

        assertEquals(period1, period2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(12);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(12);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test method for {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(12);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);

        int comparison = period1.compareTo(period2);
        assertEquals(1, comparison);
    }

}