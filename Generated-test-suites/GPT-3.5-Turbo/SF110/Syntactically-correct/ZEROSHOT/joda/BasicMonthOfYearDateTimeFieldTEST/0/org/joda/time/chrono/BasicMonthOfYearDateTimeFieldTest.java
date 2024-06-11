package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        assertEquals(1, field.get(n));
    }

    @Test
    public void testAddIntZero() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int n2 = 0;
        assertEquals(n, field.add(n, n2));
    }

    @Test
    public void testAddIntPositive() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int n2 = 2;
        long expected = n + chronology.getAverageMillisPerMonth() * n2;
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddIntNegative() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int n2 = -2;
        long expected = n - chronology.getAverageMillisPerMonth() * Math.abs(n2);
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        long n2 = 2L;
        long expected = n + chronology.getAverageMillisPerMonth() * (int) n2;
        assertEquals(expected, field.add(n, n2));
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int n2 = 2;
        long expected = field.set(n, field.get(n) + n2);
        assertEquals(expected, field.addWrapField(n, n2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n1 = 1000;
        long n2 = 500;
        long expected = chronology.getMonthsBetween(n1, n2);
        assertEquals(expected, field.getDifferenceAsLong(n1, n2));
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int n2 = 6;
        long expected = chronology.getYearMonthDayMillis(chronology.getYear(n), n2, chronology.getDayOfMonth(n, chronology.getYear(n), n2));
        assertEquals(expected, field.set(n, n2));
    }

    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(chronology.years(), field.getRangeDurationField());
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        assertFalse(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        assertEquals(0, field.getLeapAmount(n));
    }

    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(chronology.days(), field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(field.iMax, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int year = chronology.getYear(n);
        int monthOfYear = chronology.getMonthOfYear(n, year);
        long expected = chronology.getYearMonthMillis(year, monthOfYear);
        assertEquals(expected, field.roundFloor(n));
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1000;
        int year = chronology.getYear(n);
        int monthOfYear = chronology.getMonthOfYear(n, year);
        long remainder = n - chronology.getYearMonthMillis(year, monthOfYear);
        assertEquals(remainder, field.remainder(n));
    }
}