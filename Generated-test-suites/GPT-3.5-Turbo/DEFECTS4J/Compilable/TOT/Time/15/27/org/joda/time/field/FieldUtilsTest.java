package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.field.FieldUtils;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Typical use case - negating a positive value
        int result = FieldUtils.safeNegate(5);
        assertEquals(-5, result);
        
        // Error scenario - trying to negate Integer.MIN_VALUE
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
    
    @Test
    public void testSafeAdd() {
        // Typical use case - adding two positive values
        int result = FieldUtils.safeAdd(2, 3);
        assertEquals(5, result);
        
        // Error scenario - adding two positive values results in overflow
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 + 1", e.getMessage());
        }
    }
    
    @Test
    public void testSafeSubtract() {
        // Typical use case - subtracting a smaller value from a larger value
        long result = FieldUtils.safeSubtract(10, 5);
        assertEquals(5, result);
        
        // Error scenario - subtracting a larger value from a smaller value results in overflow
        try {
            FieldUtils.safeSubtract(5, 10);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 5 - 10", e.getMessage());
        }
    }
    
    @Test
    public void testSafeMultiply() {
        // Typical use case - multiplying two positive values
        int result = FieldUtils.safeMultiply(2, 3);
        assertEquals(6, result);
        
        // Error scenario - multiplying two positive values results in overflow
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: 2147483647 * 2", e.getMessage());
        }
    }
    
    @Test
    public void testSafeMultiplyToInt() {
        // Typical use case - multiplying two long values that result in an int value
        int result = FieldUtils.safeMultiplyToInt(2, 3);
        assertEquals(6, result);
        
        // Error scenario - multiplying two long values results in overflow
        try {
            FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows a long: 2147483647 * 2", e.getMessage());
        }
    }
    
    @Test
    public void testSafeToInt() {
        // Typical use case - casting a long value within the int range
        int result = FieldUtils.safeToInt(10);
        assertEquals(10, result);
        
        // Error scenario - casting a long value outside the int range
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("ArithmeticException not thrown");
        } catch (ArithmeticException e) {
            assertEquals("Value cannot fit in an int: 9223372036854775807", e.getMessage());
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        // Typical use case - wrapping a value within the specified range
        int result = FieldUtils.getWrappedValue(10, 5, 15);
        assertEquals(10, result);
        
        // Edge case - wrapping a value that reaches the upper bound of the range
        result = FieldUtils.getWrappedValue(15, 5, 15);
        assertEquals(5, result);
        
        // Error scenario - minValue is greater than maxValue
        try {
            FieldUtils.getWrappedValue(10, 15, 5);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("MIN > MAX", e.getMessage());
        }
    }
    
    @Test
    public void testEquals() {
        // Typical use case - comparing two equal objects
        boolean result = FieldUtils.equals("test", "test");
        assertTrue(result);
        
        // Typical use case - comparing two different objects
        result = FieldUtils.equals("test1", "test2");
        assertFalse(result);
        
        // Typical use case - comparing an object with null
        result = FieldUtils.equals("test", null);
        assertFalse(result);
    }
}