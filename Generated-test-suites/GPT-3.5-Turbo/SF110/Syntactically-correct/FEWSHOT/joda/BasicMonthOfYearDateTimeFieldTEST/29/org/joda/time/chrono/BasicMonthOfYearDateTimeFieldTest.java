package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testIsLenient() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        assertEquals(10, field.get(n));
    }
    
    @Test
    public void testAdd() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        int n2 = 2;
        long result = field.add(n, n2);
        assertEquals(1234569770, result);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        int n2 = 10;
        long result = field.addWrapField(n, n2);
        assertEquals(1234585090, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        long set = 987654321;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(3999, result);
    }
    
    @Test
    public void testSet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        int n2 = 5;
        long result = field.set(n, n2);
        assertEquals(1229648490, result);
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        assertFalse(field.isLeap(n));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        int result = field.getLeapAmount(n);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        long result = field.roundFloor(n);
        assertEquals(1229385600, result);
    }
    
    @Test
    public void testRemainder() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 6);
        long n = 1234567890;
        long result = field.remainder(n);
        assertEquals(18929890, result);
    }
}