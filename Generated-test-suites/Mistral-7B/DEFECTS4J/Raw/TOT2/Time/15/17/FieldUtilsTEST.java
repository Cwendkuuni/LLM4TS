package org.joda.time.field.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int input = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSafeAdd() {
        int a = 3;
        int b = 4;
        int expected = 7;
        int result = FieldUtils.safeAdd(a, b);
        assertEquals(expected, result);

        a = -3;
        b = 4;
        expected = 1;
        result = FieldUtils.safeAdd(a, b);
        assertEquals(expected, result);

        a = 3;
        b = -4;
        expected = 1;
        result = FieldUtils.safeAdd(a, b);
        assertEquals(expected, result);

        a = Integer.MIN_VALUE;
        b = 1;
        try {
            FieldUtils.safeAdd(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long a = 10;
        long b = 5;
        long expected = 5;
        long result = FieldUtils.safeSubtract(a, b);
        assertEquals(expected, result);

        a = 10;
        b = 11;
        try {
            FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        a = Long.MIN_VALUE;
        b = 1;
        try {
            FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int a = 2;
        int b = 3;
        int expected = 6;
        int result = FieldUtils.safeMultiply(a, b);
        assertEquals(expected, result);

        a = Integer.MIN_VALUE;
        b = 2;
        try {
            FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        a = -2;
        b = 2;
        expected = 4;
        result = FieldUtils.safeMultiply(a, b);
        assertEquals(expected, result);
    }
}
//