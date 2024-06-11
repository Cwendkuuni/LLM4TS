import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        int value = period.getValue();
        
        // Assert
        assertEquals(10, value);
    }
    
    @Test
    public void testSetValue() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        period.setValue(20);
        
        // Assert
        assertEquals(20, period.getValue());
    }
    
    @Test
    public void testGetFieldType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        DurationFieldType fieldType = period.getFieldType();
        
        // Assert
        assertNotNull(fieldType);
    }
    
    @Test
    public void testGetPeriodType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        PeriodType periodType = period.getPeriodType();
        
        // Assert
        assertNotNull(periodType);
    }
    
    @Test
    public void testSize() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        int size = period.size();
        
        // Assert
        assertEquals(1, size);
    }
    
    @Test
    public void testGetFieldTypeWithValidIndex() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        DurationFieldType fieldType = period.getFieldType(0);
        
        // Assert
        assertNotNull(fieldType);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        period.getFieldType(1);
        
        // Assert
        // Exception expected
    }
    
    @Test
    public void testGetValueWithValidIndex() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        int value = period.getValue(0);
        
        // Assert
        assertEquals(10, value);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        period.getValue(1);
        
        // Assert
        // Exception expected
    }
    
    @Test
    public void testGetWithValidFieldType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = period.getFieldType();
        
        // Act
        int value = period.get(fieldType);
        
        // Assert
        assertEquals(10, value);
    }
    
    @Test
    public void testGetWithInvalidFieldType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = DurationFieldType.days();
        
        // Act
        int value = period.get(fieldType);
        
        // Assert
        assertEquals(0, value);
    }
    
    @Test
    public void testIsSupportedWithValidFieldType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = period.getFieldType();
        
        // Act
        boolean supported = period.isSupported(fieldType);
        
        // Assert
        assertTrue(supported);
    }
    
    @Test
    public void testIsSupportedWithInvalidFieldType() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = DurationFieldType.days();
        
        // Act
        boolean supported = period.isSupported(fieldType);
        
        // Assert
        assertFalse(supported);
    }
    
    @Test
    public void testToPeriod() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        Period result = period.toPeriod();
        
        // Assert
        assertNotNull(result);
        assertEquals(10, result.get(period.getFieldType()));
    }
    
    @Test
    public void testToMutablePeriod() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        MutablePeriod result = period.toMutablePeriod();
        
        // Assert
        assertNotNull(result);
        assertEquals(10, result.get(period.getFieldType()));
    }
    
    @Test
    public void testEqualsWithEqualPeriod() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        
        // Act
        boolean result = period1.equals(period2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithDifferentPeriodType() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
            public PeriodType getPeriodType() { return PeriodType.days(); }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
            public PeriodType getPeriodType() { return PeriodType.months(); }
        };
        
        // Act
        boolean result = period1.equals(period2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentValue() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        
        // Act
        boolean result = period1.equals(period2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        
        // Act
        int hashCode = period.hashCode();
        
        // Assert
        assertNotNull(hashCode);
    }
    
    @Test
    public void testCompareToWithGreaterValue() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(20);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        
        // Act
        int result = period1.compareTo(period2);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testCompareToWithSmallerValue() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        
        // Act
        int result = period1.compareTo(period2);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompareToWithEqualValue() {
        // Arrange
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        
        // Act
        int result = period1.compareTo(period2);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        // Arrange
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Object other = new Object();
        
        // Act
        period.compareTo(other);
        
        // Assert
        // Exception expected
    }

}