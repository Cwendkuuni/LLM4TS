import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1));
    }

    @Test
    public void testSafeMultiply_int() {
        assertEquals(20, FieldUtils.safeMultiply(5, 4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(100000, 100000));
    }

    @Test
    public void testSafeMultiply_long_int() {
        assertEquals(2000000000L, FieldUtils.safeMultiply(1000000L, 2000));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiply_long_long() {
        assertEquals(2000000000L, FieldUtils.safeMultiply(1000000L, 2000L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(100, FieldUtils.safeToInt(100));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(20, FieldUtils.safeMultiplyToInt(5, 4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds_DateTimeField() {
        // Assuming a DateTimeField with lower bound 0 and upper bound 10
        assertDoesNotThrow(() -> FieldUtils.verifyValueBounds(dateTimeField, 5, 0, 10));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(dateTimeField, 15, 0, 10));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(15, 5, 10));
        assertEquals(0, FieldUtils.getWrappedValue(10, 0, 10));
        assertEquals(9, FieldUtils.getWrappedValue(-1, 0, 10));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertTrue(FieldUtils.equals("test", "test"));
    }
}