import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        try {
            result = FieldUtils.safeNegate(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 1;
        long val2 = 2;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10;
        int result = FieldUtils.safeToInt(value);
        assertEquals(10, result);

        value = Long.MAX_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.hours(), "hours", 0, 23);
        int lowerBound = 0;
        int upperBound = 23;

        try {
            FieldUtils.verifyValueBounds(field, 0, lowerBound - 1, upperBound);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 24, lowerBound, upperBound);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 12, lowerBound, upperBound);
            // no exception expected
        } catch (IllegalFieldValueException e) {
            fail();
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 4;
        int wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = Integer.MIN_VALUE;
        try {
            wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = 0;
        try {
            wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        wrappedValue = FieldUtils.getWrappedValue(value, -1, minValue, maxValue);
        assertEquals(Integer.MIN_VALUE, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value, 5, minValue, maxValue);
        assertEquals(3, wrappedValue);
    }
}