package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test constructor
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertNotNull(period);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(5, period.getValue(0));

        // Test get methods
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType(0));
        assertEquals(PeriodType.standard(), period.getPeriodType());

        // Test equals method
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(5);
        assertEquals(period, other);
        assertEquals(other, period);

        BaseSingleFieldPeriod differentType = new BaseSingleFieldPeriod(10, DurationFieldType.HOURS);
        assertNotEquals(period, differentType);

        BaseSingleFieldPeriod nullPeriod = null;
        assertNotEquals(period, nullPeriod);

        // Test hashCode method
        int hashCodePeriod = period.hashCode();
        int hashCodeOther = other.hashCode();
        assertEquals(hashCodePeriod, hashCodeOther);

        // Test compareTo method
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(3);
        assertEquals(-2, period.compareTo(smallerPeriod));

        BaseSingleFieldPeriod largerPeriod = new BaseSingleFieldPeriod(7);
        assertEquals(2, period.compareTo(largerPeriod));

        // Test comparison with different types
        BaseSingleFieldPeriod longPeriod = new BaseSingleFieldPeriod(15, DurationFieldType.WEEKS);
        assertEquals(0, period.compareTo(longPeriod));

        // Test toPeriod method
        Period toPeriod = period.toPeriod();
        assertEquals(5, toPeriod.getDays());

        // Test toMutablePeriod method
        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        toMutablePeriod.setDays(10);
        assertEquals(10, toMutablePeriod.getDays());
        assertEquals(period, toMutablePeriod.toPeriod());

        // Test between method
        ReadableInstant start = new BaseDateTime().withZoneRetainFields(ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(start.getEpochSecond() + 5 * 24 * 60 * 60 * 1000).toInstant();
        assertEquals(5, BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS));

        // Test standardPeriodIn method
        assertEquals(21, BaseSingleFieldPeriod.standardPeriodIn(new Period(2, 1, 3, 5, 15, 15, 30), 86400000));
    }
}