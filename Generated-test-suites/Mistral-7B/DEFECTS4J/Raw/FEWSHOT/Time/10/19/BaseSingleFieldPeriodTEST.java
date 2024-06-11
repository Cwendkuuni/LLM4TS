package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.mock.MockDurationFieldType;
import org.joda.time.mock.MockPeriodType;

public class BaseSingleFieldPeriodTest {

    @SuppressWarnings("unchecked")
    public static void testBaseSingleFieldPeriod() {
        DurationFieldType fieldType = new MockDurationFieldType();
        PeriodType periodType = new MockPeriodType();
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        assertEquals(123, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(periodType, period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(fieldType, period.getFieldType(0));
        assertEquals(123, period.getValue(0));

        assertEquals(period, period.toPeriod());
        assertEquals(period, period.toMutablePeriod().toImmutable());

        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(456) {
            @Override
            public DurationFieldType getFieldType() {
                return fieldType;
            }

            @Override
            public PeriodType getPeriodType() {
                return periodType;
            }
        };

        assertEquals(0, period.compareTo(null));
        assertEquals(0, period.compareTo(period));
        assertEquals(-1, period.compareTo(other));

        assertEquals(period, period);
        assertEquals(period, period);
        assertNotEquals(period, other);

        assertEquals(period.hashCode(), period.hashCode());
        assertEquals(period.hashCode(), period.hashCode());
        assertNotEquals(period.hashCode(), other.hashCode());

        assertEquals(period, new Period(PeriodType.standard(), 123));

        BaseSingleFieldPeriod nullPeriod = null;
        assertEquals(false, nullPeriod.equals(period));
        assertEquals(0, nullPeriod.compareTo(period));
        assertEquals(0, period.hashCode());
    }

    static class MockDurationFieldType implements DurationFieldType {
        @Override
        public String getName() {
            return "MockFieldType";
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public DurationField newField(Chronology chronology) {
            return null;
        }
    }

    static class MockPeriodType implements PeriodType {
        @Override
        public String getID() {
            return "MockPeriodType";
        }

        @Override
        public Period newPeriod(long value, Chronology instant) {
            return null;
        }

        @Override
        public DurationFieldType[] getFields() {
            return new DurationFieldType[0];
        }

        @Override
        public long getMillisPerUnit(Chronology chronology) {
            return FieldUtils.millisPerDay();
        }

        @Override
        public boolean isContiguous() {
            return true;
        }

        @Override
        public int getDaysInMonth(int year, int month, Chronology chronology) {
            return ISOChronology.getInstanceUTC().getDaysInMonth(year, month);
        }

        @Override
        public int getDaysInYear(int year, Chronology chronology) {
            return ISOChronology.getInstanceUTC().getDaysInYear(year);
        }

        @Override
        public long getLeapYearTestValue(int year, Chronology chronology) {
            return 0;
        }

        @Override
        public boolean isLeapYear(int year, Chronology chronology) {
            return ISOChronology.getInstanceUTC().isLeapYear(year);
        }

        @Override
        public long getMillisPerUnitWithoutAdjustments(Chronology chronology) {
            return FieldUtils.millisPerDay();
        }

        @Override
        public long getMillis(int value, Chronology chronology) {
            return FieldUtils.safeMultiply(FieldUtils.millisPerDay(), value);
        }

        @Override
        public int getPreciseLengthInMilliseconds() {
            return FieldUtils.millisPerDay();
        }

        @Override
        public boolean isFixedWidth() {
            return true;
        }
    }
}