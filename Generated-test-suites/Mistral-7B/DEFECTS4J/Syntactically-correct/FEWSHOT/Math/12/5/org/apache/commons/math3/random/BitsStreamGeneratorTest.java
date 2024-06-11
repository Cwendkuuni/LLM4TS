package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    // Assuming you have a concrete implementation of BitsStreamGenerator, let's call it MyBitsStreamGenerator
    @Test
    public void testNextBoolean() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xFF);
        }
    }

    @Test
    public void testNextDouble() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        double expected = Math.random();
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 0.001);

        double[] doubles = new double[10];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = generator.nextDouble();
        }
        for (int i = 0; i < doubles.length; i++) {
            assertTrue(doubles[i] >= 0.0 && doubles[i] <= 1.0);
        }
    }

    @Test
    public void testNextFloat() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        float expected = (float) Math.random();
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.001f);

        float[] floats = new float[10];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = generator.nextFloat();
        }
        for (int i = 0; i < floats.length; i++) {
            assertTrue(floats[i] >= 0.0f && floats[i] <= 1.0f);
        }
    }

    @Test
    public void testNextGaussian() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        double mean = 0.0;
        double stdDev = 1.0;
        double expected = new GaussianDistribution(mean, stdDev).sample();
        double actual = generator.nextGaussian();
        assertEquals(expected, actual, 0.1); // Adjust the tolerance as needed

        double[] gaussians = new double[10];
        for (int i = 0; i < gaussians.length; i++) {
            gaussians[i] = generator.nextGaussian();
        }
        for (int i = 0; i < gaussians.length; i++) {
            assertTrue(gaussians[i] >= mean - 3 * stdDev && gaussians[i] <= mean + 3 * stdDev);
        }
    }

    @Test
    public void testNextInt() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        int expected = (int) (Math.random() * 10);
        int actual = generator.nextInt(10);
        assertEquals(expected, actual);

        int[] ints = new int[10];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = generator.nextInt(10);
        }
        for (int i = 0; i < ints.length; i++) {
            assertTrue(ints[i] >= 0 && ints[i] <= 10);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        long expected = Math.random() * (1L << 31);
        long actual = generator.nextLong();
        assertEquals(expected, actual);

        long[] longs = new long[10];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = generator.nextLong();
        }
        for (int i = 0; i < longs.length; i++) {
            assertTrue(longs[i] >= 0 && longs[i] <= Long.MAX_VALUE);
        }
    }

    @Test
    public void testClear() {
        MyBitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextGaussian();
        // Perform some tests using nextGaussian() to verify that the cache is cleared
        // ...
        generator.clear();
        double actual = generator.nextGaussian();
        // Perform some tests using nextGaussian() to verify that the cache is empty again
        // ...
    }

}