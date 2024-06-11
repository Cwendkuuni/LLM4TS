package org.joda.time.field; // make sure the package is correct for imports

import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.field.FieldUtils.*;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int value = 10;
        int result = safeNegate(value);
        Assert.assertEquals(-10, result);

        value = 0;
        try {
            safeNegate(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }

        value = Integer.MIN_VALUE;
        try {
            safeNegate(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = safeAdd(val1, val2);
        Assert.assertEquals(8, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = safeAdd(val1, val2);
        Assert.assertEquals(Integer.MIN_VALUE, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            safeAdd(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: -2147483648 + -2147483648", e.getMessage());
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 4L;
        long result = safeSubtract(val1, val2);
        Assert.assertEquals(6L, result);

        val1 = 10L;
        val2 = Long.MAX_VALUE;
        try {
            safeSubtract(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: 10 - 9223372036854775807", e.getMessage());
        }

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            safeSubtract(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: -9223372036854775808 - 1", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 10;
        int val2 = 3;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(30, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: -2147483648 * 2", e.getMessage());
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: -2147483648 * -2147483648", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 10L;
        int val2 = 3;
        long result = safeMultiply(val1, val2);
        Assert.assertEquals(30L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: -9223372036854775808 * 2", e.getMessage());
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: -9223372036854775808 * -2147483648", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10L;
        long val2 = 3L;
        long result = safeMultiply(val1, val2);
        Assert.assertEquals(30L, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: -9223372036854775808 * -9223372036854775808", e.getMessage());
        }
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = safeToInt(value);
        Assert.assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            safeToInt(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Value cannot fit in an int: -9223372036854775808", e.getMessage());
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10L;
        long val2 = 3L;
        int result = safeMultiplyToInt(val1, val2);
        Assert.assertEquals(30, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            safeMultiplyToInt(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: -9223372036854775808 * 2", e.getMessage());
        }

        val1 = Integer.MIN_VALUE;
        val2 = 3L;
        try {
            safeMultiplyToInt(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: -2147483648 * 2", e.getMessage());
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        try {
            verifyValueBounds(fieldType, 33, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 33, maximum allowed: 31", e.getMessage());
        }

        try {
            verifyValueBounds(fieldType, 29, 1, 28);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 29, minimum allowed: 1", e.getMessage());
        }

        try {
            verifyValueBounds(fieldType, 0, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 0, minimum allowed: 1", e.getMessage());
        }

        DateTimeField field = new DateTimeField(fieldType, "test");
        try {
            FieldUtils.verifyValueBounds(field, 33, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 33, maximum allowed: 31 for test", e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(field, 29, 1, 28);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 29, minimum allowed: 1 for test", e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(field, 0, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value out of range: 0, minimum allowed: 1 for test", e.getMessage());
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(value, result);

        value = 6;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(1, result);

        value = 1;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(value, result);

        value = 7;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(5, result);

        value = 0;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, result);

        minValue = maxValue; // must be greater than minValue
        try {
            getWrappedValue(value, minValue, maxValue);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("MIN > MAX", e.getMessage());
        }

        value = Integer.MIN_VALUE;
        try {
            getWrappedValue(value, minValue, maxValue);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("MIN > MAX", e.getMessage());
        }

        minValue = 0;
        maxValue = Integer.MIN_VALUE;
        try {
            getWrappedValue(value, minValue, maxValue);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("MIN > MAX", e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse(equals(obj1, obj2));

        Object obj3 = obj1;
        Assert.assertTrue(equals(obj1, obj3));

        obj3 = null;
        Assert.assertFalse(equals(obj1, obj3));
    }

    
}