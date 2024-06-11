import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    // Test for get method
    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(3, field.get(1609459200000L)); // January 2021
    }
    
    // Test for add method
    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(1617244800000L, field.add(1609459200000L, 2)); // March 2021
    }
    
    // Test for addWrapField method
    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(1609459200000L, field.addWrapField(1609459200000L, -1)); // December 2020
    }
    
    // Test for getDifferenceAsLong method
    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(6L, field.getDifferenceAsLong(1617244800000L, 1609459200000L)); // Difference between March 2021 and January 2021
    }
    
    // Test for set method
    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(1609459200000L, field.set(1617244800000L, 1)); // January 2021
    }
    
    // Test for isLeap method
    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertTrue(field.isLeap(1609459200000L)); // January 2021 is not a leap month
    }
    
    // Test for getLeapAmount method
    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 12);
        assertEquals(0, field.getLeapAmount(1609459200000L)); // January 2021 is not a leap month
    }
    
}