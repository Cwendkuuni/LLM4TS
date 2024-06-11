package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2021, 1, 1);
        assertEquals(1, field.get(date.toDateTime(LocalTime.MIDNIGHT)));
        
        date = new LocalDate(2021, 12, 1);
        assertEquals(12, field.get(date.toDateTime(LocalTime.MIDNIGHT)));
    }
    
    @Test
    public void testAddInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);

        LocalDate date = new LocalDate(2020, 1, 1);
        assertEquals(new LocalDate(2020, 2, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.add(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1));
        
        date = new LocalDate(2020, 12, 1);
        assertEquals(new LocalDate(2021, 1, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.add(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1));
    }
    
    @Test
    public void testAddLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2020, 1, 1);
        assertEquals(new LocalDate(2020, 2, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.add(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1L));
        
        date = new LocalDate(2020, 12, 1);
        assertEquals(new LocalDate(2021, 1, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.add(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1L));
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2020, 1, 1);
        assertEquals(new LocalDate(2020, 2, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.addWrapField(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1));
        
        date = new LocalDate(2020, 12, 1);
        assertEquals(new LocalDate(2021, 1, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.addWrapField(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 1));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date1 = new LocalDate(2020, 1, 1);
        LocalDate date2 = new LocalDate(2021, 1, 1);
        
        assertEquals(12, field.getDifferenceAsLong(date2.toDateTime(LocalTime.MIDNIGHT).getMillis(),
                date1.toDateTime(LocalTime.MIDNIGHT).getMillis()));
        
        assertEquals(-12, field.getDifferenceAsLong(date1.toDateTime(LocalTime.MIDNIGHT).getMillis(),
                date2.toDateTime(LocalTime.MIDNIGHT).getMillis()));
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2020, 1, 1);
        assertEquals(new LocalDate(2020, 2, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.set(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 2));
        
        date = new LocalDate(2020, 12, 1);
        assertEquals(new LocalDate(2020, 12, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.set(date.toDateTime(LocalTime.MIDNIGHT).getMillis(), 13));
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        
        LocalDate date = new LocalDate(2020, 2, 1);
        assertTrue(field.isLeap(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
        
        date = new LocalDate(2020, 3, 1);
        assertFalse(field.isLeap(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        
        LocalDate date = new LocalDate(2020, 2, 1);
        assertEquals(1, field.getLeapAmount(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
        
        date = new LocalDate(2020, 3, 1);
        assertEquals(0, field.getLeapAmount(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(12, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2020, 1, 15);
        assertEquals(new LocalDate(2020, 1, 1).toDateTime(LocalTime.MIDNIGHT).getMillis(),
                field.roundFloor(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        
        LocalDate date = new LocalDate(2020, 1, 15);
        assertEquals(15, field.remainder(date.toDateTime(LocalTime.MIDNIGHT).getMillis()));
    }
    
}