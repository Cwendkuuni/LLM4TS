package org.joda.time.base;

import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithReadableInstant() {
        // Typical use case
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2020, 1, 2, 0, 0);
        int expected1 = 1;
        assertEquals(expected1, BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.days()));

        // Edge case: same instant
        ReadableInstant instant3 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant4 = new DateTime(2020, 1, 1, 0, 0);
        int expected2 = 0;
        assertEquals(expected2, BaseSingleFieldPeriod.between(instant3, instant4, DurationFieldType.days()));

        // Error scenario: null ReadableInstant objects
        try {
            BaseSingleFieldPeriod.between(null, instant2, DurationFieldType.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }

        try {
            BaseSingleFieldPeriod.between(instant1, null, DurationFieldType.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetweenWithReadablePartial() {
        // Typical use case
        ReadablePartial partial1 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate();
        ReadablePartial partial2 = new LocalDateTime(2020, 1, 2, 0, 0).toLocalDate();
        ReadablePeriod period = Period.days(1);
        int expected1 = 1;
        assertEquals(expected1, BaseSingleFieldPeriod.between(partial1, partial2, period));

        // Edge case: same partial
        ReadablePartial partial3 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate();
        ReadablePartial partial4 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate();
        int expected2 = 0;
        assertEquals(expected2, BaseSingleFieldPeriod.between(partial3, partial4, period));

        // Error scenarios
        try {
            BaseSingleFieldPeriod.between(null, partial2, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }

        try {
            BaseSingleFieldPeriod.between(partial1, null, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }

        ReadablePartial partial5 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate();
        ReadablePartial partial6 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalTime();
        try {
            BaseSingleFieldPeriod.between(partial5, partial6, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }

        ReadablePartial partial7 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate();
        ReadablePartial partial8 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDate().withYear(2021);
        try {
            BaseSingleFieldPeriod.between(partial7, partial8, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }

        ReadablePartial partial9 = new LocalDateTime(2020, 1, 1, 0, 0).toLocalDateTime();
        try {
            BaseSingleFieldPeriod.between(partial9, partial2, period);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }

    @Test
    public void testStandardPeriodIn() {
        // Typical use case
        ReadablePeriod period = Period.days(2).withMillis(500);
        long n = 1000;
        int expected1 = 1;
        assertEquals(expected1, BaseSingleFieldPeriod.standardPeriodIn(period, n));

        // Edge case: null ReadablePeriod
        ReadablePeriod period2 = null;
        int expected2 = 0;
        assertEquals(expected2, BaseSingleFieldPeriod.standardPeriodIn(period2, n));
    }

    @Test
    public void testGetFieldType() {
        // There are two concrete sub-classes of BaseSingleFieldPeriod, so this method should be overridden there
        // and it is not meant to be overridden in this abstract base class
        fail("This test method should be overridden in the concrete sub-classes of BaseSingleFieldPeriod");
    }

    @Test
    public void testGetPeriodType() {
        // There are two concrete sub-classes of BaseSingleFieldPeriod, so this method should be overridden there
        // and it is not meant to be overridden in this abstract base class
        fail("This test method should be overridden in the concrete sub-classes of BaseSingleFieldPeriod");
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new Days(1);
        int expected = 1;
        assertEquals(expected, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        int index1 = 0;
        assertEquals(DurationFieldType.days(), period1.getFieldType(index1));

        // Error scenario
        try {
            BaseSingleFieldPeriod period2 = new Days(1);
            int index2 = 1;
            period2.getFieldType(index2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValueAtIndex() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        int index1 = 0;
        int expected1 = 1;
        assertEquals(expected1, period1.getValue(index1));

        // Error scenario
        try {
            BaseSingleFieldPeriod period2 = new Days(1);
            int index2 = 1;
            period2.getValue(index2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        DurationFieldType fieldType1 = DurationFieldType.days();
        int expected1 = 1;
        assertEquals(expected1, period1.get(fieldType1));

        // Error scenario
        DurationFieldType fieldType2 = DurationFieldType.hours();
        int expected2 = 0;
        assertEquals(expected2, period1.get(fieldType2));
    }

    @Test
    public void testIsSupported() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        DurationFieldType fieldType1 = DurationFieldType.days();
        assertTrue(period1.isSupported(fieldType1));

        // Error scenario
        DurationFieldType fieldType2 = DurationFieldType.hours();
        assertFalse(period1.isSupported(fieldType2));
    }

    @Test
    public void testToPeriod() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        Period expected1 = Period.days(1);
        assertEquals(expected1, period1.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        MutablePeriod expected1 = Period.days(1).toMutablePeriod();
        assertEquals(expected1, period1.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        // Typical use case: same period type and value
        BaseSingleFieldPeriod period1 = new Days(1);
        ReadablePeriod period2 = Period.days(1);
        assertTrue(period1.equals(period2));

        // Typical use case: different period type
        BaseSingleFieldPeriod period3 = new Days(1);
        ReadablePeriod period4 = Period.hours(24);
        assertFalse(period3.equals(period4));

        // Typical use case: different value
        BaseSingleFieldPeriod period5 = new Days(1);
        ReadablePeriod period6 = Period.days(2);
        assertFalse(period5.equals(period6));

        // Typical use case: different class
        BaseSingleFieldPeriod period7 = new Days(1);
        Object period8 = new Object();
        assertFalse(period7.equals(period8));
    }

    @Test
    public void testHashCode() {
        // Typical use case
        BaseSingleFieldPeriod period1 = new Days(1);
        int expected1 = 133;
        assertEquals(expected1, period1.hashCode());
    }

    @Test
    public void testCompareTo() {
        // Typical use case: smaller period
        BaseSingleFieldPeriod period1 = new Days(1);
        BaseSingleFieldPeriod period2 = new Days(2);
        int expected1 = -1;
        assertEquals(expected1, period1.compareTo(period2));

        // Typical use case: larger period
        BaseSingleFieldPeriod period3 = new Days(1);
        BaseSingleFieldPeriod period4 = new Days(0);
        int expected2 = 1;
        assertEquals(expected2, period3.compareTo(period4));

        // Typical use case: equal period
        BaseSingleFieldPeriod period5 = new Days(1);
        BaseSingleFieldPeriod period6 = new Days(1);
        int expected3 = 0;
        assertEquals(expected3, period5.compareTo(period6));

        // Error scenario: different classes
        try {
            BaseSingleFieldPeriod period7 = new Days(1);
            BaseSingleFieldPeriod period8 = new Hours(24);
            period7.compareTo(period8);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("class org.joda.time.base.Days cannot be compared to class org.joda.time.Hours", e.getMessage());
        }
    }
}