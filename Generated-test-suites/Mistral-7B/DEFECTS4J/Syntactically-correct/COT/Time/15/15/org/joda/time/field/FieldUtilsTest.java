import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 10;
        int expected = -value;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 10;
        int val2 = 5;
        int expectedSum = val1 + val2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expectedSum, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 10L;
        long val2 = 5L;
        long expectedSum = val1 + val2;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expectedSum, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long expectedDiff = val1 - val2;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expectedDiff, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 10;
        int val2 = 5;
        int expectedProduct = val1 * val2;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expectedProduct, result);

        val1 = Integer.MIN_VALUE;
        val2 = 10;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyIntLong() {
        int val1 = 10;
        long val2 = 5;
        long expectedProduct = (long) val1 * val2;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expectedProduct, result);
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10L;
        long val2 = 5L;
        long expectedProduct = val1 * val2;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expectedProduct, result);
    }

    @Test
    public void testSafeToInt() {
        long value = 100L;
        int expected = (int) value;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);

        value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10L;
        long val2 = 5L;
        int expectedProduct = (int) (val1 * val2);
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(expectedProduct, result);
    }

    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31);
        int value = 32;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 30);
            assert false; // Exception should have been thrown
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 31);
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 32;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 30);
            assert false; // Exception should have been thrown
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "testField";
        int value = 32;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 30);
            assert false; // Exception should have been thrown
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 15;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 10;
        int expectedWrappedValue = 3;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(expectedWrappedValue, result);

        currentValue = Integer.MIN_VALUE;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(FieldUtils.equals(object1, object2));

        object2 = object1;
        assertTrue(FieldUtils.equals(object1, object2));

        object1 = null;
        assertFalse(FieldUtils.equals(object1, object2));
    }
}