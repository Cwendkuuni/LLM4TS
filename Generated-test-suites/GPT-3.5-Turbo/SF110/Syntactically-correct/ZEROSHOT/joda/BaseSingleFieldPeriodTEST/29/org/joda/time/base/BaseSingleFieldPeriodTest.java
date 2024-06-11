package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod baseSingleFieldPeriod;

    @Before
    public void setUp() {
        baseSingleFieldPeriod = new BaseSingleFieldPeriodImpl(10);
    }

    @Test
    public void testGetValue() {
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(20);
        assertEquals(20, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSize() {
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType() {
        assertEquals(DurationFieldType.years(), baseSingleFieldPeriod.getFieldType(0));
        baseSingleFieldPeriod.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue() {
        assertEquals(10, baseSingleFieldPeriod.getValue(0));
        baseSingleFieldPeriod.getValue(1);
    }

    @Test
    public void testGet() {
        assertEquals(10, baseSingleFieldPeriod.get(DurationFieldType.years()));
        assertEquals(0, baseSingleFieldPeriod.get(DurationFieldType.months()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(baseSingleFieldPeriod.isSupported(DurationFieldType.years()));
        assertFalse(baseSingleFieldPeriod.isSupported(DurationFieldType.months()));
    }

    @Test
    public void testToPeriod() {
        Period period = baseSingleFieldPeriod.toPeriod();
        assertEquals(baseSingleFieldPeriod.getValue(), period.get(DurationFieldType.years()));

        baseSingleFieldPeriod.setValue(5);
        period = baseSingleFieldPeriod.toPeriod();
        assertEquals(baseSingleFieldPeriod.getValue(), period.get(DurationFieldType.years()));
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(baseSingleFieldPeriod.getValue(), mutablePeriod.get(DurationFieldType.years()));

        baseSingleFieldPeriod.setValue(5);
        mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertEquals(baseSingleFieldPeriod.getValue(), mutablePeriod.get(DurationFieldType.years()));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod samePeriod = new BaseSingleFieldPeriodImpl(10);
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriodImpl(20);

        assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod));
        assertTrue(baseSingleFieldPeriod.equals(samePeriod));
        assertFalse(baseSingleFieldPeriod.equals(differentPeriod));
        assertFalse(baseSingleFieldPeriod.equals(null));
    }

    @Test
    public void testHashCode() {
        int hashCode = 27 * (27 * 17 + baseSingleFieldPeriod.getValue()) + baseSingleFieldPeriod.getFieldType().hashCode();
        assertEquals(hashCode, baseSingleFieldPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod biggerPeriod = new BaseSingleFieldPeriodImpl(15);
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriodImpl(8);
        BaseSingleFieldPeriod samePeriod = new BaseSingleFieldPeriodImpl(10);

        assertEquals(1, baseSingleFieldPeriod.compareTo(smallerPeriod));
        assertEquals(0, baseSingleFieldPeriod.compareTo(samePeriod));
        assertEquals(-1, baseSingleFieldPeriod.compareTo(biggerPeriod));
    }

    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

        protected BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.years();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.years();
        }
    }
}