package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp = 1234567890L;
        assertEquals(6, field.get(timestamp));
    }

    @Test
    public void testAddInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp = 1234567890L;
        assertEquals(1234987890L, field.add(timestamp, 4200));
        assertEquals(1234867890L, field.add(timestamp, -4200));
        assertEquals(1234567890L, field.add(timestamp, 0));
    }

    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp = 1234567890L;
        assertEquals(1234987890L, field.add(timestamp, 4200L));
        assertEquals(1234867890L, field.add(timestamp, -4200L));
        assertEquals(1234567890L, field.add(timestamp, 0L));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp = 1234567890L;
        assertEquals(1234867890L, field.addWrapField(timestamp, 4200));
        assertEquals(1234987890L, field.addWrapField(timestamp, -4200));
        assertEquals(1234567890L, field.addWrapField(timestamp, 0));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp1 = 1234567890L;
        long timestamp2 = 1234987890L;
        assertEquals(96, field.getDifferenceAsLong(timestamp1, timestamp2));
        assertEquals(-96, field.getDifferenceAsLong(timestamp2, timestamp1));
        assertEquals(0, field.getDifferenceAsLong(timestamp1, timestamp1));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        long timestamp = 1234567890L;
        assertEquals(1220714890L, field.set(timestamp, 2));
        assertEquals(1261989690L, field.set(timestamp, 12));
        assertEquals(1234567890L, field.set(timestamp, 6));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        long timestamp1 = 1234567890L;
        long timestamp2 = 1230817890L;
        assertTrue(field.isLeap(timestamp1));
        assertFalse(field.isLeap(timestamp2));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        long timestamp1 = 1234567890L;
        long timestamp2 = 1230817890L;
        assertEquals(1, field.getLeapAmount(timestamp1));
        assertEquals(0, field.getLeapAmount(timestamp2));
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 0);
        assertEquals(12, field.getMaximumValue());
    }
}