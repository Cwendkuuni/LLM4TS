package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

    abstract class TestBaseSingleFieldPeriod extends BaseSingleFieldPeriod {
        private static final long serialVersionUID = 1L;
        private static final DurationFieldType FIELD_TYPE = DurationFieldType.DAYS;
        private static final PeriodType PERIOD_TYPE = PeriodType.standard();

        TestBaseSingleFieldPeriod(int period) {
            super(period);
        }

        DurationFieldType getFieldType() {
            return FIELD_TYPE;
        }

        PeriodType getPeriodType() {
            return PERIOD_TYPE;
        }
    }

    @org.junit.Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(5);
        long millisPerUnit = 24 * 60 * 60 * 1000; // hours
        long expected = 5 * 24 * 60 * 60 * 1000; // hours
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithImprecisePeriod() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(2) {
            @Override
            public int size() {
                return 2;
            }
            @Override
            public DurationFieldType getFieldType(int index) {
                if (index == 1) {
                    return DurationFieldType.MONTHS;
                }
                return super.getFieldType(index);
            }
            @Override
            public int getValue(int index) {
                if (index == 1) {
                    return 3;
                }
                return super.getValue(index);
            }
        };
        long millisPerUnit = 24 * 60 * 60 * 1000; // hours
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    @org.junit.Test
    public void testBetween() {
        ReadableInstant start = new BaseDateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1972, 1, 5, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        int expected = 5;
        int actual = BaseSingleFieldPeriod.between(start, end, FIELD_TYPE);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testBetweenWithInvalidObjects() {
        try {
            BaseSingleFieldPeriod.between(null, null, FIELD_TYPE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            ReadableInstant start = new BaseDateTime(1972, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
            ReadableInstant end = null;
            BaseSingleFieldPeriod.between(start, end, FIELD_TYPE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetValue() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(7);
        int expected = 7;
        int actual = period.getValue();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetFieldType() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(3);
        DurationFieldType expected = FIELD_TYPE;
        DurationFieldType actual = period.getFieldType();
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(3);
        period.getFieldType(2);
    }

    @org.junit.Test
    public void testSize() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(3);
        int expected = 1;
        int actual = period.size();
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSizeWithInvalidIndex() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(3);
        period.size(2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetValueWithInvalidIndex() {
        ReadablePeriod period = new TestBaseSingleFieldPeriod(3);
        period.getValue(2);
    }

    @org.junit.Test
    public void testCompareTo() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(2);
        TestBaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(4);
        int expected = -2;
        int actual = period1.compareTo(period2);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCompareToWithSelf() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        int expected = 0;
        int actual = period.compareTo(period);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCompareToWithEqualPeriods() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(3);
        TestBaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(3);
        int expected = 0;
        int actual = period1.compareTo(period2);
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(3);
        Object period2 = new Object();
        int actual = period1.compareTo((BaseSingleFieldPeriod) period2);
        fail();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testCompareToWithNull() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        int actual = period.compareTo(null);
        fail();
    }

    @org.junit.Test
    public void testHashCode() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        long expected = FieldUtils.safeToLong(3);
        long actual = period.hashCode();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEquals() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(3);
        TestBaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(3);
        boolean expected = true;
        boolean actual = period1.equals(period2);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEqualsWithDifferentClass() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        Object other = new Object();
        boolean expected = false;
        boolean actual = period.equals(other);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEqualsWithNull() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        boolean expected = false;
        boolean actual = period.equals(null);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEqualsWithDifferentFieldType() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }
        };
        TestBaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(3);
        boolean expected = false;
        boolean actual = period1.equals(period2);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEqualsWithDifferentValue() {
        TestBaseSingleFieldPeriod period1 = new TestBaseSingleFieldPeriod(3);
        TestBaseSingleFieldPeriod period2 = new TestBaseSingleFieldPeriod(4);
        boolean expected = false;
        boolean actual = period1.equals(period2);
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidChronology() {
        ReadableInstant start = new BaseDateTime(1972, 1, 1, 0, 0, 0, 0, DateTimeUtils.JULIAN_DAY).toInstant();
        ReadableInstant end = new BaseDateTime(1972, 1, 5, 0, 0, 0, 0, DateTimeUtils.JULIAN_DAY).toInstant();
        BaseSingleFieldPeriod.between(start, end, FIELD_TYPE);
    }

    @org.junit.Test
    public void testToPeriod() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(2);
        Period expected = new Period(new DurationFieldType[] { FIELD_TYPE }, new long[] { 2 * 24 * 60 * 60 * 1000 }, 1, new PeriodType[] { PeriodType.standard() }, FIELD_TYPE.getMillisPerUnit());
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testToMutablePeriod() {
        TestBaseSingleFieldPeriod period = new TestBaseSingleFieldPeriod(3);
        MutablePeriod expected = new MutablePeriod(new DurationFieldType[] { FIELD_TYPE }, new int[] { 3 }, new PeriodType[] { PeriodType.standard() });
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }
}