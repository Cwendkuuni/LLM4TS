import org.junit.Test;
import org.junit.Assert.*;
import static org.joda.time.field.FieldUtils.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int input = 5;
        int expected = -5;
        int result = safeNegate(input);
        assertEquals(expected, result);

        input = Integer.MIN_VALUE;
        try {
            safeNegate(input);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3, val2 = 4;
        int sum = safeAdd(val1, val2);
        assertEquals(7, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        long result = safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, val1);
        assertEquals(result, val1 + val2);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 3L, val2 = 4L;
        long sum = safeAdd(val1, val2);
        assertEquals(7L, sum);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        long result = safeAdd(val1, val2);
        assertEquals(Long.MIN_VALUE, val1);
        assertEquals(result, val1 + val2);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 5L;
        long diff = safeSubtract(val1, val2);
        assertEquals(5L, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3, val2 = 4;
        int result = safeMultiply(val1, val2);
        assertEquals(12, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 3, val2 = 4;
        long result = safeMultiply(val1, val2);
        assertEquals(12L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 3, val2 = 4;
        long result = safeMultiply(val1, val2);
        assertEquals(12L, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long testValue = Long.MAX_VALUE;
        int expected = Integer.MAX_VALUE;
        int result = safeToInt(testValue);
        assertEquals(expected, result);

        testValue = Long.MIN_VALUE;
        expected = Integer.MIN_VALUE;
        result = safeToInt(testValue);
        assertEquals(expected, result);

        testValue = Long.MIN_VALUE + 1;
        try {
            safeToInt(testValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3, val2 = 4;
        int result = safeMultiplyToInt(val1, val2);
        assertEquals(12, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31);
        try {
            verifyValueBounds(field, 0, 28, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(field, 32, 28, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            verifyValueBounds(fieldType, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(fieldType, 15, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = Integer.MIN_VALUE - 5;
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MIN_VALUE + 4;
        int result = getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        value = Integer.MIN_VALUE + 5;
        result = getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue, result);

        value = Integer.MIN_VALUE + 1;
        minValue = 1;
        maxValue = 5;
        result = getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertTrue(equals(obj1, obj1));
        assertTrue(equals(obj1, obj3));
        assertFalse(equals(obj1, obj2));
        assertFalse(equals(null, obj1));
    }

}