package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** Test random number generator. */
    private static final Random TEST_RANDOM = new Random();

    /** Test Mersenne Twister implementation of {@link BitsStreamGenerator}. */
    @Test
    public void testMersenneTwister() {
        final int seed = 12345;
        final MersenneTwister generator = new MersenneTwister(seed);
        generator.nextGaussian(); // clear the cache
        BitsStreamGenerator bsg = new MersenneTwister(seed);
        final double[] expected = new double[10];
        for (int i = 0; i < expected.length; ++i) {
            expected[i] = generator.nextGaussian();
        }
        for (int i = 0; i < 10; ++i) {
            assertEquals(expected[i], bsg.nextGaussian(), 1e-8);
        }
    }

    /** Test nextBoolean() method. */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new MersenneTwister();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /** Test nextBytes() method. */
    @Test
    public void testNextBytes() {
        final byte[] expected = { 1, 2, 3, 4 };
        final byte[] actual = new byte[4];
        final BitsStreamGenerator generator = new MersenneTwister();
        generator.nextBytes(expected);
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /** Test nextDouble() method. */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new MersenneTwister();
        assertEquals(0.0, generator.nextDouble(), 1e-8);
        assertEquals(0.5, generator.nextDouble(), 1e-8);
        assertEquals(1.0, generator.nextDouble(), 1e-8);
        final double expected = 0.123456789;
        assertEquals(expected, generator.nextDouble(), 1e-8);
    }

    /** Test nextFloat() method. */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new MersenneTwister();
        assertEquals(0.0f, generator.nextFloat(), 1e-8f);
        assertEquals(0.5f, generator.nextFloat(), 1e-8f);
        assertEquals(1.0f, generator.nextFloat(), 1e-8f);
        final float expected = (float) 0.123456789;
        assertEquals(expected, generator.nextFloat(), 1e-8f);
    }

    /** Test nextGaussian() method. */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new MersenneTwister();
        assertEquals(0.0, generator.nextGaussian(), 1e-8);
        final double mu = 1.0;
        final double sigma = 2.0;
        final double expectedMean = mu;
        final double expectedVariance = sigma * sigma;
        for (int i = 0; i < 10000; ++i) {
            final double value = generator.nextGaussian();
            assertEquals(expectedMean, value, sigma, 1e-8);
            assertEquals(expectedVariance, value * value, 1e-8);
        }
    }

    /** Test nextInt() method with positive argument. */
    @Test
    public void testNextIntPositive() {
        final BitsStreamGenerator generator = new MersenneTwister();
        final int max = 10;
        for (int i = 0; i < max; ++i) {
            final int value = generator.nextInt(max);
            assertTrue(value >= 0 && value < max);
        }
    }

    /** Test nextInt() method with zero argument. */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZero() {
        final BitsStreamGenerator generator = new MersenneTwister();
        generator.nextInt(0);
    }

    /** Test nextInt() method with negative argument. */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        final BitsStreamGenerator generator = new MersenneTwister();
        generator.nextInt(-1);
    }

    /** Test nextLong() method. */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new MersenneTwister();
        assertEquals(0L, generator.nextLong());
        assertEquals(-1L, generator.nextLong());
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());
    }

    /** Test clear() method. */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new MersenneTwister();
        final double[] gaussianCache = new double[10];
        for (int i = 0; i < gaussianCache.length; ++i) {
            gaussianCache[i] = generator.nextGaussian();
        }
        generator.clear();
        for (int i = 0; i < 10; ++i) {
            assertNotEquals(gaussianCache[i], generator.nextGaussian());
        }
    }
}