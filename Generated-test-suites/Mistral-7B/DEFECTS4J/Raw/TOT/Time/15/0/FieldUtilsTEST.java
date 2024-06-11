import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test with valid integer values
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));

        // Test with an invalid integer value (MIN_VALUE)
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        // Test with valid integer values
        assertEquals(4, FieldUtils.safeAdd(1, 3));
        assertEquals(1024, FieldUtils.safeAdd(512, 512));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));

        // Test with overflow
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        // Test with valid integer values
        assertEquals(3, FieldUtils.safeSubtract(5, 2));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));

        // Test with overflow
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MAX_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiply() {
        // Test with valid integer values
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertEquals(-1, FieldUtils.safeMultiply(-1, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));

        // Test with overflow
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, 2147483648));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongAndInt() {
        // Test with valid long and int values
        long result = FieldUtils.safeMultiply(1, 5);
        assertEquals(5, (int) result);

        result = FieldUtils.safeMultiply(-1, 5);
        assertEquals(-5, (int) result);

        result = FieldUtils.safeMultiply(1, -5);
        assertEquals(-5, (int) result);

        result = FieldUtils.safeMultiply(-1, -5);
        assertEquals(5, (int) result);
    }

    @Test
    public void testSafeMultiplyLongAndLong() {
        // Test with valid long values
        long result = FieldUtils.safeMultiply(1, 5);
        assertEquals(5, (int) result);

        result = FieldUtils.safeMultiply(-1, 5);
        assertEquals(-5, (int) result);

        result = FieldUtils.safeMultiply(1, -5);
        assertEquals(-5, (int) result);

        result = FieldUtils.safeMultiply(-1, -5);
        assertEquals(5, (int) result);

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, 5);
        assertEquals(-1, (int) result);

        result = FieldUtils.safeMultiply(5, Long.MIN_VALUE);
        assertEquals(-1, (int) result);

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE);
        assertEquals(1, (int) result);

        result = FieldUtils.safeMultiply(-1, Long.MIN_VALUE);
        assertEquals(1, (int) result);

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, -1);
        assertEquals(1, (int) result);
    }

    @Test
    public void testSafeToInt() {
        // Test with valid long values
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(-1, FieldUtils.safeToInt(-1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Long.MAX_VALUE));

        // Test with out-of-range long values
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE - 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        // Test with valid long values
        assertEquals(-1, FieldUtils.safeMultiplyToInt(1, -1));
        assertEquals(1, FieldUtils.safeMultiplyToInt(1, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));

        // Test with overflow
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        // Valid values
        FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 15, 1, 31);

        // Invalid values
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 32, 1, 31));
    }

    @Test
    public void testGetWrappedValue() {
        // Test with valid values and wrap range
        assertEquals(0, FieldUtils.getWrappedValue(0, 5, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(1, 5, 1, 5));
        assertEquals(2, FieldUtils.getWrappedValue(6, 5, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(6, 5, 1, 5));

        // Test with negative wrap value
        assertEquals(4, FieldUtils.getWrappedValue(1, 5, 1, 5));
        assertEquals(0, FieldUtils.getWrappedValue(6, 5, 1, 5));

        // Test with invalid wrap range (min >= max)
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 5, 5, 1));
    }
}