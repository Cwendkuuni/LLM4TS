import org.joda.time.Months;
import org.joda.time.LocalDate;
import org.joda.time.DateTime;
import org.joda.time.chrono.BasicChronology;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        assertEquals(5, field.get(millis));
    }
    
    @Test
    public void testAddWithZeroAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.add(millis, 0);
        assertEquals(millis, result);
    }
    
    @Test
    public void testAddWithPositiveAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.add(millis, 2);
        assertEquals(chronology.getYearMonthDayMillis(2022, 7, 15), result);
    }
    
    @Test
    public void testAddWithNegativeAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.add(millis, -2);
        assertEquals(chronology.getYearMonthDayMillis(2022, 3, 15), result);
    }
    
    @Test
    public void testAddWithLongAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.add(millis, 2L);
        assertEquals(chronology.getYearMonthDayMillis(2022, 7, 15), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInvalidAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        field.add(millis, Long.MAX_VALUE);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.addWrapField(millis, 2);
        assertEquals(chronology.getYearMonthDayMillis(2022, 7, 15), result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis1 = chronology.getYearMonthDayMillis(2022, 5, 15);
        long millis2 = chronology.getYearMonthDayMillis(2022, 3, 10);
        long result = field.getDifferenceAsLong(millis1, millis2);
        assertEquals(2, result);
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.set(millis, 6);
        assertEquals(chronology.getYearMonthDayMillis(2022, 6, 15), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        field.set(millis, 13);
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        assertTrue(field.isLeap(millis));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 5);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        assertEquals(1, field.getLeapAmount(millis));
        
        millis = chronology.getYearMonthDayMillis(2022, 6, 15);
        assertEquals(0, field.getLeapAmount(millis));
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        assertEquals(1, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        assertEquals(chronology.getMaxMonth(), field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.roundFloor(millis);
        assertEquals(chronology.getYearMonthDayMillis(2022, 5, 1), result);
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long millis = chronology.getYearMonthDayMillis(2022, 5, 15);
        long result = field.remainder(millis);
        assertEquals(15 * 24 * 60 * 60 * 1000, result);
    }
}