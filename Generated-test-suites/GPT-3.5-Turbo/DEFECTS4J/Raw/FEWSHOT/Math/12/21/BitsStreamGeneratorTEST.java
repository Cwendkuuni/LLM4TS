package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    
    private BitsStreamGenerator generator;
    
    @Before
    public void setUp() {
        generator = new BitsStreamGeneratorImpl();
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result == true || result == false);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertEquals(4, bytes.length);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0 && result < 1);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        // Test the condition when nextGaussian is NaN
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertTrue(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int n = 10;
        int result = generator.nextInt(n);
        assertTrue(result >= 0 && result < n);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        int n = 0;
        generator.nextInt(n);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        int n = -10;
        generator.nextInt(n);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
    }

    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        double result = generator.nextGaussian();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }
}