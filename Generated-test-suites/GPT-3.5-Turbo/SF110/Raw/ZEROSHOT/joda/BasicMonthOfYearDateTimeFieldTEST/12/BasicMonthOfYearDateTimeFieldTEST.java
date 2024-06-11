package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
  
    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 6);
    }
  
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int actual = field.get(n);
        assertEquals(1, actual);
    }
    
    @Test
    public void testAddIntZero() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long actual = field.add(n, 0);
        assertEquals(n, actual);
    }
    
    @Test
    public void testAddIntPositive() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int n2 = 12;
        long expected = 2449440000L;  // 1970-12-19T00:00:00
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddIntNegative() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int n2 = -1;
        long expected = 31536000;  // 1970-12-19T00:00:00
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddLongPositive() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long n2 = 12;
        long expected = 2449440000L;  // 1970-12-19T00:00:00
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddLongNegative() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long n2 = -1;
        long expected = 31536000;  // 1970-12-19T00:00:00
        long actual = field.add(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddLongOutOfRange() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long n2 = 1000000000000L;
        field.add(n, n2);
    }
    
    @Test
    public void testAddWrapField() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int n2 = 12;
        long expected = 2449440000L;  // 1970-12-19T00:00:00
        long actual = field.addWrapField(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long set = 2449440000L;  // 1970-12-19T00:00:00
        long expected = 1;
        long actual = field.getDifferenceAsLong(n, set);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSet() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int n2 = 12;
        long expected = 2449440000L;  // 1970-12-19T00:00:00
        long actual = field.set(n, n2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRangeDurationField() {
        DurationField durationField = field.getRangeDurationField();
        assertNotNull(durationField);
        assertEquals("years", durationField.getName());
    }
    
    @Test
    public void testIsLeap() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        boolean actual = field.isLeap(n);
        assertFalse(actual);
    }
    
    @Test
    public void testGetLeapAmount() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        int actual = field.getLeapAmount(n);
        assertEquals(0, actual);
    }
    
    @Test
    public void testGetLeapDurationField() {
        DurationField durationField = field.getLeapDurationField();
        assertNotNull(durationField);
        assertEquals("days", durationField.getName());
    }
    
    @Test
    public void testGetMinimumValue() {
        int actual = field.getMinimumValue();
        assertEquals(1, actual);
    }
    
    @Test
    public void testGetMaximumValue() {
        int actual = field.getMaximumValue();
        assertEquals(12, actual);
    }
    
    @Test
    public void testRoundFloor() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long expected = 1512000000;  // 1970-01-01T00:00:00
        long actual = field.roundFloor(n);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemainder() {
        long n = 1576800000;  // 1970-01-19T00:00:00
        long expected = 64800000;  // 1970-01-19T00:00:00
        long actual = field.remainder(n);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadResolve() {
        Object obj = field.readResolve();
        assertNotNull(obj);
    }
}