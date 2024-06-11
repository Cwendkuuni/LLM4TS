import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField monthField;
    private BasicChronology chronology;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        monthField = new BasicMonthOfYearDateTimeField(chronology, 5);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(monthField.isLenient());
    }
    
    @Test
    public void testGet() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 1, 1);
        assertEquals(1, monthField.get(dateTime));
        
        dateTime = chronology.getYearMonthDayMillis(2020, 5, 1);
        assertEquals(5, monthField.get(dateTime));
        
        dateTime = chronology.getYearMonthDayMillis(2021, 12, 1);
        assertEquals(12, monthField.get(dateTime));
    }
    
    @Test
    public void testAddWithZeroAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.add(dateTime, 0);
        
        assertEquals(dateTime, result);
    }
    
    @Test
    public void testAddWithPositiveAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.add(dateTime, 2);
        
        long expected = chronology.getYearMonthDayMillis(2019, 8, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithNegativeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.add(dateTime, -2);
        
        long expected = chronology.getYearMonthDayMillis(2019, 4, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithLargeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.add(dateTime, 500);
        
        long expected = chronology.getYearMonthDayMillis(2060, 2, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithLargeNegativeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.add(dateTime, -500);
        
        long expected = chronology.getYearMonthDayMillis(1962, 10, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWithInvalidAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        try {
            monthField.add(dateTime, 13);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: 13", e.getMessage());
        }
    }
    
    @Test
    public void testAddWithLargeInvalidAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        try {
            monthField.add(dateTime, 10000);
            fail("IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Magnitude of add amount is too large: 10000", e.getMessage());
        }
    }
    
    @Test
    public void testAddWrapFieldWithPositiveAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.addWrapField(dateTime, 2);
        
        long expected = chronology.getYearMonthDayMillis(2019, 8, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapFieldWithNegativeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.addWrapField(dateTime, -2);
        
        long expected = chronology.getYearMonthDayMillis(2019, 4, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapFieldWithLargeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.addWrapField(dateTime, 1000);
        
        long expected = chronology.getYearMonthDayMillis(4096, 5, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapFieldWithLargeNegativeAmount() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.addWrapField(dateTime, -1000);
        
        long expected = chronology.getYearMonthDayMillis(-503, 7, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long dateTime1 = chronology.getYearMonthDayMillis(2019, 6, 15);
        long dateTime2 = chronology.getYearMonthDayMillis(2020, 5, 1);
        
        long result = monthField.getDifferenceAsLong(dateTime1, dateTime2);
        
        assertEquals(11, result);
    }
    
    @Test
    public void testGetDifferenceAsLongWithNegativeDifference() {
        long dateTime1 = chronology.getYearMonthDayMillis(2020, 5, 1);
        long dateTime2 = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.getDifferenceAsLong(dateTime1, dateTime2);
        
        assertEquals(-11, result);
    }
    
    @Test
    public void testSet() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.set(dateTime, 10);
        
        long expected = chronology.getYearMonthDayMillis(2019, 10, 15);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsLeap() {
        long leapYearDateTime = chronology.getYearMonthDayMillis(2020, 5, 1);
        long nonLeapYearDateTime = chronology.getYearMonthDayMillis(2021, 5, 1);
        
        assertTrue(monthField.isLeap(leapYearDateTime));
        assertFalse(monthField.isLeap(nonLeapYearDateTime));
    }
    
    @Test
    public void testGetLeapAmount() {
        long leapYearDateTime = chronology.getYearMonthDayMillis(2020, 5, 1);
        long nonLeapYearDateTime = chronology.getYearMonthDayMillis(2021, 5, 1);
        
        assertEquals(1, monthField.getLeapAmount(leapYearDateTime));
        assertEquals(0, monthField.getLeapAmount(nonLeapYearDateTime));
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, monthField.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(chronology.getMaxMonth(), monthField.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.roundFloor(dateTime);
        
        long expected = chronology.getYearMonthDayMillis(2019, 6, 1);
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemainder() {
        long dateTime = chronology.getYearMonthDayMillis(2019, 6, 15);
        
        long result = monthField.remainder(dateTime);
        
        long expected = chronology.getYearMonthDayMillis(2019, 6, 15);
        assertEquals(expected, result);
    }
    
}