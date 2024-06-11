package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The instance under test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests {@link #setSeed(int)}.
     * @throws NotStrictlyPositiveException if seed is not strictly positive
     */
    @Test
    public void testSetSeedInt() throws NotStrictlyPositiveException {
        final int seed = 12345;
        generator.setSeed(seed);
        final int expectedSeed = seed;
        assertEquals(expectedSeed, generator.nextInt());
        final int[] intSeed = {seed};
        generator.setSeed(intSeed);
        assertArrayEquals(intSeed, generator.nextInts(intSeed.length));
        final long longSeed = (long) seed;
        generator.setSeed(longSeed);
        assertEquals(expectedSeed, generator.nextInt());
        assertArrayEquals(intSeed, generator.nextInts(intSeed.length));
        assertEquals(longSeed, generator.nextLong());
    }

    /**
     * Tests {@link #setSeed(int[])}.
     * @throws NotStrictlyPositiveException if seed length is not strictly positive
     */
    @Test
    public void testSetSeedIntArray() throws NotStrictlyPositiveException {
        final int[] seeds = {1, 2, 3, 4};
        generator.setSeed(seeds);
        final int expectedSeed1 = seeds[0];
        assertEquals(expectedSeed1, generator.nextInt());
        final int expectedSeed2 = seeds[1];
        assertEquals(expectedSeed2, generator.nextInt());
        final int expectedSeed3 = seeds[2];
        assertEquals(expectedSeed3, generator.nextInt());
        assertArrayEquals(seeds, generator.nextInts(seeds.length));
        final long expectedSeed4 = seeds[3];
        assertEquals(expectedSeed4, generator.nextLong());
        assertArrayEquals(seeds, generator.nextInts(seeds.length));
    }

    /**
     * Tests {@link #setSeed(long)}.
     * @throws NotStrictlyPositiveException if seed is not strictly positive
     */
    @Test
    public void testSetSeedLong() throws NotStrictlyPositiveException {
        final long seed = 987654321L;
        generator.setSeed(seed);
        final long expectedSeed = seed;
        assertEquals(expectedSeed, generator.nextLong());
        assertArrayEquals(new long[] {expectedSeed}, generator.nextLongs(1));
    }

    /**
     * Tests {@link #nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expectedTrue = true;
        final boolean expectedFalse = false;
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Tests {@link #nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (final byte b : bytes) {
            assertTrue(b >= 0 && b <= 0x7f);
        }
    }

    /**
     * Tests {@link #nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        final double actual = generator.nextDouble();
        assertTrue(expectedMin <= actual && actual <= expectedMax);
    }

    /**
     * Tests {@link #nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        final float actual = generator.nextFloat();
        assertTrue(expectedMin <= actual && actual <= expectedMax);
    }

    /**
     * Tests {@link #nextGaussian()}.
     * Note that {@link #clear()} should be called between calls to
     * {@link #nextGaussian()} to ensure the correct behavior.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final double actual = generator.nextGaussian();
        final double actualMean = generator.nextGaussian();
        final double actualStdDev = Math.sqrt(Math.pow(actualMean - expectedMean, 2)
                + Math.pow(generator.nextGaussian() - expectedMean, 2)) / 2.0;
        assertTrue(expectedMean - 3 * expectedStdDev <= actualMean && actualMean <= expectedMean
                + 3 * expectedStdDev);
        assertTrue(expectedStdDev - 0.5 <= actualStdDev && actualStdDev <= expectedStdDev + 0.5);
        generator.clear();
    }

    /**
     * Tests {@link #nextInt()}.
     * @throws NotStrictlyPositiveException if n is not strictly positive
     */
    @Test
    public void testNextInt() throws NotStrictlyPositiveException {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        final int n = 12;
        final int actual = generator.nextInt(n);
        assertTrue(expectedMin <= actual && actual <= expectedMax);
        final int[] ints = new int[n];
        for (int i = 0; i < n; i++) {
            ints[i] = generator.nextInt(n);
        }
        assertArrayEquals(ints, generator.nextInts(n));
    }

    /**
     * Tests {@link #nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        final long actual = generator.nextLong();
        assertTrue(expectedMin <= actual && actual <= expectedMax);
        final long[] longs = new long[1];
        generator.nextLongs(longs);
        assertEquals(actual, longs[0]);
    }

}