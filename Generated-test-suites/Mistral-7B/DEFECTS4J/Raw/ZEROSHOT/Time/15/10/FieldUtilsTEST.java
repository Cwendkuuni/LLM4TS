package org.joda.time.test.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;

import java.util.Random;

public class FieldUtilsTest {

    @org.junit.BeforeClass
    public static void setUpBeforeClass() {
        // Set up any static variables or initialize any class-level resources
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() {
        // Release any static variables or resources
    }

    @org.junit.Before
    public void setUp() {
        // Initialize any instance variables
    }

    @org.junit.After
    public void tearDown() {
        // Release any instance variables or resources
    }

    @org.junit.Test
    public void testSafeNegate() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int negated = FieldUtils.safeNegate(i);
            int expectedNegated = -i;
            Assert.assertEquals("Negation of " + i, expectedNegated, negated);
        }
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        long maxLong = Long.MAX_VALUE;
        long minLong = Long.MIN_VALUE;

        for (int i = -100; i <= 100; i++) {
            for (int j = -100; j <= 100; j++) {
                long result = FieldUtils.safeAdd(i, j);
                long expected = i + j;
                Assert.assertEquals("Addition of " + i + " and " + j, expected, result);

                if (i > 0 && j > 0) {
                    Assert.assertTrue("Result is positive: " + i + " + " + j, result > 0);
                }

                if (i < 0 && j < 0) {
                    Assert.assertTrue("Result has opposite sign: " + i + " + " + j, result < 0);
                }

                if (i > 0 && j < 0) {
                    Assert.assertFalse("Result has same sign: " + i + " + " + j, result > 0);
                }

                if (i < 0 && j > 0) {
                    Assert.assertFalse("Result has opposite sign: " + i + " + " + j, result < 0);
                }
            }
        }

        try {
            FieldUtils.safeAdd(minLong, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeAdd(maxLong, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long maxLong = Long.MAX_VALUE;
        long minLong = Long.MIN_VALUE;

        for (int i = -100; i <= 100; i++) {
            for (int j = -100; j <= 100; j++) {
                long result = FieldUtils.safeSubtract(i, j);
                long expected = i - j;
                Assert.assertEquals("Subtraction of " + i + " from " + j, expected, result);
            }
        }

        try {
            FieldUtils.safeSubtract(minLong, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeSubtract(maxLong, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiply() {
        Random random = new Random();
        // Test random values
        for (int i = 0; i < 100; i++) {
            int a = random.nextInt(Integer.MAX_VALUE);
            int b = random.nextInt(Integer.MAX_VALUE);
            int c = FieldUtils.safeMultiply(a, b);
            int d = a * b;
            Assert.assertEquals("Multiplication of " + a + " * " + b, d, c);
        }

        // Test limits
        try {
            FieldUtils.safeMultiply(Integer.MIN_VALUE, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(1, Integer.MIN_VALUE);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(Integer.MAX_VALUE, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiply(1, Integer.MAX_VALUE);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            long a = random.nextLong();
            long b = random.nextLong();
            int c = FieldUtils.safeMultiplyToInt(a, b);
            long d = a * b;
            Assert.assertEquals("Multiplication of " + a + " * " + b, d, (long) c);
        }

        try {
            FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            FieldUtils.safeMultiplyToInt(1, Integer.MAX_VALUE);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 1);
        try {
            FieldUtils.verifyValueBounds(field, -1, 1, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 1);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 31);
            // No exception should be thrown
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected IllegalFieldValueException");
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        for (int i = -100; i <= 100; i++) {
            for (int wrapValue = -100; wrapValue <= 100; wrapValue++) {
                int minValue = 0;
                int maxValue = 100;
                int result = FieldUtils.getWrappedValue(i, wrapValue, minValue, maxValue);
                int expected = (i + wrapValue + minValue) % (maxValue - minValue + 1) + minValue;
                Assert.assertEquals("GetWrappedValue of " + i + " with wrapValue " + wrapValue, expected, result);
            }
        }

        for (int minValue = -100; minValue <= 100; minValue++) {
            for (int maxValue = minValue + 1; maxValue <= 100; maxValue++) {
                try {
                    FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, minValue, maxValue);
                    Assert.fail("Expected IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // Expected
                }

                try {
                    FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, minValue, maxValue);
                    Assert.fail("Expected IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // Expected
                }

                try {
                    FieldUtils.getWrappedValue(minValue, Integer.MIN_VALUE, minValue, maxValue);
                    Assert.fail("Expected IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // Expected
                }

                try {
                    FieldUtils.getWrappedValue(minValue, Integer.MAX_VALUE, minValue, maxValue);
                    Assert.fail("Expected IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // Expected
                }
            }
        }
    }

    @org.junit.Test
    public void testEquals() {
        Object a = new Object();
        Object b = new Object();
        Object c = a;

        Assert.assertTrue("a == a", FieldUtils.equals(a, a));
        Assert.assertFalse("a != b", FieldUtils.equals(a, b));
        Assert.assertTrue("a == c", FieldUtils.equals(a, c));
        Assert.assertFalse("a != null", FieldUtils.equals(null, a));
    }
}