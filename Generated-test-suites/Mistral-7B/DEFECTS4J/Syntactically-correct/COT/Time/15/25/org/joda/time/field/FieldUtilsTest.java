package org.joda.time.field; // Make sure the test file is in the same package as the FieldUtils class

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for FieldUtils methods.
 */
public class FieldUtilsTest {

    

    /**
     * Test int safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int[] testCases = {Integer.MIN_VALUE, 0, 5, 10, 20};
        for (int input : testCases) {
            int expected = FieldUtils.safeNegate(input);
            int result = FieldUtils.safeNegate(input);
            assertEquals("Expected negative of " + input + " to be " + expected, expected, result);
        }
    }

    /**
     * Test int safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int[] testCases1 = {Integer.MIN_VALUE, 0, 5, 10, 20};
        int[] testCases2 = {-5, 5, 10, 20, Integer.MAX_VALUE};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                int input1 = testCases1[i];
                int input2 = testCases2[j];
                int expected = input1 + input2;
                int result = FieldUtils.safeAdd(input1, input2);
                assertEquals("Expected sum of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeAdd(Integer.MIN_VALUE, 1);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test long safeAdd method.
     */
    @Test
    public void testSafeAddLong() {
        long[] testCases1 = {Long.MIN_VALUE, 0L, 5L, 10L, 20L};
        long[] testCases2 = {-5L, 5L, 10L, 20L, Long.MAX_VALUE};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                long input1 = testCases1[i];
                long input2 = testCases2[j];
                long expected = input1 + input2;
                long result = FieldUtils.safeAdd(input1, input2);
                assertEquals("Expected sum of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeAdd(Long.MIN_VALUE, 1L);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test int safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        int[] testCases1 = {-5, 0, 5, 10, 20};
        int[] testCases2 = {5, 10, 20, Integer.MAX_VALUE, Integer.MIN_VALUE};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                int input1 = testCases1[i];
                int input2 = testCases2[j];
                int expected = input1 - input2;
                int result = FieldUtils.safeSubtract(input1, input2);
                assertEquals("Expected difference of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeSubtract(Integer.MIN_VALUE, 1);
            fail("Expected ArithmeticException for underflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test long safeSubtract method.
     */
    @Test
    public void testSafeSubtractLong() {
        long[] testCases1 = {-5L, 0L, 5L, 10L, 20L};
        long[] testCases2 = {5L, 10L, 20L, Long.MAX_VALUE, Long.MIN_VALUE};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                long input1 = testCases1[i];
                long input2 = testCases2[j];
                long expected = input1 - input2;
                long result = FieldUtils.safeSubtract(input1, input2);
                assertEquals("Expected difference of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1L);
            fail
                ("Expected ArithmeticException for underflow: " + Long.MIN_VALUE + " - 1");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test int safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int[] testCases1 = {-5, 0, 5, 10, 20};
        int[] testCases2 = {-5, 0, 5, 10, 20};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                int input1 = testCases1[i];
                int input2 = testCases2[j];
                int expected = input1 * input2;
                int result = FieldUtils.safeMultiply(input1, input2);
                assertEquals("Expected product of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeMultiply(-5, 100);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test int safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long[] testCases1 = {-5L, 0L, 5L, 10L, 20L};
        long[] testCases2 = {-5L, 0L, 5L, 10L, 20L};
        for (int i = 0; i < testCases1.length; i++) {
            for (int j = 0; j < testCases2.length; j++) {
                long input1 = testCases1[i];
                long input2 = testCases2[j];
                long expected = input1 * input2;
                int result = FieldUtils.safeMultiplyToInt((int) input1, (int) input2);
                assertEquals("Expected product of " + input1 + " and " + input2 + " to be " + expected, expected, result);
            }
        }

        // Test for overflow
        try {
            FieldUtils.safeMultiplyToInt(-5, 100);
            fail("Expected ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test int safeMultiply method with long input.
     */
    @Test
    public void testSafeMultiplyWithLongInput() {
        int testCase1 = 5;
        long testCase2 = 5L;
        int expected = 25;
        int result = FieldUtils.safeMultiply(testCase1, testCase2);
        assertEquals("Expected product of 5 and 5 to be 25", expected, result);
    }

    /**
     * Test int safeMultiply method with int input.
     */
    @Test
    public void testSafeMultiplyWithIntInput() {
        int testCase1 = 5;
        int testCase2 = 5;
        int expected = 25;
        int result = FieldUtils.safeMultiply(testCase1, testCase2);
        assertEquals("Expected product of 5 and 5 to be 25", expected, result);
    }

    /**
     * Test int verifyValueBounds method with DateTimeField.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31, "test");
        int testValue1 = 1;
        int testValue2 = 31;
        int testValue3 = 32;

        try {
            FieldUtils.verifyValueBounds(field, testValue1, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue1);
        }

        try {
            FieldUtils.verifyValueBounds(field, testValue2, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue2);
        }

        try {
            FieldUtils.verifyValueBounds(field, testValue3, 1, 31);
            fail("Expected IllegalFieldValueException for invalid value: " + testValue3);
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    /**
     * Test int verifyValueBounds method with DateTimeFieldType.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int testValue1 = 1;
        int testValue2 = 31;
        int testValue3 = 32;

        try {
            FieldUtils.verifyValueBounds(fieldType, testValue1, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue1);
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, testValue2, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue2);
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, testValue3, 1, 31);
            fail("Expected IllegalFieldValueException for invalid value: " + testValue3);
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    /**
     * Test int verifyValueBounds method with String.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";
        int testValue1 = 1;
        int testValue2 = 31;
        int testValue3 = 32;

        try {
            FieldUtils.verifyValueBounds(fieldName, testValue1, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue1);
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, testValue2, 1, 31);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception for valid value: " + testValue2);
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, testValue3, 1, 31);
            fail("Expected IllegalFieldValueException for invalid value: " + testValue3);
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    /**
     * Test int getWrappedValue method.
     */
    @Test
    public void testGetWrappedValue() {
        int[] testCases = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 5, 10, 15, Integer.MAX_VALUE - 1};
        int minValue = 5;
        int maxValue = 10;

        for (int testCase : testCases) {
            int result = FieldUtils.getWrappedValue(testCase, minValue, maxValue);
            int expected = (testCase % (maxValue - minValue + 1)) + minValue;
            assertEquals("Expected wrapped value of " + testCase + " to be " + expected, expected, result);
        }
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Object testObjects[] = {new Object(), null, new FieldUtils()};
        for (Object object1 : testObjects) {
            for (Object object2 : testObjects) {
                boolean expected = (object1 == object2);
                boolean result = FieldUtils.equals(object1, object2);
                assertEquals("Expected equals(" + object1 + ", " + object2 + ") to be " + expected, expected, result);
            }
        }
    }

    
}