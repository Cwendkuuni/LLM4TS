import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicChronology chronology = new BasicChronology();
    private BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
    
    @Test
    public void testGet() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        assertEquals(5, field.get(instant));
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testAdd() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        long result = field.add(instant, 2);
        assertEquals(7, field.get(result));
    }
    
    @Test
    public void testAddWithLeapYear() {
        long instant = chronology.getYearMonthMillis(2020, 12, 15);
        long result = field.add(instant, 2);
        assertEquals(2, field.get(result));
    }
    
    @Test
    public void testAddWithNegativeMonths() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        long result = field.add(instant, -5);
        assertEquals(12, field.get(result));
    }
    
    @Test
    public void testAddWrapField() {
        long instant = chronology.getYearMonthMillis(2022, 11, 15);
        long result = field.addWrapField(instant, 3);
        assertEquals(2, field.get(result));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = chronology.getYearMonthMillis(2022, 5, 15);
        long instant2 = chronology.getYearMonthMillis(2023, 6, 10);
        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(13, difference);
    }
    
    @Test
    public void testSet() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        long result = field.set(instant, 8);
        assertEquals(8, field.get(result));
    }
    
    @Test
    public void testSetWithInvalidMonth() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        try {
            field.set(instant, 13);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
    
    @Test
    public void testRoundFloor() {
        long instant = chronology.getYearMonthMillis(2022, 5, 15);
        long rounded = field.roundFloor(instant);
        assertEquals(chronology.getYearMonthMillis(2022, 5, 1), rounded);
    }
    
    @Test
    public void testGetLeapAmount() {
        long instant = chronology.getYearMonthMillis(2020, 1, 15);
        assertEquals(1, field.getLeapAmount(instant));
    }
    
    @Test
    public void testGetLeapAmountNotLeapYear() {
        long instant = chronology.getYearMonthMillis(2021, 1, 15);
        assertEquals(0, field.getLeapAmount(instant));
    }
}