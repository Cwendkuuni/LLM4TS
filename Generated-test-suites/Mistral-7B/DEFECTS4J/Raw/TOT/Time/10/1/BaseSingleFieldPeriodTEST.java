package org.joda.time.base;

import org.joda.time.BaseDateTime;
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
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testDaysPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test standard construction
        assertEquals(1, period.getValue());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());

        // Test between method
        ReadableInstant start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        ReadableInstant end = new BaseDateTime(86400000L, ISOChronology.getInstanceUTC());
        int days = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, days);

        // Test standardPeriodIn method
        Period standardPeriod = new Period(1, PeriodType.DAYS);
        long millisPerUnit = PeriodType.DAYS.getMillisPerDay();
        int standardDays = BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, millisPerUnit);
        assertEquals(1, standardDays);

        // Test toPeriod method
        Period resultPeriod = period.toPeriod();
        assertEquals(1, resultPeriod.getDays());

        // Test toMutablePeriod method
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(1, mutablePeriod.getDays());
    }

    @Test
    public void testHoursPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test standard construction
        assertEquals(2, period.getValue());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(DurationFieldType.HOURS, period.getFieldType());

        // Test between method
        ReadableInstant start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        ReadableInstant end = new BaseDateTime(52800000L, ISOChronology.getInstanceUTC());
        int hours = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        assertEquals(1, hours);

        // Test standardPeriodIn method
        Period standardPeriod = new Period(2, PeriodType.HOURS);
        long millisPerUnit = PeriodType.HOURS.getMillisPerHour();
        int standardHours = BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, millisPerUnit);
        assertEquals(2, standardHours);

        // Test toPeriod method
        Period resultPeriod = period.toPeriod();
        assertEquals(2, resultPeriod.getHours());

        // Test toMutablePeriod method
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(2, mutablePeriod.getHours());
    }

    @Test
    public void testMinutesPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(35) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test standard construction
        assertEquals(35, period.getValue());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(DurationFieldType.MINUTES, period.getFieldType());

        // Test between method
        ReadableInstant start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        ReadableInstant end = new BaseDateTime(2100000L, ISOChronology.getInstanceUTC());
        int minutes = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(1, minutes);

        // Test standardPeriodIn method
        Period standardPeriod = new Period(1, PeriodType.MINUTES);
        long millisPerUnit = PeriodType.MINUTES.getMillisPerMinute();
        int standardMinutes = BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, millisPerUnit);
        assertEquals(1, standardMinutes);

        // Test toPeriod method
        Period resultPeriod = period.toPeriod();
        assertEquals(1, resultPeriod.getMinutes());

        // Test toMutablePeriod method
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(1, mutablePeriod.getMinutes());
    }

    @Test
    public void testDaysWeeksPeriodWithValidValues() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.WEEKS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test standard construction
        assertEquals(7, period.getValue());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(DurationFieldType.WEEKS, period.getFieldType());

        // Test between method
        ReadableInstant start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        ReadableInstant end = new BaseDateTime(604800000L, ISOChronology.getInstanceUTC());
        int weeks = BaseSingleFieldPeriod.between(start, end, DurationFieldType.WEEKS);
        assertEquals(1, weeks);

        // Test standardPeriodIn method
        Period standardPeriod = new Period(1, PeriodType.WEEKS);
        long millisPerUnit = PeriodType.WEEKS.getMillisPerWeek();
        int standardWeeks = BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, millisPerUnit);
        assertEquals(1, standardWeeks);

        // Test toPeriod method
        Period resultPeriod = period.toPeriod();
        assertEquals(1, resultPeriod.getWeeks());

        // Test toMutablePeriod method
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(1, mutablePeriod.getWeeks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDaysWeeksPeriodWithInvalidValues() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.WEEKS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test standard construction should fail
        try {
            period.setValue(-1);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception but got " + e.getMessage());
        }

        // Test between method should fail
        ReadableInstant start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        ReadableInstant end = new BaseDateTime(-604800000L, ISOChronology.getInstanceUTC());
        int weeks = BaseSingleFieldPeriod.between(start, end, DurationFieldType.WEEKS);
    }

    @Test
    public void testToPeriodWithWrongFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test toPeriod method with wrong field type should return zero period
        Period resultPeriod = period.toPeriod();
        assertEquals(Period.ZERO, resultPeriod);
    }
}