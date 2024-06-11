package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(Long.MAX_VALUE + 1, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, -1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-(Long.MAX_VALUE + 1), FieldUtils.safeSubtract(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, -1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 3));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(-3, FieldUtils.safeMultiply(1, -3));
        assertEquals(0, FieldUtils.safeMultiply(0, 3));
        assertEquals(1, FieldUtils.safeMultiply(1, 3));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(1, FieldUtils.safeMultiply(1, 1));
        assertEquals(0, FieldUtils.safeMultiply(0, 3));
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(-1, FieldUtils.safeMultiply(-1, 1));
        assertEquals(-1, FieldUtils.safeMultiply(1, -1));
        assertEquals(-1, FieldUtils.safeMultiply(-1, -1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(42, FieldUtils.safeToInt(42L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(3, FieldUtils.safeMultiplyToInt(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE + 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 31, 32));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 32, 31, 31));
        assertEquals(31, FieldUtils.verifyValueBounds(fieldType, 31, 31, 31));
        assertEquals(30, FieldUtils.verifyValueBounds(fieldType, 30, 31, 31));
        assertEquals(28, FieldUtils.verifyValueBounds(fieldType, 28, 28, 29)); // Leap year
        assertEquals(29, FieldUtils.verifyValueBounds(fieldType, 29, 29, 29)); // Leap year

        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31, new LocalDate());
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 31, 32));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 31, 31));
        assertEquals(31, FieldUtils.verifyValueBounds(field, 31, 31, 31));
        assertEquals(30, FieldUtils.verifyValueBounds(field, 30, 31, 31));
        assertEquals(28, FieldUtils.verifyValueBounds(field, 28, 28, 29)); // Leap year
        assertEquals(29, FieldUtils.verifyValueBounds(field, 29, 29, 29)); // Leap year
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 5, 0, 5));
        assertEquals(6, FieldUtils.getWrappedValue(11, 5, 0, 5));
        assertEquals(-1, FieldUtils.getWrappedValue(-1, 5, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-5, 5, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 5, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 5, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(Integer.MAX_VALUE + 1, 5, 0, 5));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE - 1, 5, 0, 5));
    }

    @Test
    public void testEquals() {
        assertEquals("java.lang.Object@12345678", FieldUtils.equals(null, new Object() {}));
        assertEquals("java.lang.Object@12345678", FieldUtils.equals(new Object() {}, new Object() {}));
        assertFalse(FieldUtils.equals(null, new Object() {}));
        assertFalse(FieldUtils.equals(new Object() {}, null));
        assertTrue(FieldUtils.equals(new Object() {}, new Object() {}));
    }
}