import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField monthOfYear;
    
    public BasicMonthOfYearDateTimeFieldTest() {
        monthOfYear = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(monthOfYear.isLenient());
    }
    
    @Test
    public void testGet() {
        long instant = 1614595200000L;  // March 1, 2021
        int result = monthOfYear.get(instant);
        assertEquals(3, result);
    }
    
    @Test
    public void testAdd() {
        long instant = 1614595200000L;  // March 1, 2021
        long result = monthOfYear.add(instant, 2);
        assertEquals(1617206400000L, result);  // May 1, 2021
    }
    
    @Test
    public void testAddWrapField() {
        long instant = 1614595200000L;  // March 1, 2021
        long result = monthOfYear.addWrapField(instant, 11);
        assertEquals(1609372800000L, result);  // February 1, 2021
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 1614595200000L;  // March 1, 2021
        long subtrahendInstant = 1609372800000L;  // February 1, 2021
        long result = monthOfYear.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, result);
    }
    
    @Test
    public void testSet() {
        long instant = 1614595200000L;  // March 1, 2021
        long result = monthOfYear.set(instant, 2);
        assertEquals(1612089600000L, result);  // February 1, 2021
    }
    
    @Test
    public void testIsLeap() {
        long instant = 1640995200000L;  // January 1, 2022
        boolean result = monthOfYear.isLeap(instant);
        assertTrue(result);
    }
    
    @Test
    public void testGetLeapAmount() {
        long instant = 1640995200000L;  // January 1, 2022
        int result = monthOfYear.getLeapAmount(instant);
        assertEquals(1, result);
    }
    
    @Test
    public void testGetMinimumValue() {
        int result = monthOfYear.getMinimumValue();
        assertEquals(1, result);
    }
    
    @Test
    public void testGetMaximumValue() {
        int result = monthOfYear.getMaximumValue();
        assertEquals(12, result);
    }
    
    @Test
    public void testRoundFloor() {
        long instant = 1614595200000L;  // March 1, 2021
        long result = monthOfYear.roundFloor(instant);
        assertEquals(1614556800000L, result);  // March 1, 2021
    }
    
    @Test
    public void testRemainder() {
        long instant = 1614595200000L;  // March 1, 2021
        long result = monthOfYear.remainder(instant);
        assertEquals(38400000L, result);
    }
}