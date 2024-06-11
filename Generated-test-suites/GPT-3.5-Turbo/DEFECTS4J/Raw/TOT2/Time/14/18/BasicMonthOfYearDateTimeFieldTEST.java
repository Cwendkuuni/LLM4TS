import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField monthField;
    
    @Before
    public void setUp() {
        monthField = new BasicMonthOfYearDateTimeField(new BasicChronology(), 5);
    }
    
    @Test
    public void testGet() {
        long instant = new DateTime(2021, 6, 15, 0, 0).getMillis();
        assertEquals(6, monthField.get(instant));
    }
    
    @Test
    public void testAdd() {
        long instant = new DateTime(2021, 6, 15, 0, 0).getMillis();
        long result = monthField.add(instant, 2);
        assertEquals(new DateTime(2021, 8, 15, 0, 0).getMillis(), result);
    }
    
    @Test
    public void testAddWrapField() {
        long instant = new DateTime(2021, 12, 31, 0, 0).getMillis();
        long result = monthField.addWrapField(instant, 2);
        assertEquals(new DateTime(2022, 2, 28, 0, 0).getMillis(), result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long instant1 = new DateTime(2021, 6, 15, 0, 0).getMillis();
        long instant2 = new DateTime(2022, 6, 15, 0, 0).getMillis();
        long result = monthField.getDifferenceAsLong(instant1, instant2);
        assertEquals(12, result);
    }
    
    @Test
    public void testSet() {
        long instant = new DateTime(2021, 6, 15, 0, 0).getMillis();
        long result = monthField.set(instant, 10);
        assertEquals(new DateTime(2021, 10, 15, 0, 0).getMillis(), result);
    }
    
    @Test
    public void testIsLeap() {
        long leapInstant = new DateTime(2020, 2, 29, 0, 0).getMillis();
        assertTrue(monthField.isLeap(leapInstant));
        
        long nonLeapInstant = new DateTime(2021, 2, 28, 0, 0).getMillis();
        assertFalse(monthField.isLeap(nonLeapInstant));
    }
    
    @Test
    public void testGetLeapAmount() {
        long leapInstant = new DateTime(2020, 2, 29, 0, 0).getMillis();
        assertEquals(1, monthField.getLeapAmount(leapInstant));
        
        long nonLeapInstant = new DateTime(2021, 2, 28, 0, 0).getMillis();
        assertEquals(0, monthField.getLeapAmount(nonLeapInstant));
    }
    
    @Test
    public void testRoundFloor() {
        long instant = new DateTime(2021, 6, 15, 12, 30).getMillis();
        long result = monthField.roundFloor(instant);
        assertEquals(new DateTime(2021, 6, 1, 0, 0).getMillis(), result);
    }
    
    @Test
    public void testRemainder() {
        long instant = new DateTime(2021, 6, 15, 12, 30).getMillis();
        long result = monthField.remainder(instant);
        assertEquals(12*60*60*1000 + 30*60*1000, result);
    }
}