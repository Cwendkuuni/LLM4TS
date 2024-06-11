package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.field.*;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.get(0));
        assertEquals(2, field.get(1));
        assertEquals(12, field.get(11));
    }
    
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.add(0, 0));
        assertEquals(1, field.add(0, 1));
        assertEquals(11, field.add(0, 10));
        assertEquals(9, field.add(1, -2));
        assertEquals(3, field.add(11, 4));
    }
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.add(0, 0L));
        assertEquals(1, field.add(0, 1L));
        assertEquals(11, field.add(0, 10L));
        assertEquals(9, field.add(1, -2L));
        assertEquals(3, field.add(11, 4L));
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.addWrapField(0, 0));
        assertEquals(1, field.addWrapField(0, 1));
        assertEquals(12, field.addWrapField(0, 11));
        assertEquals(11, field.addWrapField(1, -2));
        assertEquals(3, field.addWrapField(11, 4));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(0L, field.getDifferenceAsLong(0, 0));
        assertEquals(1L, field.getDifferenceAsLong(0, 1));
        assertEquals(10L, field.getDifferenceAsLong(0, 11));
        assertEquals(-2L, field.getDifferenceAsLong(1, -1));
        assertEquals(4L, field.getDifferenceAsLong(11, 15));
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(0, field.set(0, 0));
        assertEquals(1, field.set(0, 1));
        assertEquals(0, field.set(1, 0));
        assertEquals(5, field.set(1, 5));
        assertEquals(1, field.set(11, 1));
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertFalse(field.isLeap(0));
        assertFalse(field.isLeap(1));
        assertTrue(field.isLeap(2));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        assertEquals(0, field.getLeapAmount(0));
        assertEquals(0, field.getLeapAmount(1));
        assertEquals(1, field.getLeapAmount(2));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(0, field.roundFloor(0));
        assertEquals(0, field.roundFloor(1));
        assertEquals(0, field.roundFloor(5));
        assertEquals(6, field.roundFloor(6));
        assertEquals(11, field.roundFloor(11));
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        assertEquals(0, field.remainder(0));
        assertEquals(1, field.remainder(1));
        assertEquals(0, field.remainder(5));
        assertEquals(0, field.remainder(6));
        assertEquals(0, field.remainder(11));
    }
}