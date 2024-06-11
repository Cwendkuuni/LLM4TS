import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField monthField;
    
    @Before
    public void setUp() {
        monthField = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
    }
    
    @Test
    public void testGet() {
        long instant = 1626324600000L; // June 2021
        int month = monthField.get(instant);
        assertEquals(6, month);
    }
    
    @Test
    public void testAdd() {
        long instant = 1626324600000L; // June 2021
        long newInstant = monthField.add(instant, 2);
        assertEquals(1630520400000L, newInstant); // August 2021
    }
    
    @Test
    public void testAddWrapField() {
        long instant = 1626324600000L; // June 2021
        long newInstant = monthField.addWrapField(instant, 7);
        assertEquals(1640005200000L, newInstant); // January 2022
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = 1626324600000L; // June 2021
        long instant2 = 1630520400000L; // August 2021
        long difference = monthField.getDifferenceAsLong(instant1, instant2);
        assertEquals(2, difference); // 2 months
    }
    
    @Test
    public void testSet() {
        long instant = 1626324600000L; // June 2021
        long newInstant = monthField.set(instant, 12);
        assertEquals(1641090000000L, newInstant); // December 2021
    }
    
    @Test
    public void testIsLeap() {
        long instant = 1626324600000L; // June 2021
        assertFalse(monthField.isLeap(instant));
    }
    
    @Test
    public void testGetLeapAmount() {
        long instant = 1626324600000L; // June 2021
        assertEquals(0, monthField.getLeapAmount(instant));
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, monthField.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, monthField.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long instant = 1626324600000L; // June 2021
        long roundedInstant = monthField.roundFloor(instant);
        assertEquals(1625094000000L, roundedInstant); // June 1st, 2021
    }
    
    @Test
    public void testRemainder() {
        long instant = 1626324600000L; // June 2021
        long remainder = monthField.remainder(instant);
        assertEquals(123060000L, remainder); // 1 hour and 30 minutes
    }
}