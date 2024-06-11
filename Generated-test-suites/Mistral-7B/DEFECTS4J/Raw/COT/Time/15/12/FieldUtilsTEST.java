import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.junit.Assert;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        int expected = -5;
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
        } catch (ArithmeticException e) {
            String expectedMessage = "Integer.MIN_VALUE cannot be negated";
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        long result = FieldUtils.safeAdd(val1, val2);
        long expected = 7L;
        Assert.assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        expected = Integer.MIN_VALUE;
        Assert.assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "The calculation caused an overflow: " + Integer.MIN_VALUE + " + " + Integer.MIN_VALUE;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long result = FieldUtils.safeSubtract(val1, val2);
        long expected = 5L;
        Assert.assertEquals(expected, result);

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "The calculation caused an overflow: " + 10L + " - " + Long.MIN_VALUE;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        int expected = 15;
        Assert.assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "Multiplication overflows an int: " + Integer.MIN_VALUE + " * " + 2;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = 10L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        long expected = 30L;
        Assert.assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "Multiplication overflows a long: " + Long.MIN_VALUE + " * " + 2;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10L;
        long val2 = 3L;
        long result = FieldUtils.safeMultiply(val1, val2);
        long expected = 30L;
        Assert.assertEquals(expected, result);

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "Multiplication overflows a long: " + 1L + " * " + Long.MIN_VALUE;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            String expectedMessage = "Multiplication overflows a long: " + Long.MIN_VALUE + " * " + Long.MIN_VALUE;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = 100;
        int result = FieldUtils.safeToInt(value);
        int expected = 100;
        Assert.assertEquals(expected, result);

        value = Long.MAX_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
        } catch (ArithmeticException e) {
            String expectedMessage = "Value cannot fit in an int: " + Long.MAX_VALUE;
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected ArithmeticException");
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 2015, 1, 31);
        try {
            FieldUtils.verifyValueBounds(field, 2, 1, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 2";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(field, 31, 1, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 31";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(field, 30, 1, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 30";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            FieldUtils.verifyValueBounds(fieldType, 7, 1, 7);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 7";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 7);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 1";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        String fieldName = "dayOfWeek";
        try {
            FieldUtils.verifyValueBounds(fieldName, 8, 1, 7);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 8";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 1, 1, 7);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            String expectedMessage = "Value out of range: 1";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = -5;
        int wrapValue = 7;
        int minValue = 0;
        int maxValue = 6;
        int result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        int expected = 2;
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        wrapValue = 1;
        minValue = Integer.MAX_VALUE;
        maxValue = Integer.MIN_VALUE;
        try {
            result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "MIN > MAX";
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected IllegalArgumentException");

        value = Integer.MIN_VALUE;
        wrapValue = 1;
        minValue = 0;
        maxValue = 6;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        expected = 5;
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        wrapValue = 1;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        expected = 0;
        Assert.assertEquals(expected, result);

        value = Integer.MAX_VALUE;
        wrapValue = 1;
        minValue = 0;
        maxValue = 6;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        expected = 1;
        Assert.assertEquals(expected, result);

        value = Integer.MAX_VALUE;
        wrapValue = 1;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE;
        try {
            result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "MIN > MAX";
            Assert.assertEquals(expectedMessage, e.getMessage());
            return;
        }
        Assert.fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = "test";
        Object obj2 = "test";
        boolean result = FieldUtils.equals(obj1, obj2);
        boolean expected = true;
        Assert.assertEquals(expected, result);

        obj1 = null;
        result = FieldUtils.equals(obj1, obj2);
        expected = false;
        Assert.assertEquals(expected, result);

        obj1 = new Object();
        result = FieldUtils.equals(obj1, obj2);
        expected = false;
        Assert.assertEquals(expected, result);

        obj1 = "test";
        obj2 = 123;
        result = FieldUtils.equals(obj1, obj2);
        expected = false;
        Assert.assertEquals(expected, result);
    }
}