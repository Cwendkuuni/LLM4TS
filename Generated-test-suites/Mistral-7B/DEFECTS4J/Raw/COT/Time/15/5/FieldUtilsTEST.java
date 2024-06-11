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
        int val1 = 5, val2 = 3, sum;

        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 5L, val2 = 3L, sum;

        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, sum);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 4L, diff;

        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(6L, diff);

        val1 = 2L;
        val2 = 1L;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = 11L;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3, val2 = 2, product;

        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLong() {
        long val1 = 3L, val2 = 2L, product;

        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, product);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 3L, val2 = 2;

        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, product);

        val1 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyIntLong() {
        int val1 = 3, val2 = 2;

        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 5L;

        int result = FieldUtils.safeToInt(value);
        assertEquals(5, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L, val2 = 3L;

        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.centuryOfEra(), 2000);

        try {
            FieldUtils.verifyValueBounds(field, 19, 1, 2);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 19, 1, 3);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 20, 1, 2);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 20, 1, 3);
            // no exception expected
        } catch (IllegalFieldValueException e) {
            fail();
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10, minValue = 5, maxValue = 15;

        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(10, result);

        result = FieldUtils.getWrappedValue(value - 1, minValue, maxValue);
        assertEquals(11, result);

        result = FieldUtils.getWrappedValue(value + 1, minValue, maxValue);
        assertEquals(11, result);

        value = minValue - 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue, result);

        value = maxValue + 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        value = minValue;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object(), obj2 = new Object();
        assertEquals(false, FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertEquals(false, FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertEquals(true, FieldUtils.equals(obj1, obj2));

        String str1 = new String("Test"), str2 = new String("Test");
        assertEquals(true, FieldUtils.equals(str1, str2));
    }
}