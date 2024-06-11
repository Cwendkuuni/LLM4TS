package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest{
    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    @Before
    public void setUp() {
        BasicChronology iChronology = new BasicChronology();
        int iLeapMonth = 6;
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(iChronology, iLeapMonth);
    }

    @Test
    public void testIsLenient() {
        boolean isLenient = basicMonthOfYearDateTimeField.isLenient();
        assertFalse(isLenient);
    }

    @Test
    public void testGet() {
        long n = 1234567890L;
        int val = basicMonthOfYearDateTimeField.get(n);
        assertEquals(5, val);
    }

    @Test
    public void testAddWithIntZero() {
        long n = 1234567890L;
        int n2 = 0;
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(n, result);
    }

    @Test
    public void testAddWithIntNonZero() {
        long n = 1234567890L;
        int n2 = 3;
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(1235165490L, result);
    }

    @Test
    public void testAddWithLongAndIntZero() {
        long n = 1234567890L;
        long n2 = 0L;
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(n, result);
    }

    @Test
    public void testAddWithLongAndIntNonZero() {
        long n = 1234567890L;
        long n2 = 3L;
        long result = basicMonthOfYearDateTimeField.add(n, n2);
        assertEquals(1235165490L, result);
    }

    @Test
    public void testAddWrapField() {
        long n = 1234567890L;
        int n2 = 3;
        long result = basicMonthOfYearDateTimeField.addWrapField(n, n2);
        assertEquals(1235165490L, result);
    }

    @Test
    public void testGetDifferenceAsLongWithLesserStartAndGreaterEnd() {
        long n = 1234567890L;
        long set = 9876543210L;
        long diff = basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set);
        assertEquals(-434L, diff);
    }

    @Test
    public void testGetDifferenceAsLongWithGreaterStartAndLesserEnd() {
        long n = 9876543210L;
        long set = 1234567890L;
        long diff = basicMonthOfYearDateTimeField.getDifferenceAsLong(n, set);
        assertEquals(434L, diff);
    }

    @Test
    public void testSet() {
        long n = 1234567890L;
        int n2 = 4;
        long result = basicMonthOfYearDateTimeField.set(n, n2);
        assertEquals(1234567290L, result);
    }

    @Test
    public void testIsLeap() {
        long n = 1234567890L;
        boolean isLeap = basicMonthOfYearDateTimeField.isLeap(n);
        assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        long n = 1234567890L;
        int leapAmount = basicMonthOfYearDateTimeField.getLeapAmount(n);
        assertEquals(0, leapAmount);
    }

    @Test
    public void testGetMinimumValue() {
        int min = basicMonthOfYearDateTimeField.getMinimumValue();
        assertEquals(1, min);
    }

    @Test
    public void testGetMaximumValue() {
        int max = basicMonthOfYearDateTimeField.getMaximumValue();
        assertEquals(Integer.MAX_VALUE, max);
    }

    @Test
    public void testRoundFloor() {
        long n = 1234567890L;
        long result = basicMonthOfYearDateTimeField.roundFloor(n);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testRemainder() {
        long n = 1234567890L;
        long result = basicMonthOfYearDateTimeField.remainder(n);
        assertEquals(0L, result);
    }
}