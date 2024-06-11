package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.junit.Test;
import org.joda.time.*;
import org.joda.time.field.*;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;


public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(monthOfYear.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        assertEquals(chronology.getMonthOfYear(n), monthOfYear.get(n));
    }

    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        int n2 = 10;
        long expected = chronology.add(n, n2);
        long actual = monthOfYear.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        long n2 = 10;
        long expected = chronology.add(n, (int)n2);
        long actual = monthOfYear.add(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        int n2 = 10;
        long expected = chronology.addWrapField(n, n2);
        long actual = monthOfYear.addWrapField(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        long n2 = 87654321;
        long expected = chronology.getDifferenceAsLong(n, n2);
        long actual = monthOfYear.getDifferenceAsLong(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        int n2 = 10;
        long expected = chronology.set(n, n2);
        long actual = monthOfYear.set(n, n2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        int expected = monthOfYear.isLeap(n) ? 1 : 0;
        int actual = monthOfYear.getLeapAmount(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = 1;
        int actual = monthOfYear.getMinimumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = monthOfYear.iMax;
        int actual = monthOfYear.getMaximumValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        long expected = chronology.roundFloor(n);
        long actual = monthOfYear.roundFloor(n);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField monthOfYear = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 12345678;
        long expected = n - monthOfYear.roundFloor(n);
        long actual = monthOfYear.remainder(n);
        assertEquals(expected, actual);
    }
}