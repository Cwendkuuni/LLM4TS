package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(2, field.get(n));
    }
    
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(1584296400000L, field.add(n, 1)); // March 13, 2020 00:00:00
    }
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(1584296400000L, field.add(n, 1L)); // March 13, 2020 00:00:00
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(1581963600000L, field.addWrapField(n, -12)); // February 13, 2019 00:00:00
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n1 = 1584200000000L; // February 13, 2020 00:00:00
        long n2 = 1577833200000L; // January 1, 2020 00:00:00
        assertEquals(1, field.getDifferenceAsLong(n1, n2));
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(1581765600000L, field.set(n, 1)); // January 17, 2020 00:00:00
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n1 = 1584200000000L; // February 13, 2020 00:00:00
        long n2 = 1577833200000L; // January 1, 2020 00:00:00
        assertTrue(field.isLeap(n1));
        assertFalse(field.isLeap(n2));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n1 = 1584200000000L; // February 13, 2020 00:00:00
        long n2 = 1577833200000L; // January 1, 2020 00:00:00
        assertEquals(1, field.getLeapAmount(n1));
        assertEquals(0, field.getLeapAmount(n2));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(1580539200000L, field.roundFloor(n)); // February 1, 2020 00:00:00
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1584200000000L; // February 13, 2020 00:00:00
        assertEquals(432000000L, field.remainder(n)); // 12 hours
    }
    
    @Test
    public void testAdd() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        ReadablePartial partial = new DateMidnight(2020, 2, 13);
        int[] values = {2};
        int[] result = field.add(partial, 1, values, 0);
        int[] expected = {3};
        assertArrayEquals(expected, result);
    }

}