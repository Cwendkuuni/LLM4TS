package org.joda.time.field.util; // replace with the actual package name if necessary

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test int safeNegate method
        int val1 = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(val1);
        assertEquals(expected, result);

        val1 = 0;
        expected = 0;
        result = FieldUtils.safeNegate(val1);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(val1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test long safeNegate method
        long val2 = 10L;
        long expectedLong = -10L;
        long resultLong = FieldUtils.safeNegate((int) val2);
        assertEquals(expectedLong, resultLong);

        val2 = 0L;
        expectedLong = 0L;
        resultLong = FieldUtils.safeNegate(val2);
        assertEquals(expectedLong, resultLong);

        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeNegate(val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        // Test int safeAdd method
        int val1 = 3;
        int val2 = 4;
        int expected = 7;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = 100;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test long safeAdd method
        long val3 = 15L;
        long val4 = 7L;
        long expectedLong = 22L;
        long resultLong = FieldUtils.safeAdd(val3, val4);
        assertEquals(expectedLong, resultLong);

        val3 = Long.MIN_VALUE;
        val4 = 1L;
        try {
            FieldUtils.safeAdd(val3, val4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val3 = 10L;
        val4 = Long.MAX_VALUE;
        try {
            FieldUtils.safeAdd(val3, val4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        // Test long safeSubtract method
        long val1 = 20L;
        long val2 = 10L;
        long expected = 10L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5L;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        // Test int safeMultiply method
        int val1 = 3;
        int val2 = 4;
        int expected = 12;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test long safeMultiply method
        long val3 = 15L;
        long val4 = 7L;
        long expectedLong = 105L;
        long resultLong = FieldUtils.safeMultiply(val3, val4);
        assertEquals(expectedLong, resultLong);

        val3 = Long.MIN_VALUE;
        val4 = 2L;
        try {
            FieldUtils.safeMultiply(val3, val4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val3 = 10L;
        val4 = Long.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val3, val4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        // Test int safeMultiplyToInt method
        long val1 = 5L;
        long val2 = 3L;
        int expected = 15;
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

        val1 = 5L;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        // Test int verifyValueBounds method using DateTimeField
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test int verifyValueBounds method using DateTimeFieldType
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        value = 8;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test int verifyValueBounds method using String
        String fieldName = "dayOfWeek";
        value = 10;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test int verifyValueBounds method with valid value
        DateTimeField fieldValid = new DateTimeField(DateTimeFieldType.hourOfDay(), "hourOfDay");
        int validValue = 12;
        FieldUtils.verifyValueBounds(fieldValid, validValue, 0, 23);
    }

    @Test
    public void testGetWrappedValue() {
        // Test getWrappedValue method using int
        int value = 5;
        int minValue = 1;
        int maxValue = 5;
        int expected = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 0;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = -1;
        expected = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = maxValue + 1;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getWrappedValue method using long
        long longValue = 5L;
        long minValueLong = 1L;
        long maxValueLong = 5L;
        long expectedLong = 5L;
        long resultLong = FieldUtils.getWrappedValue((int) longValue, (int) minValueLong, (int) maxValueLong);
        assertEquals(expectedLong, resultLong);

        longValue = 0L;
        expectedLong = 1L;
        resultLong = FieldUtils.getWrappedValue((int) longValue, (int) minValueLong, (int) maxValueLong);
        assertEquals(expectedLong, resultLong);

        longValue = -1L;
        expectedLong = 5L;
        resultLong = FieldUtils.getWrappedValue((int) longValue, (int) minValueLong, (int) maxValueLong);
        assertEquals(expectedLong, resultLong);

        minValueLong = maxValueLong + 1;
        try {
            FieldUtils.getWrappedValue((int) longValue, (int) minValueLong, (int) maxValueLong);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        // Test equals method using different classes
        Object obj1 = new Object();
        Object obj2 = new String("Test");
        assertFalse(FieldUtils.equals(obj1, obj2));

        // Test equals method using null objects
        Object obj3 = null;
        Object obj4 = null;
        assertTrue(FieldUtils.equals(obj3, obj4));

        // Test equals method using same object
        Object obj5 = new Object();
        assertTrue(FieldUtils.equals(obj5, obj5));

        // Test equals method using same primitive type and value
        int int1 = 5;
        int int2 = 5;
        assertTrue(FieldUtils.equals(int1, int2));

        // Test equals method using different primitive types and values
        int int3 = 5;
        long long1 = 5;
        assertFalse(FieldUtils.equals(int3, long1));

        // Test equals method using different primitive types and values but same wrapper type
        int int4 = 5;
        Long long2 = 5L;
        assertTrue(FieldUtils.equals(int4, long2));
    }
}