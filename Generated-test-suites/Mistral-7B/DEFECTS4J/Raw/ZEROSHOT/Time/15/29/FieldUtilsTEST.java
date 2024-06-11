package org.joda.time.field.test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int negated = FieldUtils.safeNegate(i);
            assertEquals(-i, negated);
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    /**
     * Test for safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                int sum = FieldUtils.safeAdd(i, j);
                int expected = i + j;
                assertEquals(expected, sum);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    /**
     * Test for safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                long diff = FieldUtils.safeSubtract(i, j);
                long expected = i - j;
                assertEquals(expected, diff);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    /**
     * Test for safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                int product = FieldUtils.safeMultiply(i, j);
                int expected = i * j;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    /**
     * Test for safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            for (long j = Long.MIN_VALUE; j <= Long.MAX_VALUE; j++) {
                int product = FieldUtils.safeMultiplyToInt((int) i, (int) j);
                long expected = i * j;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    /**
     * Test for verifyValueBounds method.
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "Test");

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 13, 1, 12));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 1, 12));

        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 25, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, -1, 0, 23));

        String fieldName = "testField";

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 13, 1, 12));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, 1, 12));
    }

    /**
     * Test for getWrappedValue method.
     */
    @Test
    public void testGetWrappedValue() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int minValue = Integer.MIN_VALUE; minValue <= Integer.MAX_VALUE; minValue++) {
                for (int maxValue = minValue + 1; maxValue <= Integer.MAX_VALUE; maxValue++) {
                    int wrappedValue = FieldUtils.getWrappedValue(i, 1, minValue, maxValue);
                    assertEquals(minValue + ((i - minValue) % (maxValue - minValue + 1)), wrappedValue);
                }
            }
        }
    }
}