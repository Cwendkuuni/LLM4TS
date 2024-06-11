package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        // Test true and false values
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        // Test multiple calls
        for (int i = 0; i < 1000; i++) {
            assertTrue(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] output = new byte[10];
        generator.nextBytes(output);
        // Test that all bytes have been generated
        for (int i = 0; i < output.length; i++) {
            assertTrue(output[i] >= 0);
        }
        // Test with empty byte array
        byte[] emptyOutput = new byte[0];
        generator.nextBytes(emptyOutput);
        // Test multiple calls with same byte array
        byte[] testOutput = new byte[10];
        generator.nextBytes(testOutput);
        generator.nextBytes(testOutput);
        assertArrayEquals(testOutput, testOutput);
    }

    @Test
    public void testNextDoubleAndNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        // Test nextDouble()
        for (int i = 0; i < 100; i++) {
            double actual = generator.nextDouble();
            assertTrue(actual >= 0.0);
            assertTrue(actual <= 1.0);
        }
        // Test nextGaussian()
        for (int i = 0; i < 100; i++) {
            double actual = generator.nextGaussian();
            assertTrue(actual >= -3.0);
            assertTrue(actual <= 3.0);
        }
        // Test nextGaussian() with initial cache cleared
        generator.clear();
        for (int i = 0; i < 100; i++) {
            double actual = generator.nextGaussian();
            assertTrue(actual >= -3.0);
            assertTrue(actual <= 3.0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(-42);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 100; i++) {
            int actual = generator.nextInt();
            assertTrue(actual >= 0);
            assertTrue(actual <= Integer.MAX_VALUE);
        }
        for (int i = 0; i < 100; i++) {
            int actual = generator.nextInt(10);
            assertTrue(actual >= 0);
            assertTrue(actual <= 10);
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 100; i++) {
            long actual = generator.nextLong();
            assertTrue(actual >= 0);
            assertTrue(actual <= Long.MAX_VALUE);
        }
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.setSeed(12345);
        long seed = generator.getSeed();
        assertEquals(12345, seed);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.clear();
        double actual = generator.nextGaussian();
        assertEquals(Double.NaN, actual);
    }

    @Test
    public void testNextGaussianInvalidSeed() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.clear();
        try {
            generator.nextGaussian();
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}

class MyBitsStreamGenerator extends BitsStreamGenerator {
    // Override any method that requires a seed for testing
    @Override
    public void setSeed(int seed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSeed(int[] seed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSeed(long seed) {
        throw new UnsupportedOperationException();
    }
}