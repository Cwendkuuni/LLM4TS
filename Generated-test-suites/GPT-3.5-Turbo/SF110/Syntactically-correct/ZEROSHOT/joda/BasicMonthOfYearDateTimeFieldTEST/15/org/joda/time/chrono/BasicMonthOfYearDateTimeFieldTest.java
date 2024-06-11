package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        int expected = 1;
        int result = field.get(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        int n2 = 1; // add 1 month
        long expected = 1580515200L; // February 1, 2020
        long result = field.add(n, n2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        long n2 = 1; // add 1 month
        long expected = 1580515200L; // February 1, 2020
        long result = field.add(n, n2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        int n2 = 12; // add 12 months, wrap around
        long expected = 1609459200L; // January 1, 2021
        long result = field.addWrapField(n, n2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        long set = 1609459200L; // January 1, 2021
        long expected = 12; // 12 months difference
        long result = field.getDifferenceAsLong(n, set);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        int n2 = 6; // set month to June
        long expected = 1590969600L; // June 1, 2020
        long result = field.set(n, n2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February is leap month
        
        long n = 1577836800L; // January 1, 2020
        boolean result = field.isLeap(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February is leap month
        
        long n = 1577836800L; // January 1, 2020
        int expected = 1;
        int result = field.getLeapAmount(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        int expected = 1;
        int result = field.getMinimumValue();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        int expected = 12;
        int result = field.getMaximumValue();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        long expected = 1577836800L; // January 1, 2020
        long result = field.roundFloor(n);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long n = 1577836800L; // January 1, 2020
        long expected = 0;
        long result = field.remainder(n);
        
        assertEquals(expected, result);
    }
}