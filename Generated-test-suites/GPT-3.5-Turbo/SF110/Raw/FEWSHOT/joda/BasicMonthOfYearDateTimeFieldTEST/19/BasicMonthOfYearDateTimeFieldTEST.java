import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    // Test case for isLenient method
    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        assertFalse(field.isLenient());
    }
    
    // Tests for get method
    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        int expected = 3;
        int result = field.get(n);
        assertEquals(expected, result);
    }
    
    // Tests for add method with int parameter
    @Test
    public void testAddWithInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        int value = 2;
        long expected = 123456789;
        long result = field.add(n, value);
        assertEquals(expected, result);
    }
    
    // Tests for add method with long parameter
    @Test
    public void testAddWithLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        long value = 2L;
        long expected = 123456789L;
        long result = field.add(n, value);
        assertEquals(expected, result);
    }
    
    // Tests for addWrapField method
    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        int value = 2;
        long expected = 123456789;
        long result = field.addWrapField(n, value);
        assertEquals(expected, result);
    }
    
    // Tests for getDifferenceAsLong method
    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        long set = 987654321;
        long expected = -876543532;
        long result = field.getDifferenceAsLong(n, set);
        assertEquals(expected, result);
    }
    
    // Test case for set method
    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        int value = 2;
        long expected = 123456789;
        long result = field.set(n, value);
        assertEquals(expected, result);
    }
    
    // Test case for getRangeDurationField method
    @Test
    public void testGetRangeDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DurationField expected = chronology.years();
        DurationField result = field.getRangeDurationField();
        assertEquals(expected, result);
    }
    
    // Test case for isLeap method
    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        assertTrue(field.isLeap(n));
    }
    
    // Test case for getLeapAmount method
    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        int expected = 1;
        int result = field.getLeapAmount(n);
        assertEquals(expected, result);
    }
    
    // Test case for getLeapDurationField method
    @Test
    public void testGetLeapDurationField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        DurationField expected = chronology.days();
        DurationField result = field.getLeapDurationField();
        assertEquals(expected, result);
    }
    
    // Test case for getMinimumValue method
    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = 1;
        int result = field.getMinimumValue();
        assertEquals(expected, result);
    }
    
    // Test case for getMaximumValue method
    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        int expected = 12;
        int result = field.getMaximumValue();
        assertEquals(expected, result);
    }
    
    // Test case for roundFloor method
    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        long expected = 123456789;
        long result = field.roundFloor(n);
        assertEquals(expected, result);
    }
    
    // Test case for remainder method
    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        long n = 123456789;
        long expected = 0;
        long result = field.remainder(n);
        assertEquals(expected, result);
    }
    
    // Test case for readResolve method
    @Test
    public void testReadResolve() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
        Object expected = chronology.monthOfYear();
        Object result = field.readResolve();
        assertEquals(expected, result);
    }

}