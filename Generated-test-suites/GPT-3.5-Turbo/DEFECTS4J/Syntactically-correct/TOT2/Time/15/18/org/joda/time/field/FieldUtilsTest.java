import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {
    
    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(0, FieldUtils.safeAdd(0, 0));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(8, 3));
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeMultiply() {
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertEquals(0, FieldUtils.safeMultiply(0, 100));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetWrappedValue() {
        assertEquals(2, FieldUtils.getWrappedValue(8, 4, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 6, 0, 5));
        try {
            FieldUtils.getWrappedValue(5, 1, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("hello", "hello"));
        assertFalse(FieldUtils.equals("hello", "world"));
        assertFalse(FieldUtils.equals(null, "hello"));
    }
    
}