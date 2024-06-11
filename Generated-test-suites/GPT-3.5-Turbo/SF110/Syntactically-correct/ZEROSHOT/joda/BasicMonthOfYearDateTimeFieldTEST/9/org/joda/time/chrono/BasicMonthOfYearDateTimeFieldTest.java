package org.joda.time.chrono;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    
    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 3);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        int monthOfYear = field.get(n);
        assertEquals(1, monthOfYear);
    }
    
    @Test
    public void testAddWithZero() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.add(n, 0);
        assertEquals(n, result);
    }
    
    @Test
    public void testAddWithPositiveValue() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        int monthOfYear = field.get(n);
        
        long result = field.add(n, 2);
        assertEquals(monthOfYear + 2, field.get(result));
    }
    
    @Test
    public void testAddWithNegativeValue() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        int monthOfYear = field.get(n);
        
        long result = field.add(n, -3);
        assertEquals(monthOfYear - 3, field.get(result));
    }
    
    @Test
    public void testAddWrapField() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.addWrapField(n, 5);
        assertEquals(6, field.get(result));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddWrapFieldWithLargeValue() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        field.addWrapField(n, 10);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long n1 = 1577836800L; // 2020-01-01T00:00:00Z
        long n2 = 1609459200L; // 2021-01-01T00:00:00Z
        
        long difference = field.getDifferenceAsLong(n2, n1);
        assertEquals(12, difference);
    }
    
    @Test
    public void testSet() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.set(n, 6);
        assertEquals(6, field.get(result));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidValue() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        field.set(n, 13);
    }
    
    @Test
    public void testIsLeap() {
        long leapYear = 1583020800L; // 2020-02-01T00:00:00Z
        long nonLeapYear = 1577836800L; // 2020-01-01T00:00:00Z
        
        assertTrue(field.isLeap(leapYear));
        assertFalse(field.isLeap(nonLeapYear));
    }
    
    @Test
    public void testGetLeapAmount() {
        long leapYear = 1583020800L; // 2020-02-01T00:00:00Z
        long nonLeapYear = 1577836800L; // 2020-01-01T00:00:00Z
        
        assertEquals(1, field.getLeapAmount(leapYear));
        assertEquals(0, field.getLeapAmount(nonLeapYear));
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
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.roundFloor(n);
        assertEquals(n, result);
    }
    
    @Test
    public void testRemainder() {
        long n = 1577836800L; // 2020-01-01T00:00:00Z
        long result = field.remainder(n);
        assertEquals(0, result);
    }
    
}