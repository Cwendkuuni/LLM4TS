package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.BaseJulianDate;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSimpleDayPeriod() {
        BaseSingleFieldPeriod dayPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }
        };

        assertEquals(1, dayPeriod.getValue());
        assertEquals(DurationFieldType.DAYS, dayPeriod.getFieldType());
        assertEquals(PeriodType.standard(), dayPeriod.getPeriodType());
        assertEquals(1, dayPeriod.size());
        assertEquals(DurationFieldType.DAYS, dayPeriod.getFieldType(0));

        BaseSingleFieldPeriod otherDayPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }
        };
        assertEquals(1, dayPeriod.compareTo(dayPeriod));
        assertEquals(0, dayPeriod.compareTo(dayPeriod));
        assertEquals(-1, dayPeriod.compareTo(otherDayPeriod));

        BaseDateTime start = new BaseDateTime(0);
        BaseDateTime end = new BaseDateTime(1);
        Period period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        BaseSingleFieldPeriod createdDayPeriod = new BaseSingleFieldPeriod(period.getMillis());
        assertEquals(dayPeriod, createdDayPeriod);

        MutablePeriod mutablePeriod = new MutablePeriod(dayPeriod);
        mutablePeriod.setValue(DurationFieldType.DAYS, 2);
        Period newPeriod = mutablePeriod.toPeriod();
        BaseSingleFieldPeriod newCreatedDayPeriod = new BaseSingleFieldPeriod(newPeriod.getMillis());
        assertEquals(otherDayPeriod, newCreatedDayPeriod);

        Period invalidPeriod = new Period(1, DurationFieldType.DAYS, PeriodType.standard());
        try {
            new BaseSingleFieldPeriod(invalidPeriod.getMillis());
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleHourPeriod() {
        BaseSingleFieldPeriod hourPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }
        };

        assertEquals(1, hourPeriod.getValue());
        assertEquals(DurationFieldType.HOURS, hourPeriod.getFieldType());
        assertEquals(PeriodType.standard(), hourPeriod.getPeriodType());
        assertEquals(1, hourPeriod.size());
        assertEquals(DurationFieldType.HOURS, hourPeriod.getFieldType(0));

        BaseSingleFieldPeriod otherHourPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }
        };
        assertEquals(1, hourPeriod.compareTo(hourPeriod));
        assertEquals(0, hourPeriod.compareTo(hourPeriod));
        assertEquals(-1, hourPeriod.compareTo(otherHourPeriod));

        BaseJulianDate start = new BaseJulianDate(0);
        BaseJulianDate end = new BaseJulianDate(1);
        Period period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.HOURS);
        BaseSingleFieldPeriod createdHourPeriod = new BaseSingleFieldPeriod(period.getMillis());
        assertEquals(hourPeriod, createdHourPeriod);

        MutablePeriod mutablePeriod = new MutablePeriod(hourPeriod);
        mutablePeriod.setValue(DurationFieldType.HOURS, 2);
        Period newPeriod = mutablePeriod.toPeriod();
        BaseSingleFieldPeriod newCreatedHourPeriod = new BaseSingleFieldPeriod(newPeriod.getMillis());
        assertEquals(otherHourPeriod, newCreatedHourPeriod);

        Period invalidPeriod = new Period(1, DurationFieldType.HOURS, PeriodType.standard());
        try {
            new BaseSingleFieldPeriod(invalidPeriod.getMillis());
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimpleFieldPeriodWithChronology() {
        BaseSingleFieldPeriod monthPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MONTHS;
            }
        };

        Chronology isoChronology = ISOChronology.getInstanceUTC();
        BaseJulianDate start = new BaseJulianDate(0, 0, 1, isoChronology);
        BaseJulianDate end = new BaseJulianDate(2000, 1, 1, isoChronology);
        Period period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MONTHS);
        BaseSingleFieldPeriod createdMonthPeriod = new BaseSingleFieldPeriod(period.getMillis(), isoChronology);
        assertEquals(monthPeriod, createdMonthPeriod);

        Period invalidPeriod = new Period(1, DurationFieldType.MONTHS, PeriodType.standard());
        try {
            new BaseSingleFieldPeriod(invalidPeriod.getMillis(), isoChronology);
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}