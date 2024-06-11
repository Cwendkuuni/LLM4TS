import static org.junit.Assert.*;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField basicMonthField = new BasicMonthOfYearDateTimeField(chronology, 12);
    
    @Test
    public void testIsLenient() {
        assertFalse(basicMonthField.isLenient());
    }
    
    @Test
    public void testGet() {
        long instant = chronology.getYearMonthDayMillis(2023, 5, 15);
        assertEquals(5, basicMonthField.get(instant));
    }
    
    @Test
    public void testAdd() {
        long instant = chronology.getYearMonthDayMillis(2023, 5, 15);
        long newInstant = basicMonthField.add(instant, 2);
        assertEquals(7, basicMonthField.get(newInstant));
    }
    
    @Test
    public void testAddWrapField() {
        long instant = chronology.getYearMonthDayMillis(2023, 12, 15); // December
        long newInstant = basicMonthField.addWrapField(instant, 2);
        assertEquals(2, basicMonthField.get(newInstant)); // February
    }
    
    @Test
    public void testSet() {
        long instant = chronology.getYearMonthDayMillis(2023, 5, 15);
        long newInstant = basicMonthField.set(instant, 9);
        assertEquals(9, basicMonthField.get(newInstant));
    }
    
    @Test
    public void testGetRangeDurationField() {
        assertNotNull(basicMonthField.getRangeDurationField());
    }
    
    @Test
    public void testIsLeap() {
        long leapInstant = chronology.getYearMonthDayMillis(2020, 2, 15); // Leap year, February
        assertTrue(basicMonthField.isLeap(leapInstant));
        
        long nonLeapInstant = chronology.getYearMonthDayMillis(2021, 2, 15); // Non-leap year, February
        assertFalse(basicMonthField.isLeap(nonLeapInstant));
    }
    
    @Test
    public void testGetLeapAmount() {
        long leapInstant = chronology.getYearMonthDayMillis(2020, 2, 15); // Leap year, February
        assertEquals(1, basicMonthField.getLeapAmount(leapInstant));
        
        long nonLeapInstant = chronology.getYearMonthDayMillis(2021, 2, 15); // Non-leap year, February
        assertEquals(0, basicMonthField.getLeapAmount(nonLeapInstant));
    }
    
    @Test
    public void testGetLeapDurationField() {
        assertNotNull(basicMonthField.getLeapDurationField());
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, basicMonthField.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(12, basicMonthField.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        long instant = chronology.getYearMonthDayMillis(2023, 5, 15);
        long floorInstant = basicMonthField.roundFloor(instant);
        assertEquals(2023, chronology.getYear(floorInstant));
        assertEquals(5, basicMonthField.get(floorInstant));
    }

    @Test
    public void testRemainder() {
        long instant = chronology.getYearMonthDayMillis(2023, 5, 15);
        assertEquals(0, basicMonthField.remainder(instant));
    }
}