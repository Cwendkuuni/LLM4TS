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
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
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
        long result1 = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(Integer.MIN_VALUE, result1);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
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
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
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
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
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
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long expected = 15;
        long result = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
        }

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
        }
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = 10;
        int expected = 10;
        int result = FieldUtils.safeToInt(value);
        Assert.assertEquals(expected, result);

        value = Long.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
        }

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertTrue(true);
        }
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.hours();
        int value = 23;
        int lowerBound = 0;
        int upperBound = 23;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            Assert.fail();
        }

        value = 24;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertTrue(true);
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 3;
        int minValue = 1;
        int maxValue = 5;
        int expected = 3;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        minValue = 1;
        maxValue = Integer.MAX_VALUE;
        int result1 = FieldUtils.getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, result1);

        value = Integer.MIN_VALUE + 1;
        minValue = 1;
        maxValue = Integer.MAX_VALUE;
        int result2 = FieldUtils.getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(maxValue, result2);

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        Assert.assertTrue(FieldUtils.equals(obj1, obj2));
    }
}