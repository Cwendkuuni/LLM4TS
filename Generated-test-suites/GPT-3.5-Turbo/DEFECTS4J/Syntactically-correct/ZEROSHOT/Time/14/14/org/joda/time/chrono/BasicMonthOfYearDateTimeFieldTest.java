import static org.junit.Assert.*;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField monthField;
    private BasicChronology chronology;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        monthField = new BasicMonthOfYearDateTimeField(chronology, 8);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(monthField.isLenient());
    }
    
    @Test
    public void testGet() {
        long instant = 123456789L;
        assertEquals(10, monthField.get(instant));
    }
    
    @Test
    public void testAdd() {
        long instant = 123456789L;
        int months = 3;
        long result = monthField.add(instant, months);
        long expected = 456789123L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testAddWrapField() {
        long instant = 123456789L;
        int months = 3;
        long result = monthField.addWrapField(instant, months);
        long expected = 123456789L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = 123456789L;
        long subtrahendInstant = 987654321L;
        long result = monthField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        long expected = -5L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testSet() {
        long instant = 123456789L;
        int month = 7;
        long result = monthField.set(instant, month);
        long expected = 123456789L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testIsLeap() {
        long instant1 = 123456789L;
        long instant2 = 987654321L;
        assertFalse(monthField.isLeap(instant1));
        assertTrue(monthField.isLeap(instant2));
    }
    
    @Test
    public void testGetLeapAmount() {
        long instant1 = 123456789L;
        long instant2 = 987654321L;
        assertEquals(0, monthField.getLeapAmount(instant1));
        assertEquals(1, monthField.getLeapAmount(instant2));
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
        long instant = 123456789L;
        long result = monthField.roundFloor(instant);
        long expected = 123456789L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemainder() {
        long instant = 123456789L;
        long result = monthField.remainder(instant);
        long expected = 0L;
        assertEquals(expected, result);
    }
    
    @Test
    public void testSerialization() {
        assertSame(monthField, monthField.readResolve());
    }
}