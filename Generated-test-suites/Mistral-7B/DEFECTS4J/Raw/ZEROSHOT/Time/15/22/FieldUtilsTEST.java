package org.joda.time.test.field;

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

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7, result);

        val1 = 10;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch(ArithmeticException e) {
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
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 10;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(30, result);

        val1 = Long.MIN_VALUE;
        val2 = -1;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(30, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }

        val1 = 1;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(30, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch(ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch(IllegalFieldValueException e) {
            fail();
        }

        value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail();
        } catch(IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
        } catch(IllegalFieldValueException e) {
            fail();
        }

        value = 32;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            fail();
        } catch(IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";
        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
        } catch(IllegalFieldValueException e) {
            fail();
        }

        value = 32;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail();
        } catch(IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 1;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, result);

        value = 0;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);

        value = 6;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, result);

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail();
        } catch(IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = new Object();
        assertTrue(FieldUtils.equals(obj1, obj1));
    }
}