package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        assertEquals(1, field.get(n));
    }
    
    @Test
    public void testAdd() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        long result = field.add(n, 1);
        long expected = basicChronology.getYearMonthDayMillis(1990, 2, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 12, 1);
        long result = field.addWrapField(n, 1);
        long expected = basicChronology.getYearMonthDayMillis(1991, 1, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        long set = basicChronology.getYearMonthDayMillis(1980, 1, 1);
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(10, result);
    }
    
    @Test
    public void testSet() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        long result = field.set(n, 2);
        long expected = basicChronology.getYearMonthDayMillis(1990, 2, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetRangeDurationField() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        DurationField rangeDurationField = field.getRangeDurationField();
        assertNotNull(rangeDurationField);
        assertTrue(rangeDurationField instanceof DurationField);
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        assertFalse(field.isLeap(n));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        int result = field.getLeapAmount(n);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetLeapDurationField() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        DurationField leapDurationField = field.getLeapDurationField();
        assertNotNull(leapDurationField);
        assertTrue(leapDurationField instanceof DurationField);
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        assertEquals(31, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 15);
        long result = field.roundFloor(n);
        long expected = basicChronology.getYearMonthDayMillis(1990, 1, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemainder() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        long n = basicChronology.getYearMonthDayMillis(1990, 1, 15);
        long result = field.remainder(n);
        long expected = 15 * 24 * 60 * 60 * 1000;
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadResolve() {
        BasicChronology basicChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(basicChronology, 1);
        assertNotNull(field.readResolve());
    }
    
}