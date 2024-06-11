package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseSingleFieldPeriodTest {

    @org.junit.Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(123, period.getValue());
    }

    @org.junit.Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }
        };
        assertEquals(DurationFieldType.HOURS, period.getFieldType());
    }

    @org.junit.Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @org.junit.Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(1, period.size());
    }

    @org.junit.Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(123, period.getValue(0));
    }

    @org.junit.Test
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        try {
            period.getValue(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetValueNullType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        try {
            period.get(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }
        };
        assertFalse(period.isSupported(DurationFieldType.HOURS));
        assertTrue(period.isSupported(DurationFieldType.MINUTES));
    }

    @org.junit.Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        Period result = period.toPeriod();
        assertEquals(123, result.getHours());
    }

    @org.junit.Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(123, result.getHours());
    }

    @org.junit.Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }
        };

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
    }

    @org.junit.Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(456);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @org.junit.Test
    public void testCompareToSame() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        assertEquals(0, period1.compareTo(period2));
    }

    @org.junit.Test
    public void testCompareToDifferent() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(456);
        int result = period1.compareTo(period2);
        assertTrue(result < 0);
    }

    @org.junit.Test
    public void testCompareToSameClassDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(567);
        int result = period1.compareTo(period2);
        assertTrue(result < 0);
    }

    @org.junit.Test
    public void testBetween() {
        long startMillis = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        long endMillis = System.currentTimeMillis();
        Chronology isoChronology = ISOChronology.getInstanceUTC();
        int result = BaseSingleFieldPeriod.between(isoChronology.instant(startMillis), isoChronology.instant(endMillis), DurationFieldType.HOURS);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(result);
        assertEquals(period, new BaseSingleFieldPeriod(DurationFieldType.HOURS, result));
    }

    @org.junit.Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(2, 3, 4, 5, 6, 7, 8);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, Seconds.MILLIS_PER_SECOND);
        assertEquals(2 * 24 * 3 * 60 * 4 * 60 * 5 * 60 * 6 * 60 * 7 * 60 * 8 * 60, result * Seconds.MILLIS_PER_SECOND);
    }
}