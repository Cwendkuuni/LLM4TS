package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test between two instants
        Chronology chrono = ISOChronology.getInstanceUTC();
        ReadableInstant start = new BaseDateTime(1234567890000L, chrono);
        ReadableInstant end = new BaseDateTime(1234567895000L, chrono);
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(5, period);

        // Test between two partials
        ReadablePartial startPartial = new org.joda.time.local.LocalDate(2015, 1, 1);
        ReadablePartial endPartial = new org.joda.time.local.LocalDate(2015, 1, 5);
        period = BaseSingleFieldPeriod.between(startPartial, endPartial, Period.days());
        assertEquals(4, period);

        // Test invalid partials
        assertNull(BaseSingleFieldPeriod.between(null, null, Period.days()));
        assertNull(BaseSingleFieldPeriod.between(startPartial, null, Period.days()));
        assertNull(BaseSingleFieldPeriod.between(null, endPartial, Period.days()));

        // Test null duration field type
        try {
            BaseSingleFieldPeriod.between(start, end, null);
            fail("Null pointer exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test different chronology
        Chronology differentChrono = DateTimeUtils.getInstantChronology(start);
        assertNotEquals(chrono, differentChrono);
        try {
            BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
            fail("Illegal argument exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStandardPeriodIn() {
        // Test with valid period
        ReadablePeriod period = new Period(5, 5, 5, 5, 5, 5, 5, PeriodType.standard());
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 3600000);
        assertEquals(5 * 24 + 5, hours);

        // Test with null period
        int hoursNull = BaseSingleFieldPeriod.standardPeriodIn(null, 3600000);
        assertEquals(0, hoursNull);

        // Test with null millisPerUnit
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period, 0);
            fail("Null pointer exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructors() {
        // Test int constructor
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        assertEquals(5, period1.getValue());
        assertEquals(DurationFieldType.days(), period1.getFieldType());

        // Test int constructor with null parameter
        try {
            new BaseSingleFieldPeriod(null);
            fail("Null pointer exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null constructor
        try {
            new BaseSingleFieldPeriod(null);
            fail("Null pointer exception expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAndSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue();
        period.setValue(10);
        int newValue = period.getValue();
        assertEquals(10, newValue);
        assertEquals(DurationFieldType.days(), period.getFieldType());
        value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testGetFieldTypeAndGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAndGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType expectedFieldType = DurationFieldType.days();
        assertEquals(expectedFieldType, period.getFieldType());
        int expectedValue = 5;
        assertEquals(expectedValue, period.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expectedPeriod = new Period(5, 0, 0, 0, 0, 0, 0, PeriodType.standard());
        Period resultPeriod = period.toPeriod();
        assertEquals(expectedPeriod, resultPeriod);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expectedPeriod = new MutablePeriod().withDays(5);
        MutablePeriod resultPeriod = period.toMutablePeriod();
        assertEquals(expectedPeriod, resultPeriod);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod("foo");
        BaseSingleFieldPeriod period6 = new BaseSingleFieldPeriod(5, DurationFieldType.weeks());

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
        assertNotEquals(period1, period5);
        assertNotEquals(period1, period6);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);

        int hashCodePeriod1 = period1.hashCode();
        int hashCodePeriod2 = period2.hashCode();
        int hashCodePeriod3 = period3.hashCode();

        assertEquals(hashCodePeriod1, hashCodePeriod2);
        assertNotEquals(hashCodePeriod1, hashCodePeriod3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6);

        int compareResult = period1.compareTo(period2);
        int expectedCompareResult = -1;

        assertEquals(expectedCompareResult, compareResult);
    }
}