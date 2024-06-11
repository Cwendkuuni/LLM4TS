package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    
    public BasicMonthOfYearDateTimeFieldTest() {
        BasicChronology iChronology = new BasicChronology();
        int iLeapMonth = 12;
        field = new BasicMonthOfYearDateTimeField(iChronology, iLeapMonth);
    }
    
    @Test
    public void testIsLenient() {
        boolean result = field.isLenient();
        assertEquals(false, result);
    }
    
    @Test
    public void testGet() {
        long n = 1000;
        int result = field.get(n);
        assertEquals(1, result);
    }
    
    @Test
    public void testAddTwoInts() {
        long n = 1000;
        int n2 = 2;
        long result = field.add(n, n2);
        assertEquals(86700, result);
    }
    
    @Test
    public void testAddTwoLongs() {
        long n = 1000;
        long n2 = 2;
        long result = field.add(n, n2);
        assertEquals(86700, result);
    }
    
    @Test
    public void testAddReadablePartial() {
        ReadablePartial readablePartial = new ReadablePartial();
        int n = 0;
        int[] array = {1, 2, 3};
        int n2 = 1;
        int[] result = field.add(readablePartial, n, array, n2);
        int[] expected = {1, 2, 3};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAddWrapField() {
        long n = 1000;
        int n2 = 2;
        long result = field.addWrapField(n, n2);
        assertEquals(28800, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long n = 1000;
        long set = 800;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(123, result);
    }
    
    @Test
    public void testSet() {
        long n = 1000;
        int n2 = 2;
        long result = field.set(n, n2);
        assertEquals(609168, result);
    }
    
    @Test
    public void testGetRangeDurationField() {
        DurationField result = field.getRangeDurationField();
        DurationField expected = new BasicChronology().years();
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsLeap() {
        long n = 1000;
        boolean result = field.isLeap(n);
        assertEquals(false, result);
    }
    
    @Test
    public void testGetLeapAmount() {
        long n = 1000;
        int result = field.getLeapAmount(n);
        assertEquals(0, result);
    }
    
    @Test
    public void testGetLeapDurationField() {
        DurationField result = field.getLeapDurationField();
        DurationField expected = new BasicChronology().days();
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMinimumValue() {
        int result = field.getMinimumValue();
        assertEquals(1, result);
    }
    
    @Test
    public void testGetMaximumValue() {
        int result = field.getMaximumValue();
        assertEquals(12, result);
    }
    
    @Test
    public void testRoundFloor() {
        long n = 1000;
        long result = field.roundFloor(n);
        assertEquals(86400000, result);
    }
    
    @Test
    public void testRemainder() {
        long n = 1000;
        long result = field.remainder(n);
        assertEquals(1000, result);
    }
    
    @Test
    public void testReadResolve() {
        Object result = field.readResolve();
        assertSame(this.field.iChronology.monthOfYear(), result);
    }
}