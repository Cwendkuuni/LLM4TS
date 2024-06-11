package org.joda.time.base;

import static org.junit.Assert.assertEquals;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(5, period.getValue());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        Period expected = Period.ZERO.withFields(period);

        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        MutablePeriod expected = new MutablePeriod();
        expected.add(period);

        assertEquals(expected, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(true, period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        int expected = 27 * 17 + period.getValue();
        expected = 27 * expected + period.getFieldType().hashCode();

        assertEquals(expected, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public org.joda.time.DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };

        assertEquals(1, period1.compareTo(period2));
        assertEquals(-1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(period1));
    }
}