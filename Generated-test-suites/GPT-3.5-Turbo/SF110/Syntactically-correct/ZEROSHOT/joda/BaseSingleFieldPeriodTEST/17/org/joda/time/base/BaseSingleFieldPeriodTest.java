import static org.junit.Assert.*;
import org.junit.Test;

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
        // Implement test for getFieldType() method
    }

    @Test
    public void testGetPeriodType() {
        // Implement test for getPeriodType() method
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeWithValidIndex() {
        // Implement test for getFieldType(int) method with valid index
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getFieldType(1);
    }

    @Test
    public void testGetValueWithValidIndex() {
        // Implement test for getValue(int) method with valid index
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
    public void testGetWithDifferentFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(0, period.get(DurationFieldType.YEARS));
    }

    @Test
    public void testIsSupportedWithMatchingFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.isSupported(period.getFieldType()));
    }

    @Test
    public void testIsSupportedWithDifferentFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertFalse(period.isSupported(DurationFieldType.YEARS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Period result = period.toPeriod();
        assertEquals(10, result.getYears());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(10, result.getYears());
    }

    @Test
    public void testEqualsWithSameObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.equals(period));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentReadablePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEqualsWithMatchingReadablePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(850, period.hashCode());
    }

    @Test
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        try {
            period1.compareTo(period2);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals("BaseSingleFieldPeriod cannot be compared to BaseSingleFieldPeriod", e.getMessage());
        }
    }

    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(20);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(1, period1.compareTo(period2));
    }

    @Test
    public void testCompareToWithSmallerValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(0, period1.compareTo(period2));
    }
}