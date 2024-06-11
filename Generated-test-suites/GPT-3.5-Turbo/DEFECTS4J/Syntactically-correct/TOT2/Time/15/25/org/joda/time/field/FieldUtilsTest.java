import static org.junit.Assert.*;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
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
            // Pass
        }
    }

    @Test
    public void testSafeAdd() {
        // Typical case
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        
        // Edge cases
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testSafeMultiply() {
        // Typical case
        assertEquals(20, FieldUtils.safeMultiply(4, 5));
        
        // Edge cases
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testSafeSubtract() {
        // Typical case
        assertEquals(10, FieldUtils.safeSubtract(15, 5));
        
        // Edge cases
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testSafeToInt() {
        // Typical case
        assertEquals(10, FieldUtils.safeToInt(10L));
        
        // Edge cases
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        // Typical case
        assertEquals(20, FieldUtils.safeMultiplyToInt(4, 5));
        
        // Edge cases
        try {
            FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @Test
    public void testVerifyValueBounds() {
        // Typical case
        FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), 15, 1, 31);
        
        // Edge cases
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), 40, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Pass
        }
    }

    @Test
    public void testGetWrappedValue() {
        // Typical case
        assertEquals(2, FieldUtils.getWrappedValue(12, 10, 20));
        
        // Edge cases
        try {
            FieldUtils.getWrappedValue(25, 20, 20);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testEquals() {
        // Typical case
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "test2"));
        
        // Edge cases
        assertFalse(FieldUtils.equals("test", null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertTrue(FieldUtils.equals(null, null));
    }

}