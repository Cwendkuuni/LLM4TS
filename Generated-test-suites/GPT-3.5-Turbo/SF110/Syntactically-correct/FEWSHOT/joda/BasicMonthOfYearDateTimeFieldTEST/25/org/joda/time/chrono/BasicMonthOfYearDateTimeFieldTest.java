package org.joda.time.chrono;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    BasicChronology basicChronology = new BasicChronology();
    BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, 1);
    long currentTime = System.currentTimeMillis();

    @Test
    public void testIsLenient() {
        assertFalse(basicMonthOfYearDateTimeField.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(1, basicMonthOfYearDateTimeField.get(currentTime));
    }

    @Test
    public void testAddInt() {
        long addedTime = basicMonthOfYearDateTimeField.add(currentTime, 1);
        assertEquals(basicMonthOfYearDateTimeField.get(currentTime) + 1, basicMonthOfYearDateTimeField.get(addedTime));
    }
    
    @Test
    public void testAddLong() {
        long addedTime = basicMonthOfYearDateTimeField.add(currentTime, 1L);
        assertEquals(basicMonthOfYearDateTimeField.get(currentTime) + 1, basicMonthOfYearDateTimeField.get(addedTime));
    }

    @Test
    public void testAddWrapField() {
        long wrappedTime = basicMonthOfYearDateTimeField.addWrapField(currentTime, 1);
        assertEquals(basicMonthOfYearDateTimeField.get(currentTime) + 1, basicMonthOfYearDateTimeField.get(wrappedTime));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long otherTime = currentTime - (3600 * 1000);
        long difference = basicMonthOfYearDateTimeField.getDifferenceAsLong(otherTime, currentTime);
        assertEquals(1, difference);
    }
    
    @Test
    public void testSet() {
        long newTime = basicMonthOfYearDateTimeField.set(currentTime, 6);
        assertEquals(6, basicMonthOfYearDateTimeField.get(newTime));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertNotNull(basicMonthOfYearDateTimeField.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        assertFalse(basicMonthOfYearDateTimeField.isLeap(currentTime));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(0, basicMonthOfYearDateTimeField.getLeapAmount(currentTime));
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, basicMonthOfYearDateTimeField.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(basicChronology.getMaxMonth(), basicMonthOfYearDateTimeField.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long roundedTime = basicMonthOfYearDateTimeField.roundFloor(currentTime);
        assertEquals(basicMonthOfYearDateTimeField.get(roundedTime), basicMonthOfYearDateTimeField.get(currentTime));
    }
    
    @Test
    public void testRemainder() {
        long roundedTime = basicMonthOfYearDateTimeField.roundFloor(currentTime);
        long remainder = basicMonthOfYearDateTimeField.remainder(currentTime);
        assertEquals(currentTime - roundedTime, remainder);
    }

    @Test
    public void testAddReadablePartial() {
        ReadablePartial readablePartial = DateTimeUtils.getCurrentReadablePartial();
        int[] array = {1, 2};
        int[] addedArray = basicMonthOfYearDateTimeField.add(readablePartial, 1, array, 0);
        assertEquals(basicMonthOfYearDateTimeField.get(currentTime) + 1, addedArray[0]);
        assertEquals(2, addedArray[1]);
    }
}