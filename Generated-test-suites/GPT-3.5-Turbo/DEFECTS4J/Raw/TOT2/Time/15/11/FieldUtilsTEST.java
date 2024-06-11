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
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(0, FieldUtils.safeSubtract(5, 5));
        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyInt() {
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertEquals(0, FieldUtils.safeMultiply(0, 5));
        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(100000000, FieldUtils.safeMultiply(10000L, 10000L));
        assertEquals(0, FieldUtils.safeMultiply(0L, 10000L));
        try {
            FieldUtils.safeMultiply(Long.MAX_VALUE, 2);
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
    public void testSafeMultiplyToInt() {
        assertEquals(10, FieldUtils.safeMultiplyToInt(2, 5));
        assertEquals(0, FieldUtils.safeMultiplyToInt(0, 5));
        try {
            FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(10, 3, 5));
        assertEquals(5, FieldUtils.getWrappedValue(10, 3, 5));
        assertEquals(3, FieldUtils.getWrappedValue(-2, 3, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-7, 3, 5));
        try {
            FieldUtils.getWrappedValue(5, 5, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals("abc", null));
        assertFalse(FieldUtils.equals(null, "abc"));
        assertTrue(FieldUtils.equals("abc", "abc"));
    }

}