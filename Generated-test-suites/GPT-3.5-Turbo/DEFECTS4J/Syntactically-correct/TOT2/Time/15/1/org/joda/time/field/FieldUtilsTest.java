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
        assertEquals(-10, FieldUtils.safeAdd(-5, -5));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(-5, FieldUtils.safeSubtract(-10, -5));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertEquals(0, FieldUtils.safeMultiply(0, 5));
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
        assertEquals(-5, FieldUtils.safeToInt(-5));
        try {
            FieldUtils.safeToInt(Long.MAX_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        try {
            FieldUtils.verifyValueBounds("Test", 10, 5, 8);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
        FieldUtils.verifyValueBounds("Test", 5, 5, 8);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(10, 5, 9));
        assertEquals(5, FieldUtils.getWrappedValue(15, 5, 9));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 5, 9));
        try {
            FieldUtils.getWrappedValue(10, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals(null, "test"));
        assertTrue(FieldUtils.equals(null, null));
    }
}