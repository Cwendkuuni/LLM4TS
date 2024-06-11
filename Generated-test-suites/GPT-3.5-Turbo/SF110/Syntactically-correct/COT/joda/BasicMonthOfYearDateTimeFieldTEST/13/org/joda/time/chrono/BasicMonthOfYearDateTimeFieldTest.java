import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    private BasicMonthOfYearDateTimeField field;
    
    @Before
    public void setup() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        int month = field.get(n);
        assertEquals(12, month);
    }
    
    @Test
    public void testAdd() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.add(n, 2);
        assertEquals(1579396800000L, result); // Thursday, 30 January 2020 00:00:00
    }
    
    @Test
    public void testAddWithLong() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.add(n, 2);
        assertEquals(1579396800000L, result); // Thursday, 30 January 2020 00:00:00
    }
    
    @Test
    public void testAddWrapField() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.addWrapField(n, 11);
        assertEquals(2437550400000L, result); // Saturday, 30 November 2046 00:00:00
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        long n1 = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long n2 = 1579396800000L; // Thursday, 30 January 2020 00:00:00
        long result = field.getDifferenceAsLong(n1, n2);
        assertEquals(1, result);
    }
    
    @Test
    public void testSet() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.set(n, 3);
        assertEquals(1580402400000L, result); // Sunday, 1 March 2020 00:00:00
    }
    
    @Test
    public void testIsLeap() {
        long n1 = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long n2 = 1578847200000L; // Thursday, 16 January 2020 00:00:00
        assertFalse(field.isLeap(n1));
        assertTrue(field.isLeap(n2));
    }
    
    @Test
    public void testGetLeapAmount() {
        long n1 = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long n2 = 1578847200000L; // Thursday, 16 January 2020 00:00:00
        assertEquals(0, field.getLeapAmount(n1));
        assertEquals(1, field.getLeapAmount(n2));
    }
    
    @Test
    public void testGetMinimumValue() {
        int minValue = field.getMinimumValue();
        assertEquals(1, minValue);
    }
    
    @Test
    public void testGetMaximumValue() {
        int maxValue = field.getMaximumValue();
        assertEquals(12, maxValue);
    }
    
    @Test
    public void testRoundFloor() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.roundFloor(n);
        assertEquals(1575170400000L, result); // Sunday, 1 December 2019 00:00:00
    }
    
    @Test
    public void testRemainder() {
        long n = 1576788000000L; // Monday, 30 December 2019 00:00:00
        long result = field.remainder(n);
        assertEquals(1616000000L, result); // 06:00:00
    }
}