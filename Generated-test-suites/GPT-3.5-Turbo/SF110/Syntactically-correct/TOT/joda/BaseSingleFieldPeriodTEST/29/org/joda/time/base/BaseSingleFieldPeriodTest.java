import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;
    
    @Before
    public void setUp() {
        period = new MockSingleFieldPeriod(10);
    }

    // Test cases for getValue() method
    
    @Test
    public void testGetValue() {
        assertEquals(10, period.getValue());
    }
    
    // Test cases for setValue() method

    @Test
    public void testSetValue() {
        period.setValue(20);
        assertEquals(20, period.getValue());
    }
    
    // Test cases for getFieldType() method
    
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.YEARS, period.getFieldType());
    }
    
    // Test cases for getPeriodType() method
    
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }
    
    // Test cases for size() method

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }
    
    // Test cases for getFieldType(int) method
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        assertEquals(DurationFieldType.YEARS, period.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        period.getFieldType(1);
    }
    
    // Test cases for getValue(int) method
    
    @Test
    public void testGetValueWithValidIndex() {
        assertEquals(10, period.getValue(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        period.getValue(1);
    }
    
    // Test cases for get(DurationFieldType) method
    
    @Test
    public void testGetWithMatchingFieldType() {
        assertEquals(10, period.get(DurationFieldType.YEARS));
    }
    
    @Test
    public void testGetWithNonMatchingFieldType() {
        assertEquals(0, period.get(DurationFieldType.MONTHS));
    }
    
    // Test cases for isSupported(DurationFieldType) method
    
    @Test
    public void testIsSupportedWithMatchingFieldType() {
        assertTrue(period.isSupported(DurationFieldType.YEARS));
    }
    
    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        assertFalse(period.isSupported(DurationFieldType.MONTHS));
    }
    
    // Test cases for toPeriod() method
    
    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        assertNotNull(result);
        assertEquals(10, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }
    
    // Test cases for toMutablePeriod() method
    
    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        assertNotNull(result);
        assertEquals(10, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }
    
    // Test cases for equals(Object) method
    
    @Test
    public void testEqualsWithEqualObjects() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(10);
        assertTrue(period.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(20);
        assertFalse(period.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(period.equals("Not a BaseSingleFieldPeriod object"));
    }
    
    // Test cases for hashCode() method
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(10);
        assertEquals(period.hashCode(), other.hashCode());
    }
    
    // Test cases for compareTo(BaseSingleFieldPeriod) method
    
    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(5);
        assertEquals(1, period.compareTo(other));
    }
    
    @Test
    public void testCompareToWithSmallerValue() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(15);
        assertEquals(-1, period.compareTo(other));
    }
    
    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(10);
        assertEquals(0, period.compareTo(other));
    }
    
    // Custom MockSingleFieldPeriod class for testing
    
    private class MockSingleFieldPeriod extends BaseSingleFieldPeriod {
        
        public MockSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }
        
        public DurationFieldType getFieldType() {
            return DurationFieldType.YEARS;
        }
        
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}