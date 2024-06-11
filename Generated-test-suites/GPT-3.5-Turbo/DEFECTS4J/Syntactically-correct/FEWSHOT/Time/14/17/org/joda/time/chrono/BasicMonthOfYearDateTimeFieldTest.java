package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicChronology chronology = new BasicChronology();
    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

    @Test
    public void testGet() {
        assertEquals(1, field.get(0));
        assertEquals(12, field.get(11));
        assertEquals(1, field.get(12));
        assertEquals(12, field.get(23));
    }

    @Test
    public void testAdd() {
        assertEquals(11, field.add(0, 10));
        assertEquals(1, field.add(23, 1));
        assertEquals(2, field.add(23, 2));
        assertEquals(12, field.add(23, 12));
    }

    @Test
    public void testAddWrapField() {
        assertEquals(12, field.addWrapField(0, 11));
        assertEquals(1, field.addWrapField(11, 1));
        assertEquals(2, field.addWrapField(11, 2));
        assertEquals(12, field.addWrapField(23, 12));
    }

    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(10, field.getDifferenceAsLong(10, 0));
        assertEquals(20, field.getDifferenceAsLong(0, 20));
        assertEquals(-10, field.getDifferenceAsLong(0, 10));
        assertEquals(0, field.getDifferenceAsLong(0, 0));
    }

    @Test
    public void testSet() {
        assertEquals(23, field.set(0, 12));
        assertEquals(22, field.set(23, 11));
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
        assertEquals(0, field.roundFloor(0));
        assertEquals(12, field.roundFloor(23));
    }

    @Test
    public void testRemainder() {
        assertEquals(0, field.remainder(0));
        assertEquals(11, field.remainder(23));
    }
}