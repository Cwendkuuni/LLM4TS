package org.joda.time.chrono;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;

import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
            
    @Test
    public void testIsLenient() {
        assertEquals(false, field.isLenient());
    }
    
    @Test
    public void testGet() {
        LocalDateTime dateTime = new LocalDateTime();
        assertEquals(dateTime.getMonthOfYear(), field.get(dateTime.toDateTime().getMillis()));
    }
    
    @Test
    public void testAddWithInt() {
        LocalDateTime dateTime = new LocalDateTime();
        long millis = dateTime.toDateTime().getMillis();
        int fieldVal = field.get(millis);
        
        assertEquals(millis, field.add(millis, 0));
        assertEquals(millis + 1, field.add(millis, 1));
        assertEquals(millis - 1, field.add(millis, -1));
        assertEquals(millis + field.getMaxValue(), field.add(millis, field.getMaxValue()));
        assertEquals(millis - field.getMaxValue(), field.add(millis, -field.getMaxValue()));
        assertEquals(millis + field.getMaxValue() + 1, field.add(millis, field.getMaxValue() + 1));
        assertEquals(millis - field.getMaxValue() - 1, field.add(millis, -field.getMaxValue() - 1));
    }
    
    @Test
    public void testAddWithLong() {
        LocalDateTime dateTime = new LocalDateTime();
        long millis = dateTime.toDateTime().getMillis();
        int fieldVal = field.get(millis);
        
        assertEquals(millis, field.add(millis, 0L));
        assertEquals(millis + 1, field.add(millis, 1L));
        assertEquals(millis - 1, field.add(millis, -1L));
        assertEquals(millis + field.getMaxValue(), field.add(millis, (long)field.getMaxValue()));
        assertEquals(millis - field.getMaxValue(), field.add(millis, (long)-field.getMaxValue()));
        assertEquals(millis + field.getMaxValue() + 1, field.add(millis, (long)field.getMaxValue() + 1));
        assertEquals(millis - field.getMaxValue() - 1, field.add(millis, (long)-field.getMaxValue() - 1));
    }
    
    @Test
    public void testAddWrapField() {
        LocalDateTime dateTime = new LocalDateTime();
        long millis = dateTime.toDateTime().getMillis();
        int fieldVal = field.get(millis);
        
        assertEquals(millis + field.getMaxValue() + 1, field.addWrapField(millis, 1));
        assertEquals(millis - field.getMaxValue() - 1, field.addWrapField(millis, -1));
        assertEquals(millis, field.addWrapField(millis, 0));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        LocalDateTime dateTime2 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        
        long diff = field.getDifferenceAsLong(dateTime1.toDateTime().getMillis(),
                                              dateTime2.toDateTime().getMillis());
        assertEquals(0L, diff);
        
        LocalDateTime dateTime3 = new LocalDateTime(2021, 1, 1, 0, 0, 0);
        LocalDateTime dateTime4 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        
        diff = field.getDifferenceAsLong(dateTime3.toDateTime().getMillis(),
                                              dateTime4.toDateTime().getMillis());
        assertEquals(12L, diff);
        
        LocalDateTime dateTime5 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        LocalDateTime dateTime6 = new LocalDateTime(2023, 1, 1, 0, 0, 0);
        
        diff = field.getDifferenceAsLong(dateTime5.toDateTime().getMillis(),
                                              dateTime6.toDateTime().getMillis());
        assertEquals(12L, diff);
        
        LocalDateTime dateTime7 = new LocalDateTime(2023, 1, 1, 0, 0, 0);
        LocalDateTime dateTime8 = new LocalDateTime(2023, 1, 1, 0, 0, 0);
        
        diff = field.getDifferenceAsLong(dateTime7.toDateTime().getMillis(),
                                              dateTime8.toDateTime().getMillis());
        assertEquals(0L, diff);
    }
    
    @Test
    public void testSet() {
        LocalDateTime dateTime = new LocalDateTime();
        long millis = dateTime.toDateTime().getMillis();
        
        assertEquals(millis, field.set(millis, 1));
        assertEquals(millis, field.set(millis, field.getMinimumValue()));
        assertEquals(millis, field.set(millis, field.getMaximumValue()));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertNotNull(field.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        assertTrue(field.isLeap(dateTime1.toDateTime().getMillis()));
        
        LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
        assertFalse(field.isLeap(dateTime2.toDateTime().getMillis()));
    }
    
    @Test
    public void testGetLeapAmount() {
        LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
        assertEquals(1, field.getLeapAmount(dateTime1.toDateTime().getMillis()));
        
        LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
        assertEquals(0, field.getLeapAmount(dateTime2.toDateTime().getMillis()));
    }
    
    @Test
    public void testGetLeapDurationField() {
        assertNotNull(field.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(field.getMaxValue(), field.getMaximumValue());
    }
    
    @Test
    public void testRemainder() {
        LocalDateTime dateTime = new LocalDateTime();
        long millis = dateTime.toDateTime().getMillis();
        long rounded = field.roundFloor(millis);
        
        assertEquals(millis - rounded, field.remainder(millis));
    }
    
    @Test
    public void testReadResolve() {
        assertEquals(field, field.readResolve());
    }
}