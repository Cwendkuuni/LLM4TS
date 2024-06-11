import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = -5;
        val2 = 3;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(0, sum);

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 5L;
        long val2 = 3L;
        long sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, sum);

        val1 = -5L;
        val2 = 3L;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(0L, sum);

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, diff);

        val1 = -10L;
        val2 = 5L;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(-5L, diff);

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = 3;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5L;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = 3L;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = Integer.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, intValue);

        value = Integer.MIN_VALUE + 1;
        intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MIN_VALUE, intValue);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt((long) value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 1, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;
        int currentValue = 7;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(3, wrappedValue);

        currentValue = -3;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(1, wrappedValue);

        currentValue = 7;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(3, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(0, wrappedValue);

        currentValue = 1;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(1, wrappedValue);

        currentValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, 2);
        assertEquals(3, wrappedValue);

        minValue = 10;
        maxValue = 15;
        currentValue = 20;
        try {
            FieldUtils.getWrappedValue(currentValue, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}