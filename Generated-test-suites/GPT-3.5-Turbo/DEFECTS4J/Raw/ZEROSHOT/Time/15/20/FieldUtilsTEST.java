import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeNegate(-Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeAdd_long() {
        assertEquals(3L, FieldUtils.safeAdd(1L, 2L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(-1L, FieldUtils.safeSubtract(1L, 2L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeSubtract(Long.MIN_VALUE, 0L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeSubtract(Long.MAX_VALUE, 0L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testSafeMultiply_int() {
        assertEquals(6, FieldUtils.safeMultiply(2, 3));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply_long_int() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals(-2L, FieldUtils.safeMultiply(2L, -1));
        assertEquals(0L, FieldUtils.safeMultiply(0L, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiply_long() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals(2L, FieldUtils.safeMultiply(2L, 1L));
        assertEquals(0L, FieldUtils.safeMultiply(0L, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1L));
        assertEquals(-1, FieldUtils.safeToInt(-1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(6, FieldUtils.safeMultiplyToInt(2L, 3L));
        assertEquals(2, FieldUtils.safeMultiplyToInt(2L, 1L));
        assertEquals(0, FieldUtils.safeMultiplyToInt(0L, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds_DateTimeField() {
        DateTimeField field = null; // TODO: Mock the DateTimeField object
        FieldUtils.verifyValueBounds(field, 1, 0, 2);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 3, 0, 2));
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        DateTimeFieldType fieldType = null; // TODO: Mock the DateTimeFieldType object
        FieldUtils.verifyValueBounds(fieldType, 1, 0, 2);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 3, 0, 2));
    }

    @Test
    public void testVerifyValueBounds_String() {
        FieldUtils.verifyValueBounds("field", 1, 0, 2);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds("field", 3, 0, 2));
    }

    @Test
    public void testGetWrappedValue_int_int_int() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 2));
        assertEquals(0, FieldUtils.getWrappedValue(-1, 0, 2));
        assertEquals(2, FieldUtils.getWrappedValue(3, 0, 2));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 2, 0));
    }

    @Test
    public void testGetWrappedValue_int_int_int_minMaxValueEqual() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 1, 1));
    }

    @Test
    public void testGetWrappedValue_int_int_int_negativeValue() {
        assertEquals(1, FieldUtils.getWrappedValue(-1, 0, 2));
    }

    @Test
    public void testGetWrappedValue_overflow() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test", null));
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "test2"));
    }
    
}