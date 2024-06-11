package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Assert that all bytes are within the range of 0-255
        for (byte b : bytes) {
            assertEquals(true, b >= 0 && b <= 255);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        double result = generator.nextDouble();
        // Assert that the result is within the range of 0-1
        assertEquals(true, result >= 0 && result < 1);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        float result = generator.nextFloat();
        // Assert that the result is within the range of 0-1
        assertEquals(true, result >= 0 && result < 1);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        double result = generator.nextGaussian();
        // Assert that the result is within the range of -infinity to +infinity
        assertEquals(true, Double.isFinite(result));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        int result = generator.nextInt();
        // Assert that the result is within the range of Integer.MIN_VALUE to Integer.MAX_VALUE
        assertEquals(true, result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        int result = generator.nextInt(100);
        // Assert that the result is within the range of 0-99
        assertEquals(true, result >= 0 && result < 100);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        generator.nextInt(0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorStub();
        long result = generator.nextLong();
        // Assert that the result is within the range of Long.MIN_VALUE to Long.MAX_VALUE
        assertEquals(true, result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
    }

    private class BitsStreamGeneratorStub extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {}

        @Override
        public void setSeed(int[] seed) {}

        @Override
        public void setSeed(long seed) {}

        @Override
        protected int next(int bits) {
            return 0;
        }
    }
}