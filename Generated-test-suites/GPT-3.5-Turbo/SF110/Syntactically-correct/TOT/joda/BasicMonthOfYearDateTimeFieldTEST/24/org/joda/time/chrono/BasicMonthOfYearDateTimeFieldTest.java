import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    // Test for isLenient() method
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(dateTimeField.isLenient());
    }
    
    // Test for get() method
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        assertEquals(9, dateTimeField.get(n));
    }
    
    // Test for add() method with int parameter
    @Test
    public void testAddWithInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        int addition = 5;
        long result = dateTimeField.add(n, addition);
        long expected = 1577836800000L; // 15th February 2020
        assertEquals(expected, result);
    }
    
    // Test for add() method with long parameter
    @Test
    public void testAddWithLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        long addition = 15;
        long result = dateTimeField.add(n, addition);
        long expected = 1628035200000L; // 15th December 2020
        assertEquals(expected, result);
    }
    
    // Test for addWrapField() method
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        int addition = 10;
        long result = dateTimeField.addWrapField(n, addition);
        long expected = 1568304000000L; // 15th September 2019 (No change as field wraps around)
        assertEquals(expected, result);
    }
    
    // Test for getDifferenceAsLong() method
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n1 = 1568304000000L; // 15th September 2019
        long n2 = 1609459200000L; // 1st January 2021
        long result = dateTimeField.getDifferenceAsLong(n1, n2);
        long expected = 15;
        assertEquals(expected, result);
    }
    
    // Test for set() method
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        int newValue = 4;
        long result = dateTimeField.set(n, newValue);
        long expected = 1567488000000L; // 1st September 2019 (Changed to 1st September as day was out of range)
        assertEquals(expected, result);
    }
    
    // Test for isLeap() method
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1577836800000L; // 1st January 2020
        assertTrue(dateTimeField.isLeap(n));
    }
    
    // Test for getLeapAmount() method
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 2);
        long n = 1577836800000L; // 1st January 2020
        int result = dateTimeField.getLeapAmount(n);
        int expected = 1;
        assertEquals(expected, result);
    }
    
    // Test for getMinimumValue() method
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(1, dateTimeField.getMinimumValue());
    }
    
    // Test for getMaximumValue() method
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertEquals(12, dateTimeField.getMaximumValue());
    }
    
    // Test for roundFloor() method
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        long result = dateTimeField.roundFloor(n);
        long expected = 1567296000000L; // 1st September 2019
        assertEquals(expected, result);
    }
    
    // Test for remainder() method
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField dateTimeField = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 1568304000000L; // 15th September 2019
        long result = dateTimeField.remainder(n);
        long expected = 1008000000L; // Remainder is 1008000000 milliseconds
        assertEquals(expected, result);
    }
    
}