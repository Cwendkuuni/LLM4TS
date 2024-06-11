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
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(-5, FieldUtils.safeAdd(-10, 5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertEquals(-25, FieldUtils.safeMultiply(5, -5));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        // Typical case
        FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfDay(), 5, 0, 59);
        
        // Lower bound edge case
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.secondOfDay(), -1, 0, 59);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
        
        // Upper bound edge case
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 24, 0, 23);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(25, 5, 0, 10));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 5, 0, 10));
        assertEquals(0, FieldUtils.getWrappedValue(0, 5, 0, 10));
        assertEquals(10, FieldUtils.getWrappedValue(-1, 10, 0, 10));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "another"));
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals("test", null));
    }
}