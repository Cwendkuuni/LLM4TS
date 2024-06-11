package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDate;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        DurationFieldType days = DurationFieldType.days();
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.hours();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test equals
        assertEquals(period1, period1);
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
        assertNotEquals(period1, new Object());

        // Test hashCode
        int hashCodePeriod1 = period1.hashCode();
        int hashCodePeriod2 = period2.hashCode();
        assertEquals(hashCodePeriod1, period1.hashCode());
        assertEquals(hashCodePeriod2, period2.hashCode());
        assertNotEquals(hashCodePeriod1, hashCodePeriod2);

        // Test compareTo
        assertEquals(0, period1.compareTo(period1));
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(-1, period1.compareTo(period3));
        assertEquals(1, period3.compareTo(period1));
        assertEquals(0, period1.compareTo(period4));
        assertEquals(0, period4.compareTo(period1));

        // Test between
        ReadableInstant start = new BaseDate().withZoneRetainFields(DateTimeConstants.MINUTE_OF_JANUARY_1, 2012, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDate().withZoneRetainFields(DateTimeConstants.MINUTE_OF_JANUARY_1, 2012, 1, 1, 0, 0, 2, 0, ISOChronology.getInstanceUTC()).toInstant();
        int daysBetween = BaseSingleFieldPeriod.between(start, end, days);
        assertEquals(2, daysBetween);

        // Test standardPeriodIn
        ReadablePeriod period = new Period(2, DateTimeConstants.DAYS, PeriodType.standard());
        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(period, days.getMillisPerUnit());
        assertEquals(2, standardPeriod);

        // Test toPeriod
        Period toPeriod = period1.toPeriod();
        assertEquals(1, toPeriod.getYears());
        assertEquals(0, toPeriod.getMonths());
        assertEquals(1, toPeriod.getDays());
        assertEquals(0, toPeriod.getHours());
        assertEquals(0, toPeriod.getMinutes());
        assertEquals(0, toPeriod.getSeconds());
        assertEquals(0, toPeriod.getMillis());

        // Test toMutablePeriod
        MutablePeriod toMutablePeriod = period1.toMutablePeriod();
        assertEquals(1, toMutablePeriod.getYears());
        assertEquals(0, toMutablePeriod.getMonths());
        assertEquals(1, toMutablePeriod.getDays());
        assertEquals(0, toMutablePeriod.getHours());
        assertEquals(0, toMutablePeriod.getMinutes());
        assertEquals(0, toMutablePeriod.getSeconds());
        assertEquals(0, toMutablePeriod.getMillis());

        // Test getFieldType and getPeriodType
        DurationFieldType fieldType = period1.getFieldType();
        assertEquals(days, fieldType);
        PeriodType periodType = period1.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);

        // Test size
        int size = period1.size();
        assertEquals(1, size);

        // Test getValue
        int value = period1.getValue();
        assertEquals(1, value);

        // Test getValue with invalid index
        try {
            period1.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test getValue with valid index
        int valueGet = period1.getValue(0);
        assertEquals(1, valueGet);

        // Test get
        int getDays = period1.get(days);
        assertEquals(1, getDays);

        // Test isSupported
        boolean supported = period1.isSupported(days);
        assertTrue(supported);

        // Test isSupported with invalid field type
        boolean notSupported = period1.isSupported(DurationFieldType.weeks());
        assertFalse(notSupported);

        // Test toString
        String toString = period1.toString();
        String expectedToString = "P1D";
        assertEquals(expectedToString, toString);

        // Test compareTo with null
        try {
            period1.compareTo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test compareTo with incorrect class
        try {
            period1.compareTo(new Object());
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }

        // Test equals with null
        assertFalse(period1.equals(null));

        // Test equals with different class
        assertFalse(period1.equals(new Object()));

        // Test equals with different period type
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.fixed(DateTimeConstants.MILLIS_PER_DAY, 2);
            }
        };
        assertFalse(period1.equals(period5));

        // Test equals with same period but different value
        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(period1.equals(period6));

        // Test equals with same period but different chronology
        BaseSingleFieldPeriod period7 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseDate baseDate = new BaseDate(2012, 1, 1).withChronology(DateTimeConstants.JAPAN_LOCAL);
        ReadableInstant startJapan = baseDate.toInstant();
        ReadableInstant endJapan = baseDate.withFieldAdded(DateTimeConstants.DAY_OF_MONTH, 2).toInstant();
        BaseSingleFieldPeriod period8 = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(startJapan, endJapan, days));
        assertFalse(period1.equals(period8));
    }
}