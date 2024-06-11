
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(20);
        assertEquals(20, period.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        // TODO: Write test case for getFieldType()
    }
    
    @Test
    public void testGetPeriodType() {
        // TODO: Write test case for getPeriodType()
    }
    
    @Test
    public void testSize() {
        assertEquals(1, new BaseSingleFieldPeriod(10).size());
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(period.getFieldType(), period.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getFieldType(1);
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(period.getValue(), period.getValue(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getValue(1);
    }
    
    @Test
    public void testGetWithMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.get(period.getFieldType()));
    }
    
    @Test
    public void testGetWithNonMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(0, period.get(DurationFieldType.hours()));
    }
    
    @Test
    public void testIsSupportedWithMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.isSupported(period.getFieldType()));
    }
    
    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }
    
    @Test
    public void testToPeriod() {
        // TODO: Write test case for toPeriod()
    }
    
    @Test
    public void testToMutablePeriod() {
        // TODO: Write test case for toMutablePeriod()
    }
    
    @Test
    public void testEqualsWithEqualPeriods() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));
    }
    
    @Test
    public void testEqualsWithDifferentPeriods() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        assertFalse(period1.equals(period2));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(27 * (27 * 17 + 10) + period.getFieldType().hashCode(), period.hashCode());
    }
    
    @Test
    public void testCompareToWithSamePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(0, period1.compareTo(period2));
    }
    
    @Test
    public void testCompareToWithGreaterPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(20);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(1, period1.compareTo(period2));
    }
    
    @Test
    public void testCompareToWithSmallerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        assertEquals(-1, period1.compareTo(period2));
    }
}
