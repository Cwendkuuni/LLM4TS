package org.joda.time.base;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;

import static org.junit.Assert.*;

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
    public void testStandardPeriodIn() {
        ReadablePeriod period = new ReadablePeriod() {
            @Override
            public int getValue(int index) {
                return 2;
            }

            @Override
            public DurationFieldType getFieldType(int index) {
                return DurationFieldType.minutes();
            }

            @Override
            public int size() {
                return 1;
            }
        };

        assertEquals(120, BaseSingleFieldPeriod.standardPeriodIn(period, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithImpreciseValues() {
        ReadablePeriod period = new ReadablePeriod() {
            @Override
            public int getValue(int index) {
                return 2;
            }

            @Override
            public DurationFieldType getFieldType(int index) {
                return DurationFieldType.years();
            }

            @Override
            public int size() {
                return 1;
            }
        };

        BaseSingleFieldPeriod.standardPeriodIn(period, 1);
    }

    private static class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {
        protected BaseSingleFieldPeriodImpl(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.minutes();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.time();
        }
    }
}