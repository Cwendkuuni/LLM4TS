import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(-3, FieldUtils.safeAdd(-1, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeAddLong() {
        assertEquals(3L, FieldUtils.safeAdd(1L, 2L));
        assertEquals(-3L, FieldUtils.safeAdd(-1L, -2L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3L, FieldUtils.safeSubtract(5L, 2L));
        assertEquals(-3L, FieldUtils.safeSubtract(1L, 4L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(6, FieldUtils.safeMultiply(2, 3));
        assertEquals(-6, FieldUtils.safeMultiply(-2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2L));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(6, FieldUtils.safeMultiplyToInt(2L, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L));
    }

    @Test
    public void testVerifyValueBounds() {
        FieldUtils.verifyValueBounds(null, 5, 0, 10);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(null, 15, 0, 10));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(11, 1, 10));
        assertEquals(10, FieldUtils.getWrappedValue(0, 1, 10));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("abc", "abc"));
        assertFalse(FieldUtils.equals("abc", "def"));
    }

}