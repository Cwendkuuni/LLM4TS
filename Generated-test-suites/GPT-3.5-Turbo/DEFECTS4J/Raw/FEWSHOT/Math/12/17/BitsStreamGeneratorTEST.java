package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    @Test
    public void testNext() {
        // Test cases with different number of bits
        int bits = 1;
        int result = BitsStreamGenerator.next(bits);
        assertTrue(result >= 0 && result <= 1);

        bits = 8;
        result = BitsStreamGenerator.next(bits);
        assertTrue(result >= 0 && result <= 255);

        bits = 16;
        result = BitsStreamGenerator.next(bits);
        assertTrue(result >= 0 && result <= 65535);

        bits = 32;
        result = BitsStreamGenerator.next(bits);
        assertTrue(result >= 0 && result <= Integer.MAX_VALUE);

        // Test case with negative number of bits
        bits = -1;
        try {
            result = BitsStreamGenerator.next(bits);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        byte[] bytes = new byte[10];

        generator.nextBytes(bytes);
        assertNotNull(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        float result = generator.nextFloat();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        double result = generator.nextGaussian();
        assertFalse(Double.isNaN(result));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        int n = 10;
        int result = generator.nextInt(n);
        assertTrue(result >= 0 && result < n);
    }

    @Test
    public void testNextIntWithZeroN() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        int n = 0;
        try {
            int result = generator.nextInt(n);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }

    @Test
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        int n = -10;
        try {
            int result = generator.nextInt(n);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();

        long result = generator.nextLong();
        assertNotNull(result);
    }

    private static class ConcreteBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
            // TODO implement setSeed()
        }

        @Override
        public void setSeed(int[] seed) {
            // TODO implement setSeed()
        }

        @Override
        public void setSeed(long seed) {
            // TODO implement setSeed()
        }

        @Override
        protected int next(int bits) {
            // TODO implement next()
            return 0;
        }
    }
}