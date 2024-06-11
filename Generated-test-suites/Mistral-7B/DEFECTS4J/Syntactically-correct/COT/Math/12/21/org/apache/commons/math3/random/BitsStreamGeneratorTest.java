package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator} class
 */
public class BitsStreamGeneratorTest {

    private static final int N = 10;
    private static final int M = 5;

    @Test
    public void testSetSeedInt() throws NotStrictlyPositiveException {
        BitsStreamGenerator generator = new MersenneTwister();
        generator.setSeed(12345);
        long expectedSeed = 12345;
        assertEquals(expectedSeed, generator.nextLong());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() throws NotStrictlyPositiveException {
        BitsStreamGenerator generator = new MersenneTwister();
        generator.setSeed(-12345);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwister();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwister();
        byte[] bytes = new byte[M * N];
        generator.nextBytes(bytes);
        byte[] expected = new byte[M * N];
        for (int i = 0; i < M * N; i++) {
            expected[i] = (byte) (i % 256);
        }
        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwister();
        double expectedMin = -1.5;
        double expectedMax = 1.5;
        double min = generator.nextDouble();
        double max = generator.nextDouble();
        assertTrue(min < expectedMin);
        assertTrue(max > expectedMax);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwister();
        float expectedMin = -1.5f;
        float expectedMax = 1.5f;
        float min = generator.nextFloat();
        float max = generator.nextFloat();
        assertTrue(min < expectedMin);
        assertTrue(max > expectedMax);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MersenneTwister();
        double mean = 0.0;
        double stddev = 1.0;
        double expectedMin = -3;
        double expectedMax = 3;
        double min = generator.nextGaussian();
        double max = generator.nextGaussian();
        double meanValue = (min + max) / 2.0;
        double stddevValue = Math.sqrt(Math.pow(min, 2) + Math.pow(max, 2)) / 2.0;
        assertTrue(meanValue > mean - stddev);
        assertTrue(meanValue < mean + stddev);
        assertTrue(stddevValue > 0);
        assertTrue(min < expectedMin);
        assertTrue(max > expectedMax);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwister();
        int max = 10;
        int min = 5;
        int expected = 7;
        int value = generator.nextInt(max - min + 1) + min;
        assertTrue(value >= min);
        assertTrue(value <= max);
        assertEquals(expected, value);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeValue() {
        BitsStreamGenerator generator = new MersenneTwister();
        int min = -10;
        int max = 10;
        generator.nextInt(max - min + 1 + min);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwister();
        long max = 10L;
        long min = 5L;
        long expected = 7L;
        long value = generator.nextLong() & 0xffffffffL;
        assertTrue(value >= min);
        assertTrue(value <= max);
        assertEquals(expected, value);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwister();
        double[] gaussians = new double[N];
        for (int i = 0; i < N; i++) {
            gaussians[i] = generator.nextGaussian();
            generator.clear();
        }
        for (int i = 1; i < N; i++) {
            double previous = gaussians[i - 1];
            double current = generator.nextGaussian();
            assertTrue(current != previous);
        }
    }

}