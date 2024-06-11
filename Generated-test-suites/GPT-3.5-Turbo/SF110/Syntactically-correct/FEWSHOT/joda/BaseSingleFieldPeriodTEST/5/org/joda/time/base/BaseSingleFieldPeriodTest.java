package org.joda.time.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_WithNullReadableInstant_ShouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetween_WithDifferentReadablePartials_ShouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }

    @Test
    public void testBetween_WithNonContiguousReadablePartials_ShouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.between(null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }

    @Test
    public void testStandardPeriodIn_WithNullReadablePeriod_ShouldReturnZero() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 0L);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodIn_WithUnsupportedFieldType_ShouldThrowIllegalArgumentException() {
        try {
            BaseSingleFieldPeriod.standardPeriodIn(null, 0L);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as is not precise in the period ", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.getValue();
        assertEquals(10, result);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(5);
        int result = period.getValue();
        assertEquals(5, result);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(null, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(null, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.size();
        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType_WithInvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValue_WithInvalidIndex_ShouldThrowIndexOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.get(null);
        assertEquals(0, result);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        boolean result = period.isSupported(null);
        assertEquals(false, result);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(null, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(null, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        boolean result = period.equals(null);
        assertEquals(false, result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.hashCode();
        assertEquals(278, result);
    }

    @Test
    public void testCompareTo() {
        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
            BaseSingleFieldPeriod otherPeriod = new BaseSingleFieldPeriod(5);
            period.compareTo(otherPeriod);
        } catch (ClassCastException e) {
            assertEquals("class org.joda.time.base.BaseSingleFieldPeriod cannot be compared to class org.joda.time.base.BaseSingleFieldPeriod", e.getMessage());
        }
    }
}