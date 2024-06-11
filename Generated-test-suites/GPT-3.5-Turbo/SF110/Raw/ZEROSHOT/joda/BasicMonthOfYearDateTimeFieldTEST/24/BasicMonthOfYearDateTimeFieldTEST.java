package org.joda.time.chrono;

import static org.junit.Assert.*;

import org.joda.time.DateTimeFieldType;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1, field.get(1000));
    }
    
    @Test
    public void testAddIntZero() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1000, field.add(1000, 0));
    }
    
    @Test
    public void testAddInt() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1002, field.add(1000, 2));
    }
    
    @Test
    public void testAddLongZero() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1000, field.add(1000, 0L));
    }
    
    @Test
    public void testAddLong() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1002, field.add(1000, 2L));
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1002, field.addWrapField(1000, 2));
    }
    
    @Test
    public void testGetDifferenceAsLongPositive() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(12, field.getDifferenceAsLong(1000, 988));
    }
    
    @Test
    public void testGetDifferenceAsLongNegative() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(-12, field.getDifferenceAsLong(988, 1000));
    }
    
    @Test
    public void testSet() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1002, field.set(1000, 2));
    }
    
    @Test
    public void testGetLeapAmountLeap() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        assertEquals(1, field.getLeapAmount(1000));
    }
    
    @Test
    public void testGetLeapAmountNoLeap() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 2);
        assertEquals(0, field.getLeapAmount(1001));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1000, field.roundFloor(1001));
    }
    
    @Test
    public void testRemainder() {
        BasicChronology iChronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(iChronology, 1);
        assertEquals(1, field.remainder(1001));
    }
    
    // add more test methods for all the other methods in the class...
}