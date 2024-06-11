import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.field.*;
import org.joda.time.*;

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
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        int expected = 2; // February
        int actual = field.get(dateTime);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdd1() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        int addValue = 1;
        long expected = 1615759200000L; // Sunday, March 14, 2021 12:00:00 AM
        long actual = field.add(dateTime, addValue);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAdd2() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long addValue = 1L;
        long expected = 1615759200000L; // Sunday, March 14, 2021 12:00:00 AM
        long actual = field.add(dateTime, addValue);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        int addValue = 15;
        long expected = 1625430000000L; // Tuesday, May 11, 2021 12:00:00 AM
        long actual = field.addWrapField(dateTime, addValue);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime1 = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long dateTime2 = 1618418400000L; // Tuesday, April 6, 2021 12:00:00 AM
        long expected = 6;
        long actual = field.getDifferenceAsLong(dateTime2, dateTime1);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        int setValue = 5;
        long expected = 1620223200000L; // Tuesday, May 5, 2021 12:00:00 AM
        long actual = field.set(dateTime, setValue);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        
        long dateTime1 = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long dateTime2 = 1618418400000L; // Tuesday, April 6, 2021 12:00:00 AM
        
        assertTrue(field.isLeap(dateTime1));
        assertFalse(field.isLeap(dateTime2));
    }
    
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        
        long dateTime1 = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long dateTime2 = 1618418400000L; // Tuesday, April 6, 2021 12:00:00 AM
        
        int expected1 = 1;
        int expected2 = 0;
        int actual1 = field.getLeapAmount(dateTime1);
        int actual2 = field.getLeapAmount(dateTime2);
        
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        int expected = 1;
        int actual = field.getMinimumValue();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        int expected = 12;
        int actual = field.getMaximumValue();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long expected = 1612134000000L; // Monday, February 1, 2021 12:00:00 AM
        long actual = field.roundFloor(dateTime);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        
        long dateTime = 1613071200000L; // Friday, February 12, 2021 12:00:00 AM
        long expected = 937200000L; // 10 days and 20 hours
        long actual = field.remainder(dateTime);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddReadablePartial() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.monthOfYear();
        ReadablePartial readablePartial = new ReadablePartial() {
            @Override
            public int size() {
                return 1;
            }

            @Override
            public DateTimeFieldType getFieldType(int index) {
                return dateTimeFieldType;
            }

            @Override
            public int getValue(int index) {
                return 2;
            }
        };
        
        int n = 0;
        int[] array = new int[1];
        int n2 = 1;
        int[] expected = {3};
        int[] actual = field.add(readablePartial, n, array, n2);
        
        assertArrayEquals(expected, actual);
    }
    
}