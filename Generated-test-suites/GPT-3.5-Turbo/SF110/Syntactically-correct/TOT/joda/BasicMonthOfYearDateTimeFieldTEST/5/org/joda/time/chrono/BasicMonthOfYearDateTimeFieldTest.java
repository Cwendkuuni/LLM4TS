package org.joda.time.chrono;

import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static BasicMonthOfYearDateTimeField field;
    private static BasicChronology chronology;

    @BeforeClass
    public static void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }

    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long n = 1586822400000L; // April 1, 2020
        assertEquals(4, field.get(n));
    }

    @Test
    public void testAdd_WithZero() {
        long n = 1586822400000L; // April 1, 2020
        long result = field.add(n, 0);
        assertEquals(n, result);
    }

    @Test
    public void testAdd_WithPositiveNumber() {
        long n = 1586822400000L; // April 1, 2020
        int n2 = 1;
        long result = field.add(n, n2);
        assertEquals(1589500800000L, result); // May 1, 2020
    }

    @Test
    public void testAdd_WithNegativeNumber() {
        long n = 1586822400000L; // April 1, 2020
        int n2 = -4;
        long result = field.add(n, n2);
        assertEquals(1579372800000L, result); // December 1, 2019
    }

    @Test
    public void testAdd_WithLargePositiveNumber() {
        long n = 1586822400000L; // April 1, 2020
        int n2 = 26;
        long result = field.add(n, n2);
        assertEquals(1612132800000L, result); // June 1, 2021
    }

    @Test
    public void testAdd_WithLargeNegativeNumber() {
        long n = 1586822400000L; // April 1, 2020
        int n2 = -28;
        long result = field.add(n, n2);
        assertEquals(1546300800000L, result); // February 1, 2019
    }

    @Test
    public void testAdd_WithLongValue() {
        long n = 1586822400000L; // April 1, 2020
        long n2 = 1L;
        long result = field.add(n, n2);
        assertEquals(1589500800000L, result); // May 1, 2020
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_WithException() {
        long n = 1586822400000L; // April 1, 2020
        long n2 = 72576000000001L; // 25,001 years
        field.add(n, n2);
    }

    @Test
    public void testAddWrapField() {
        long n = 1586822400000L; // April 1, 2020
        int n2 = 1;
        long result = field.addWrapField(n, n2);
        assertEquals(1589500800000L, result); // May 1, 2020
    }

    @Test
    public void testGetDifferenceAsLong() {
        long n = 1617235200000L; // April 1, 2021
        long set = 1577836800000L; // January 1, 2020
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(15, result);
    }

    @Test
    public void testSet() {
        long n = 1617235200000L; // April 1, 2021
        int n2 = 7;
        long result = field.set(n, n2);
        assertEquals(1633046400000L, result); // September 1, 2021
    }

    @Test
    public void testIsLeap() {
        long n = 1617235200000L; // April 1, 2021
        assertFalse(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        long n = 1617235200000L; // April 1, 2021
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
        long n = 1617235200000L; // April 1, 2021
        long result = field.roundFloor(n);
        assertEquals(1614556800000L, result); // March 1, 2021
    }

    @Test
    public void testRemainder() {
        long n = 1617235200000L; // April 1, 2021
        long result = field.remainder(n);
        assertEquals(2678400000L, result); // 31 days
    }

}