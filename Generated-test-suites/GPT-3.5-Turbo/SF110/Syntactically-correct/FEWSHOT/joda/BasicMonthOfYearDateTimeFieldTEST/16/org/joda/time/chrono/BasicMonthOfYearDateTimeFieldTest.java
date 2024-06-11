package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.DateTime;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        assertEquals(1, field.get(n));
    }
    
    @Test
    public void testAddInt() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0).getMillis(), field.add(n, 1));
    }
    
    @Test
    public void testAddLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0).getMillis(), field.add(n, 1L));
    }
    
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0).getMillis(), field.addWrapField(n, 1));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n1 = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        long n2 = new DateTime(2023, 2, 1, 0, 0, 0, 0).getMillis();
        assertEquals(13, field.getDifferenceAsLong(n2, n1));
    }
    
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 2, 1, 0, 0, 0, 0).getMillis(), field.set(n, 2));
    }
    
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long leapYear = new DateTime(2020, 2, 1, 0, 0, 0, 0).getMillis();
        long nonLeapYear = new DateTime(2021, 2, 1, 0, 0, 0, 0).getMillis();
        assertTrue(field.isLeap(leapYear));
        assertFalse(field.isLeap(nonLeapYear));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long leapYear = new DateTime(2020, 2, 1, 0, 0, 0, 0).getMillis();
        long nonLeapYear = new DateTime(2021, 2, 1, 0, 0, 0, 0).getMillis();
        assertEquals(1, field.getLeapAmount(leapYear));
        assertEquals(0, field.getLeapAmount(nonLeapYear));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 15, 0, 0, 0, 0).getMillis();
        assertEquals(new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis(), field.roundFloor(n));
    }
    
    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
        long n = new DateTime(2022, 1, 15, 0, 0, 0, 0).getMillis();
        assertEquals(14 * 24 * 60 * 60 * 1000, field.remainder(n));
    }
}