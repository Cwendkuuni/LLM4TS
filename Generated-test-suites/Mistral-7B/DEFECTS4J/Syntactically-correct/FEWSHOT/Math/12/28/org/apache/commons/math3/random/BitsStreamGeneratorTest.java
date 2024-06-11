package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new MersenneTwister();
    }

    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextDouble() {
        final double expectedMean = 0.5;
        final double expectedStdev = 1.0;
        final int sampleSize = 10000;
        double sum = 0.0;
        double sumSquares = 0.0;

        for (int i = 0; i < sampleSize; i++) {
            sum += generator.nextDouble();
            sumSquares += generator.nextDouble() * generator.nextDouble();
        }

        double mean = sum / sampleSize;
        double stdev = FastMath.sqrt(sumSquares / sampleSize - mean * mean);

        assertEquals(expectedMean, mean, 0.001);
        assertEquals(expectedStdev, stdev, 0.001);
    }

    @Test
    public void testNextFloat() {
        final float expectedMean = 0.5f;
        final float expectedStdev = 1.0f;
        final int sampleSize = 10000;
        float sum = 0.0f;
        float sumSquares = 0.0f;

        for (int i = 0; i < sampleSize; i++) {
            sum += generator.nextFloat();
            sumSquares += generator.nextFloat() * generator.nextFloat();
        }

        float mean = sum / sampleSize;
        float stdev = (float) Math.sqrt(sumSquares / sampleSize - mean * mean);

        assertEquals(expectedMean, mean, 0.001f);
        assertEquals(expectedStdev, stdev, 0.001f);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextInt() {
        final int expectedMin = 0;
        final int expectedMax = 10;
        final int expectedValue = 5;

        for (int i = 0; i < 100; i++) {
            int value = generator.nextInt(expectedMax);
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
        }

        int value = generator.nextInt(expectedValue);
        assertEquals(expectedValue, value);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZero() {
        generator.nextInt(0);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        final int bits = 32;
        final int expected = (int) ((1L << bits) * (long) next(31) >> 31);
        int result = generator.nextInt(bits);

        assertEquals(expected, result);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        final int bits = 8;
        int result;
        do {
            result = generator.nextInt(bits);
        } while (result % bits != 0);
        assertEquals(bits, result);
    }

    @Test
    public void testNextLong() {
        final long expectedMin = 0;
        final long expectedMax = Long.MAX_VALUE;
        final long expectedValue = Long.MIN_VALUE;

        for (int i = 0; i < 10; i++) {
            long value = generator.nextLong();
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
        }

        long value = generator.nextLong();
        assertEquals(expectedValue, value);
    }

    @Test
    public void testClear() {
        double gaussian1 = generator.nextGaussian();
        double gaussian2 = generator.nextGaussian();
        assertNotSame(gaussian1, gaussian2);

        generator.clear();

        double gaussian3 = generator.nextGaussian();
        double gaussian4 = generator.nextGaussian();
        assertEquals(gaussian1, gaussian3);
        assertEquals(gaussian1, gaussian4);
    }
}