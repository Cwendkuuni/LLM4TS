package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        assertFalse(field.isLenient());
    }
    
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int expected = 7; // month of year at that timestamp
        
        assertEquals(expected, field.get(n));
    }
    
    
    @Test
    public void testAddIntZero() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int n2 = 0;
        long expected = n;
        
        assertEquals(expected, field.add(n, n2));
    }
    
    
    @Test
    public void testAddIntPositive() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int n2 = 3; // add 3 months
        long expected = 1573981679000L; // expected timestamp after adding 3 months
        
        assertEquals(expected, field.add(n, n2));
    }
    
    
    @Test
    public void testAddIntNegative() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int n2 = -2; // subtract 2 months
        long expected = 1555071279000L; // expected timestamp after subtracting 2 months
        
        assertEquals(expected, field.add(n, n2));
    }
    
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        long n2 = 2L; // cast to int results in 2
        long expected = 1573981679000L; // expected timestamp after adding 2 months
        
        assertEquals(expected, field.add(n, n2));
    }
    
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int n2 = 4; // add 4 months
        long expected = 1564465879000L; // expected timestamp after adding 4 months (wrap around)
        
        assertEquals(expected, field.addWrapField(n, n2));
    }
    
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        long set = 1564465879000L; // some other random timestamp
        long expected = 0L; // difference between the two timestamps
        
        assertEquals(expected, field.getDifferenceAsLong(n, set));
    }
    
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        int n2 = 6; // set to month 6
        long expected = 1564326479000L; // expected timestamp after setting to month 6
        
        assertEquals(expected, field.set(n, n2));
    }
    
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        int expected = 1; // minimum month of year
        
        assertEquals(expected, field.getMinimumValue());
    }
    
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        int expected = 12; // maximum month of year
        
        assertEquals(expected, field.getMaximumValue());
    }
    
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        long expected = 1564465879000L; // same timestamp
        
        assertEquals(expected, field.roundFloor(n));
    }
    
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        long n = 1564465879000L; // some random timestamp
        long expected = 0L; // since the timestamp is already at the start of the month
        
        assertEquals(expected, field.remainder(n));
    }
    
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        long n = 1564465879000L; // some random timestamp
        int expected = 1; // month is a leap month
        
        assertEquals(expected, field.getLeapAmount(n));
    }
}