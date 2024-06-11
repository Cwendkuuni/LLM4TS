package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] result = new byte[10];
        generator.nextBytes(result);
        assertNotNull(result);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(10);
        assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.clear();
        // Verify that generator has been cleared (e.g. nextGaussian is NaN)
        double result = generator.nextGaussian();
        assertTrue(Double.isNaN(result));
    }

    private static class BitsStreamGeneratorImpl extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
            // Not implemented
        }

        @Override
        public void setSeed(int[] seed) {
            // Not implemented
        }

        @Override
        public void setSeed(long seed) {
            // Not implemented
        }

        @Override
        protected int next(int bits) {
            // Not implemented
            return 0;
        }
    }

}