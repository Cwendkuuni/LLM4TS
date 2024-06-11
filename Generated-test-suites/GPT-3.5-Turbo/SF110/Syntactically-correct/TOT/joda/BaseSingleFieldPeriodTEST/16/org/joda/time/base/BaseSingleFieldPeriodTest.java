package org.joda.time.base;

import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithNullReadableInstant() {
        try {
            BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetweenWithNullDurationFieldType() {
        try {
            BaseSingleFieldPeriod.between(new DateTime(), new DateTime(), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetweenWithReadableInstantInDifferentChronology() {
        try {
            BaseSingleFieldPeriod.between(new DateTime(), new DateTime(BuddhistChronology.getInstance()), DurationFieldType.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as year is not precise in the period", e.getMessage());
        }
    }

    @Test
    public void testBetweenWithReadablePartialInDifferentFields() {
        try {
            BaseSingleFieldPeriod.between(new YearMonth(2019, 1), new YearMonth(2020, 1), new Period(1, 1, 1, 1));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }

    @Test
    public void testBetweenWithNonContiguousReadablePartial() {
        try {
            BaseSingleFieldPeriod.between(new LocalDate(2019, 1, 1), new LocalDate(2019, 3, 1), new Period(1, 0, 1));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }

    @Test
    public void testStandardPeriodInWithNullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithInvalidFieldType() {
        BaseSingleFieldPeriod.standardPeriodIn(new Period(1, 2, 3, 4), 1);
    }

    @Test
    public void testStandardPeriodInWithZeroValue() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(new Period(0), 1);
        assertEquals(0, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.getValue();
        assertEquals(5, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        int result = period.getValue();
        assertEquals(10, result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.size();
        assertEquals(1, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType result = period.getFieldType(0);
        assertEquals(DurationFieldType.years(), result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.getValue(0);
        assertEquals(5, result);
    }

    @Test
    public void testGetWithUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.get(DurationFieldType.hours());
        assertEquals(0, result);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.get(DurationFieldType.years());
        assertEquals(5, result);
    }

    @Test
    public void testIsSupportedWithUnsupportedFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean result = period.isSupported(DurationFieldType.hours());
        assertFalse(result);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        boolean result = period.isSupported(DurationFieldType.years());
        assertTrue(result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(5, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(5, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void testEqualsWithDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = new Period(5);
        boolean result = period1.equals(period2);
        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentPeriodType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            public PeriodType getPeriodType() {
                return PeriodType.days();
            }

            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            public PeriodType getPeriodType() {
                return PeriodType.months();
            }

            public DurationFieldType getFieldType() {
                return DurationFieldType.months();
            }
        };
        boolean result = period1.equals(period2);
        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        boolean result = period1.equals(period2);
        assertFalse(result);
    }

    @Test
    public void testEqualsWithSameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        boolean result = period1.equals(period2);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int result = period.hashCode();
        assertEquals(778, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = new Period(5);
        period1.compareTo(period2);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentFieldType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            public DurationFieldType getFieldType() {
                return DurationFieldType.months();
            }
        };
        period1.compareTo(period2);
    }

    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareToWithLesserValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

}