package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.field.FieldUtils;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    private Chronology chronology;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(1, field.get(0));
        assertEquals(2, field.get(1000));
        assertEquals(3, field.get(2000));
    }
    
    @Test
    public void testAdd_int() {
        assertEquals(500, field.add(0, 500));
        assertEquals(1500, field.add(1000, 500));
        assertEquals(2500, field.add(2000, 500));
    }
    
    @Test
    public void testAdd_long() {
        assertEquals(500, field.add(0, 500L));
        assertEquals(1500, field.add(1000, 500L));
        assertEquals(2500, field.add(2000, 500L));
    }
    
    @Test
    public void testAddWrapField() {
        assertEquals(2, field.addWrapField(0, 1));
        assertEquals(3, field.addWrapField(1000, 1));
        assertEquals(1, field.addWrapField(2000, 11));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(500, field.getDifferenceAsLong(1000, 500));
        assertEquals(1000, field.getDifferenceAsLong(1500, 500));
        assertEquals(-500, field.getDifferenceAsLong(2000, 500));
    }
    
    @Test
    public void testSet() {
        assertEquals(500, field.set(0, 5));
        assertEquals(1500, field.set(1000, 5));
        assertEquals(2500, field.set(2000, 5));
    }
    
    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(0));
        assertFalse(field.isLeap(1000));
        assertFalse(field.isLeap(2000));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(0));
        assertEquals(0, field.getLeapAmount(1000));
        assertEquals(0, field.getLeapAmount(2000));
    }
    
    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        assertNotNull(durationField);
    }
    
    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        assertNotNull(durationField);
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        assertEquals(0, field.roundFloor(100));
        assertEquals(1000, field.roundFloor(1050));
        assertEquals(2000, field.roundFloor(2100));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(100, field.remainder(100));
        assertEquals(50, field.remainder(1050));
        assertEquals(100, field.remainder(2100));
    }
    
    @Test
    public void testReadResolve() {
        DateTimeField resolved = field.readResolve();
        assertNotNull(resolved);
        assertTrue(resolved instanceof DateTimeFieldType);
        assertEquals(DateTimeFieldType.monthOfYear(), resolved);
    }
}