package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween_Instant_WithNull() {
        try {
            int result = BaseSingleFieldPeriod.between(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testBetween_Partial_WithNull() {
        try {
            int result = BaseSingleFieldPeriod.between(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testBetween_Partial_SizeMismatch() {
        try {
            int result = BaseSingleFieldPeriod.between(new ReadablePartial(), new ReadablePartial(), new ReadablePeriod());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testBetween_Partial_FieldMismatch() {
        try {
            int result = BaseSingleFieldPeriod.between(new ReadablePartial(), new ReadablePartial(), new ReadablePeriod());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testBetween_Partial_NotContiguous() {
        try {
            int result = BaseSingleFieldPeriod.between(new ReadablePartial(), new ReadablePartial(), new ReadablePeriod());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testStandardPeriodIn_NullReadablePeriod() {
        int result = BaseSingleFieldPeriod.standardPeriodIn(null, 0);
        assertEquals(0, result);
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
        period.setValue(20);
        int result = period.getValue();
        assertEquals(20, result);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.size();
        assertEquals(1, result);
    }

    @Test
    public void testGetFieldType_WithIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected exception
        }
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType result = period.getFieldType(0);
        assertEquals(null, result);
    }

    @Test
    public void testGetValue_WithIndexOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected exception
        }
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.getValue(0);
        assertEquals(10, result);
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
        Period result = period.toPeriod();
        assertEquals(Period.ZERO.withFields(period), result);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        MutablePeriod result = period.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals_SameObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        boolean result = period.equals(period);
        assertEquals(true, result);
    }

    @Test
    public void testEquals_DifferentObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        boolean result = period1.equals(period2);
        assertEquals(false, result);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int result = period.hashCode();
        int expected = 27 * (27 * 17 + period.getValue()) + period.getFieldType().hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareTo_SameClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareTo_DifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        try {
            period1.compareTo(null);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            //Expected exception
        }
    }

}