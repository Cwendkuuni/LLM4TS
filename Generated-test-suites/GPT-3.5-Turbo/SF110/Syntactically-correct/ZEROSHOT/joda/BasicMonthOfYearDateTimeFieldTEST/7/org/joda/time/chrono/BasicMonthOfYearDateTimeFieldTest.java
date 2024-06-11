package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int expected = 0;
        int result = field.get(n);
        assertEquals(expected, result);
    }

    @Test
    public void testAddIntLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 0;
        long expected = 0;
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddLongLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long n2 = 0;
        long expected = 0;
        long result = field.add(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testAddReadablePartialIntArrayInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        ReadablePartial readablePartial = null;
        int n = 0;
        int[] array = null;
        int n2 = 0;
        int[] expected = null;
        int[] result = field.add(readablePartial, n, array, n2);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 0;
        long expected = 0;
        long result = field.addWrapField(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long set = 0;
        long expected = 0;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(expected, result);
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int n2 = 0;
        long expected = 0;
        long result = field.set(n, n2);
        assertEquals(expected, result);
    }

    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        DurationField result = field.getRangeDurationField();
        assertNotNull(result);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        boolean expected = false;
        boolean result = field.isLeap(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        int expected = 0;
        int result = field.getLeapAmount(n);
        assertEquals(expected, result);
    }

    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        DurationField result = field.getLeapDurationField();
        assertNotNull(result);
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int expected = 1;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        int expected = 0;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long expected = 0;
        long result = field.roundFloor(n);
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        long n = 0;
        long expected = 0;
        long result = field.remainder(n);
        assertEquals(expected, result);
    }
}