package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(1, field.get(0));
        assertEquals(12, field.get(3700000000L));
    }
    
    @Test
    public void testAddWithInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(1, field.add(0, 0));
        assertEquals(3700000000L, field.add(0, 132));
        assertEquals(1, field.add(3700000000L, -131));
    }
    
    @Test
    public void testAddWithLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(1, field.add(0, 0L));
        assertEquals(3700000000L, field.add(0, 132L));
        assertEquals(1, field.add(3700000000L, -131L));
    }
    
    @Test
    public void testAddWithReadablePartial() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        int[] values = {1, 2, 3};
        assertArrayEquals(new int[]{1, 2, 3}, field.add(null, 0, values, 0));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(1, field.addWrapField(0, 0));
        assertEquals(3700000000L, field.addWrapField(0, 12));
        assertEquals(11, field.addWrapField(0, -2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(-120000000L, field.getDifferenceAsLong(120000000L, 240000000L));
        assertEquals(12, field.getDifferenceAsLong(120000000L, 240000012L));
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(120000000L, field.set(120000000L, 1));
        assertEquals(240000012L, field.set(240000000L, 12));
    }
    
    @Test
    public void testGetRangeDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertFalse(field.isLeap(0));
        assertTrue(field.isLeap(3600000000L));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(0, field.getLeapAmount(0));
        assertEquals(1, field.getLeapAmount(3600000000L));
    }
    
    @Test
    public void testGetLeapDurationField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertNotNull(field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(120000000L, field.roundFloor(123456789L));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertEquals(123456789L, field.remainder(246913578L));
    }

    @Test
    public void testReadResolve() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        assertNotNull(field.readResolve());
    }
}