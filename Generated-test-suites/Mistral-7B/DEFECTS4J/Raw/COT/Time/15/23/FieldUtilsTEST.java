package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

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
        int val1 = 3;
        int val2 = 4;
        int expected = 7;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        long sum = (long) val1 + (long) val2;
        if (sum < Integer.MIN_VALUE) {
            try {
                FieldUtils.safeAdd(val1, val2);
                fail("Expected ArithmeticException");
            } catch (ArithmeticException e) {
                // Expected
            }
        } else {
            int result1 = (int) sum;
            assertEquals(expected, result1);
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        sum = (long) val1 + (long) val2;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 3L;
        long val2 = 4L;
        long expected = 7L;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        long sum = val1 + val2;
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            try {
                FieldUtils.safeAdd(val1, val2);
                fail("Expected ArithmeticException");
            } catch (ArithmeticException e) {
                // Expected
            }
        } else {
            long result1 = sum;
            assertEquals(expected, result1);
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        sum = val1 + val2;
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
        long val2 = 3L;
        long expected = 7L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 3L;
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
        int expected = 15;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        long total = (long) val1 * (long) val2;
        if (total < Integer.MIN_VALUE || total > Integer.MAX_VALUE) {
            try {
                FieldUtils.safeMultiply(val1, val2);
                fail("Expected ArithmeticException");
            } catch (ArithmeticException e) {
                // Expected
            }
        } else {
            int result1 = (int) total;
            assertEquals(expected, result1);
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 10L;
        int val2 = 3;
        long expected = 30L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10L;
        long val2 = 3L;
        long expected = 30L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        long total = val1 * val2;
        if (total / val2 != val1 || val1 == Long.MIN_VALUE && val2 == -1 || val2 == Long.MIN_VALUE && val1 == -1) {
            try {
                FieldUtils.safeMultiply(val1, val2);
                fail("Expected ArithmeticException");
            } catch (ArithmeticException e) {
                // Expected
            }
        } else {
            long result1 = total;
            assertEquals(expected, result1);
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10L;
        int expected = 10;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);

        value = Long.MIN_VALUE + 1;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10L;
        long val2 = 3L;
        int expected = 30;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1);
        int value = 7;
        FieldUtils.verifyValueBounds(field, value, 1, 7);

        value = 1;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 8;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 0;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 7;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 7);

        value = 1;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 8;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 0;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";
        int value = 7;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 7);

        value = 1;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 8;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 0;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 7;
        int expected = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = -2;
        int wrapRange = maxValue - minValue + 1;
        int remByRange = (-value) % wrapRange;
        if (remByRange == 0) {
            int result1 = 0;
            assertEquals(expected, result1);
        } else {
            int result1 = wrapRange - remByRange;
            assertEquals(expected, result1);
        }

        value = -wrapRange - 1;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        String object1 = "test";
        String object2 = "test";
        assertTrue(FieldUtils.equals(object1, object2));

        object1 = null;
        assertFalse(FieldUtils.equals(object1, object2));

        object2 = null;
        assertFalse(FieldUtils.equals(object1, object2));

        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}