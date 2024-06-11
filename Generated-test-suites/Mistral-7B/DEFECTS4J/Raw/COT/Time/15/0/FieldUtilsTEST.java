import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 10;
        int expected = -10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 3, sum = 8;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(sum, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 3L, difference = 7L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(difference, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5, val2 = 3, product = 15;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(product, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L, val2 = 3, product = 15;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(product, result);

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L, val2 = 3L, product = 15;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(product, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10L;
        int expected = 10;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        value = Long.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L, val2 = 3L, product = 15;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(product, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Long.MAX_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyValueBounds_DateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31);

        int value = 35;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 31;
        FieldUtils.verifyValueBounds(field, value, 1, 31);
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        int value = 35;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        value = 31;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
    }

    @Test
    public void testVerifyValueBounds_String() {
        String fieldName = "dayOfMonth";

        int value = 35;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 31;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 10, wrapValue = 5, minValue = 0, maxValue = 5;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(result, 3);

        currentValue = Integer.MIN_VALUE;
        wrapValue = 1;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        currentValue = Integer.MAX_VALUE;
        wrapValue = 1;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        currentValue = Integer.MIN_VALUE;
        wrapValue = Integer.MIN_VALUE;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        currentValue = Integer.MIN_VALUE;
        wrapValue = 6;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        currentValue = Integer.MAX_VALUE;
        wrapValue = 6;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object(), obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj2, obj1));

        obj1 = new Object();
        assertFalse(FieldUtils.equals(obj1, null));
    }
}