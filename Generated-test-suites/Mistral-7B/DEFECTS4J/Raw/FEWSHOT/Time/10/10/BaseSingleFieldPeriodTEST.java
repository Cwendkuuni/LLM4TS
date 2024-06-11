package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test BaseSingleFieldPeriod constructor
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());

        // Test getters
        assertEquals(1, period.size());
        DurationFieldType fieldType = period.getFieldType();
        PeriodType periodType = period.getPeriodType();
        assertEquals(BaseDurationFieldType.DAYS, fieldType.getClass());
        assertEquals(PeriodType.standard(), periodType);

        // Test toPeriod() and toMutablePeriod() methods
        Period toPeriod = period.toPeriod();
        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        assertEquals(period, toPeriod);
        assertEquals(period, toMutablePeriod);

        // Test equals() method
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(other));
        BaseSingleFieldPeriod notSameType = new BaseSingleFieldPeriod(5, DurationFieldType.HOURS);
        assertFalse(period.equals(notSameType));
        Object notInstance = new Object();
        assertFalse(period.equals(notInstance));

        // Test hashCode() method
        int hashCode1 = period.hashCode();
        BaseSingleFieldPeriod identical = new BaseSingleFieldPeriod(5);
        int hashCode2 = identical.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test compareTo() method
        BaseSingleFieldPeriod less = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod greater = new BaseSingleFieldPeriod(7);
        int compareToLess = less.compareTo(period);
        int compareToGreater = greater.compareTo(period);
        assertEquals(-2, compareToLess);
        assertEquals(2, compareToGreater);

        // Test between() method
        ReadableInstant start = DateTimeUtils.currentTime().withFieldAdded(BaseDurationFieldType.DAYS, 3);
        ReadableInstant end = DateTimeUtils.currentTime().withFieldAdded(BaseDurationFieldType.DAYS, 6);
        int between = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(3, between);

        // Test standardPeriodIn() method
        ReadablePeriod testPeriod = Period.parse("PT3S");
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, 1000);
        assertEquals(3, standardPeriodIn);
    }
}