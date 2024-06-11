import org.junit.Assert;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int expected = 8;
        int result = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        long sum = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(Integer.MIN_VALUE - 1, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long expected = 7;
        long result = FieldUtils.safeSubtract(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int expected = 15;
        int result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long expected = 15;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = -5;
        val2 = 2;
        long result2 = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(-15, result2);
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long expected = 15;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = -5;
        val2 = 2;
        long result2 = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(-15, result2);

        val1 = 5;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = 5;
        int expected = 5;
        int result = FieldUtils.safeToInt(value);
        Assert.assertEquals(expected, result);

        value = Long.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int expected = 15;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.millis(), "test");
        int value = 10;
        int lowerBound = 0;
        int upperBound = 5;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected IllegalFieldValueException");
        }

        value = 15;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 2;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        DateTimeFieldType fieldType = DateTimeFieldType.weekyear();
        value = 2023;
        FieldUtils.verifyValueBounds(fieldType, value, 1900, 2100);

        value = 1900;
        FieldUtils.verifyValueBounds(fieldType, value, 1900, 2100);

        value = 2101;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1900, 2100);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        String fieldName = "testField";
        value = 10;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);

        value = 15;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 2;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int currentValue = 10;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(5, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        currentValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(5, wrappedValue);

        currentValue = 0;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(0, wrappedValue);

        wrapValue = 1;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(5, wrappedValue);

        wrapValue = -1;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(4, wrappedValue);

        minValue = 2;
        maxValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(1, wrappedValue);

        minValue = 5;
        maxValue = 10;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(5, wrappedValue);

        minValue = 1;
        maxValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(5, wrappedValue);
    }

    @org.junit.Test
    public void testEquals() {
        String obj1 = "hello";
        String obj2 = "hello";
        boolean expected = true;
        boolean result = FieldUtils.equals(obj1, obj2);
        Assert.assertEquals(expected, result);

        obj1 = null;
        obj2 = "hello";
        result = FieldUtils.equals(obj1, obj2);
        Assert.assertEquals(false, result);

        obj1 = "hello";
        obj2 = null;
        result = FieldUtils.equals(obj1, obj2);
        Assert.assertEquals(false, result);

        obj1 = "hello";
        obj2 = "world";
        result = FieldUtils.equals(obj1, obj2);
        Assert.assertEquals(false, result);
    }
}