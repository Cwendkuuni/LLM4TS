package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.test.TestBase;

import java.util.Arrays;

public class BaseSingleFieldPeriodTest extends TestBase {

    public void testBaseSingleFieldPeriod() {
        DurationFieldType fieldType = DurationFieldType.days();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(7, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
        assertNotNull(period.toPeriod());
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(period, mutablePeriod.toImmutable());
        assertEquals(period, mutablePeriod.add(Period.ONE.withFieldType(fieldType)));

        assertEquals(Period.ONE.withFieldType(fieldType), period.toPeriod().withFields(new BaseSingleFieldPeriod(1, fieldType)));

        assertEquals("BaseSingleFieldPeriod{[1]fieldType=days()period=7}", period.toString());
    }

    public void testBaseSingleFieldPeriodComparison() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(7) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(period1, period1);
        assertNotSame(period1, period2);
        assertEquals(period1, new BaseSingleFieldPeriod(7, DurationFieldType.days()));
        assertNotEquals(period1, new BaseSingleFieldPeriod(6, DurationFieldType.days()));
        assertNotEquals(period1, new BaseSingleFieldPeriod(7, DurationFieldType.hours()));

        assertEquals(0, period1.compareTo(period1));
        assertEquals(-2, period1.compareTo(new BaseSingleFieldPeriod(5, DurationFieldType.days())));
        assertEquals(1, period1.compareTo(new BaseSingleFieldPeriod(8, DurationFieldType.days())));
        assertEquals(1, period1.compareTo(new BaseSingleFieldPeriod(7, DurationFieldType.days())));
        assertEquals(0, period1.compareTo(new BaseSingleFieldPeriod(7, PeriodType.standard())));
    }

    public void testStandardPeriodIn() {
        assertEquals(6, BaseSingleFieldPeriod.standardPeriodIn(Period.ONE.withFieldType(DurationFieldType.days()), 86400000L));
        assertEquals(2, BaseSingleFieldPeriod.standardPeriodIn(Period.ONE.withFieldType(DurationFieldType.hours()), 86400000L));
        assertEquals(24, BaseSingleFieldPeriod.standardPeriodIn(Period.ONE.withFieldType(DurationFieldType.minutes()), 86400000L));
        assertEquals(1440, BaseSingleFieldPeriod.standardPeriodIn(Period.ONE.withFieldType(DurationFieldType.seconds()), 86400000L));
        assertEquals(43200, BaseSingleFieldPeriod.standardPeriodIn(Period.ONE.withFieldType(DurationFieldType.milliseconds()), 86400000L));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(new Period(7, DurationFieldType.days()), 86400000L));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(new Period(7, PeriodType.standard()), 86400000L));
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 86400000L));
    }

    public void testBetween() {
        ReadableInstant start = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1, 0, 0, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();

        assertEquals(1, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));
    }

    public void testBetweenPartial() {
        ReadablePartial start = new org.joda.time.LocalDate(2018, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2018, 1, 2);

        assertEquals(1, BaseSingleFieldPeriod.between(start, end, Period.standardInstance()));
    }

    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public int size() {
                return 2;
            }
        };
        assertEquals(1, period.size());
    }

    public void testGetValueAt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public int getValue(int index) {
                return index == 0 ? 10 : 5;
            }
        };
        assertEquals(10, period.getValue());
        assertEquals(5, period.getValue(1));
    }

    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
            @Override
            public boolean isSupported(DurationFieldType type) {
                return type == DurationFieldType.days() || type == DurationFieldType.minutes();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }
}