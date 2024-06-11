package org.joda.time.chrono;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field;

    @Before
    public void setup() {
        BasicChronology chronology = new BasicChronology();
        int leapMonth = 2;
        field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long n = 1234567890L;
        int result = field.get(n);
        assertEquals(2, result);
    }

    @Test
    public void testAddWithZeroValue() {
        long n = 1234567890L;
        int n2 = 0;
        long result = field.add(n, n2);
        assertEquals(n, result);
    }

    @Test
    public void testAddWithPositiveValue() {
        long n = 1234567890L;
        int n2 = 4;
        long result = field.add(n, n2);
        assertEquals(1235547890L, result);
    }

    @Test
    public void testAddWithNegativeValue() {
        long n = 1234567890L;
        int n2 = -2;
        long result = field.add(n, n2);
        assertEquals(1233874890L, result);
    }

    @Test
    public void testAddWithLongValue() {
        long n = 1234567890L;
        long n2 = 4567890123L;
        long result = field.add(n, n2);
        assertEquals(5802458013L, result);
    }

    @Test
    public void testAddWrapField() {
        long n = 1234567890L;
        int n2 = 4;
        long result = field.addWrapField(n, n2);
        assertEquals(1235547890L, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long n = 1234567890L;
        long set = 9876543210L;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(-88, result);
    }

    @Test
    public void testSet() {
        long n = 1234567890L;
        int n2 = 8;
        long result = field.set(n, n2);
        assertEquals(1255568490L, result);
    }

    @Test
    public void testIsLeap() {
        long n = 1234567890L;
        boolean result = field.isLeap(n);
        assertFalse(result);
    }

    @Test
    public void testGetLeapAmount() {
        long n = 1234567890L;
        int result = field.getLeapAmount(n);
        assertEquals(0, result);
    }

    @Test
    public void testGetMinimumValue() {
        int result = field.getMinimumValue();
        assertEquals(1, result);
    }

    @Test
    public void testGetMaximumValue() {
        int result = field.getMaximumValue();
        assertEquals(12, result);
    }

    @Test
    public void testRoundFloor() {
        long n = 1234567890L;
        long result = field.roundFloor(n);
        assertEquals(1230768000L, result);
    }

    @Test
    public void testRemainder() {
        long n = 1234567890L;
        long result = field.remainder(n);
        assertEquals(37968000L, result);
    }
}