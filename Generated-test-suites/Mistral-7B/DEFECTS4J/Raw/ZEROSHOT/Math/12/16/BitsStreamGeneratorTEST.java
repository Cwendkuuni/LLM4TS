package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up test environment.
     */
    @SuppressWarnings("PMD.AssumeNonNull")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expectedTrue = true;
        final boolean expectedFalse = false;
        for (int i = 0; i < 10; i++) {
            final boolean result = generator.nextBoolean();
            assertTrue("Unexpected boolean value: " + result, result == expectedTrue || result == expectedFalse);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] { 3, 42, -123, 11, 0 };
        final byte[] result    = new byte[5];
        generator.nextBytes(result);
        assertArrayEquals("Unexpected byte array: ", expected, result);
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        for (int i = 0; i < 10; i++) {
            final double result = generator.nextDouble();
            assertTrue("Unexpected double value: " + result, result >= expectedMin && result <= expectedMax);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        for (int i = 0; i < 10; i++) {
            final float result = generator.nextFloat();
            assertTrue("Unexpected float value: " + result, result >= expectedMin && result <= expectedMax);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStddev = 1.0;
        for (int i = 0; i < 10; i++) {
            final double result = generator.nextGaussian();
            assertEquals("Unexpected gaussian value: " + result, expectedMean, result.getMean(), 0.001);
            assertEquals("Unexpected gaussian value: " + result, expectedStddev, FastMath.sqrt(result * result + FastMath.EPSILON), 0.001);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            final int result = generator.nextInt();
            assertTrue("Unexpected integer value: " + result, result >= expectedMin && result <= expectedMax);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntN() {
        final int expected = 123;
        for (int i = 0; i < 10; i++) {
            final int result = generator.nextInt(expected);
            assertTrue("Unexpected integer value: " + result, result % expected == 0);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            final long result = generator.nextLong();
            assertTrue("Unexpected long value: " + result, result >= expectedMin && result <= expectedMax);
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse("Gaussian value should have been cleared", Double.isNaN(generator.nextGaussian()));
    }

    /**
     * Tests method {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 123;
        generator.nextGaussian();
        generator.setSeed(seed);
        final int seed2 = generator.next(32);
        assertEquals("Unexpected seed value: " + seed2, seed, seed2);
    }

    /**
     * Tests method {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 123, 456, 789 };
        generator.nextGaussian();
        generator.setSeed(seed);
        final int[] seed2 = new int[seed.length];
        for (int i = 0; i < seed.length; i++) {
            final int result = generator.next(32);
            assertEquals("Unexpected seed value: " + result, seed[i], result);
            seed2[i] = result;
        }
        for (int i = 0; i < seed.length; i++) {
            assertEquals("Unexpected seed value: " + seed[i], seed2[i], generator.next(32));
        }
    }

    /**
     * Tests method {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 123L;
        generator.nextGaussian();
        generator.setSeed(seed);
        final long seed2 = ((long) generator.next(32)) << 32;
        final int  seed3  = generator.next(32);
        final long seed4  = ((long) seed3) << 24;
        final long seed5  = ((long) seed3) << 16;
        final long seed6  = ((long) seed3) << 8;
        final long seed7  = seed3;
        assertEquals("Unexpected seed value: " + seed2, seed, seed2 | (seed3 << 32));
        assertEquals("Unexpected seed value: " + seed4, seed >> 8, seed4);
        assertEquals("Unexpected seed value: " + seed5, seed >> 16, seed5);
        assertEquals("Unexpected seed value: " + seed6, seed >> 24, seed6);
        assertEquals("Unexpected seed value: " + seed7, seed3, seed7);
    }

}