package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MyBitsStreamGenerator();
    }

    /**
     * Test method for {@link BitsStreamGenerator#BitsStreamGenerator()}.
     */
    @Test
    public void testConstructor() {
        BitsStreamGenerator instance = new BitsStreamGenerator();
        assertNotNull(instance);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean expectedTrue = true;
        boolean expectedFalse = false;

        for (int i = 0; i < 10; i++) {
            boolean result = generator.nextBoolean();
            if (i % 2 == 0) {
                assertTrue(result);
                assertEquals(expectedTrue, result);
                expectedTrue = result;
            } else {
                assertFalse(result);
                assertEquals(expectedFalse, result);
                expectedFalse = result;
            }
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] result = new byte[expected.length];

        generator.nextBytes(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double expectedMin = -1.0;
        double expectedMax = 1.0;
        double result;

        // Test minimum value
        result = generator.nextDouble();
        assertTrue(result >= expectedMin);
        assertEquals(expectedMin, result, 0.0001);

        // Test maximum value
        result = generator.nextDouble();
        assertTrue(result <= expectedMax);
        assertEquals(expectedMax, result, 0.0001);

        // Test uniform distribution
        for (int i = 0; i < 10000; i++) {
            double sample = generator.nextDouble();
            assertTrue(sample >= expectedMin && sample <= expectedMax);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float expectedMin = -1.0f;
        float expectedMax = 1.0f;
        float result;

        // Test minimum value
        result = generator.nextFloat();
        assertTrue(result >= expectedMin);
        assertEquals(expectedMin, result, 0.0001f);

        // Test maximum value
        result = generator.nextFloat();
        assertTrue(result <= expectedMax);
        assertEquals(expectedMax, result, 0.0001f);

        // Test uniform distribution
        for (int i = 0; i < 10000; i++) {
            float sample = generator.nextFloat();
            assertTrue(sample >= expectedMin && sample <= expectedMax);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double expectedMean = 0.0;
        double expectedVariance = 1.0;
        double result;

        // Test mean
        for (int i = 0; i < 10000; i++) {
            result = generator.nextGaussian();
            assertTrue(result >= expectedMean - 1.0 && result <= expectedMean + 1.0);
        }

        // Test variance
        for (int i = 0; i < 10000; i++) {
            double sample1 = generator.nextGaussian();
            double sample2 = generator.nextGaussian();

            double diff = sample1 - sample2;
            assertTrue(diff >= -3.0 && diff <= 3.0);
        }

        // Test normal distribution
        for (int i = 0; i < 10000; i++) {
            double sample = generator.nextGaussian();
            double zScore = FastMath.abs(sample - expectedMean) / FastMath.sqrt(expectedVariance);
            assertTrue(zScore <= 1.96);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int expectedMin = Integer.MIN_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int result;

        // Test minimum value
        result = generator.nextInt();
        assertTrue(result >= expectedMin);
        assertEquals(expectedMin, result);

        // Test maximum value
        result = generator.nextInt();
        assertTrue(result <= expectedMax);
        assertEquals(expectedMax, result);

        // Test power-of-2 size
        for (int i = 0; i < 10; i++) {
            int size = 1 << i;
            int expected = (int) ((size * (long) next(31)) >> 31);
            result = generator.nextInt(size);
            assertEquals(expected, result);
        }

        // Test non-power-of-2 size
        int size = 17;
        int expected = next(31) % size;
        int rejectionCount = 0;

        while (result < expected || result > (expected + size - 1)) {
            result = generator.nextInt(size);
            rejectionCount++;
        }

        assertEquals(expected, result);
        assertEquals(size, size + rejectionCount);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;
        long result;

        // Test minimum value
        result = generator.nextLong();
        assertTrue(result >= expectedMin);
        assertEquals(expectedMin, result);

        // Test maximum value
        result = generator.nextLong();
        assertTrue(result <= expectedMax);
        assertEquals(expectedMax, result);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double[] expectedGaussians = new double[2];
        generator.nextGaussian();
        generator.nextGaussian();
        generator.clear();

        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        int seed = 12345;
        generator.setSeed(seed);

        int sample = generator.nextInt();

        Random expected = new Random(seed);
        int expectedValue = expected.nextInt();

        assertEquals(expectedValue, sample);
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        int[] seeds = { 1, 2, 3 };
        generator.setSeed(seeds);

        int sample = generator.nextInt();

        Random expected = new Random(seeds);
        int expectedValue = expected.nextInt();

        assertEquals(expectedValue, sample);
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        long seed = 0x1234567890abcdefL;
        generator.setSeed(seed);

        long sample = generator.nextLong();

        Random expected = new Random(seed);
        long expectedValue = expected.nextLong();

        assertEquals(expectedValue, sample);
    }
}