package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        boolean result = generator.nextBoolean();
        assertTrue(result == true || result == false);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        double result = generator.nextDouble();

        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        float result = generator.nextFloat();

        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        double result = generator.nextGaussian();

        // Gaussian distribution has no specific bounds, so we can't test for exact values, only some properties
        assertTrue(Double.isFinite(result));

        // Testing generation of two values
        generator.clear();
        double result1 = generator.nextGaussian();
        double result2 = generator.nextGaussian();

        assertTrue(Double.isFinite(result1));
        assertTrue(Double.isFinite(result2));
        assertTrue(result1 != result2);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        int result = generator.nextInt();

        // Int range is quite large, so testing only if it's a valid integer
        assertTrue(Integer.MIN_VALUE <= result && result <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();

        // Testing n = 0
        try {
            generator.nextInt(0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }

        // Testing n > 0
        for (int i = 1; i <= 100; i++) {
            int result = generator.nextInt(i);
            assertTrue(result >= 0 && result < i);
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MockBitsStreamGenerator();
        long result = generator.nextLong();

        // Long range is quite large, so testing only if it's a valid long
        assertTrue(Long.MIN_VALUE <= result && result <= Long.MAX_VALUE);
    }

    private class MockBitsStreamGenerator extends BitsStreamGenerator {

        private int next = 0;

        @Override
        public void setSeed(int seed) {
            // Not implemented for this mock
        }

        @Override
        public void setSeed(int[] seed) {
            // Not implemented for this mock
        }

        @Override
        public void setSeed(long seed) {
            // Not implemented for this mock
        }

        @Override
        protected int next(int bits) {
            return next++;
        }

    }

}