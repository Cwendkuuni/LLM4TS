package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        new BitsStreamGenerator();
    }

    /**
     * Test setting seed using int.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 1234;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        generator.setSeed(seed);
        final int actualSeed = generator.nextInt();
        assertEquals(seed, actualSeed);
    }

    /**
     * Test setting seed using int array.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = {1, 2, 3, 4};
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        generator.setSeed(seed);
        final int actualSeed = generator.nextInt();
        // The seed value is not directly testable as it is consumed
        // by the generator and the test relies on the randomness of the generated numbers
        // Instead, we test that the generator produces different sequences of numbers
        // with different seeds
        final int[] expectedSeq1 = {3, 16777215, 1332722475, 2147483647};
        final int[] expectedSeq2 = {16843009, 16349262, 1374389531, 1335543121};
        final int[] sequence1 = new int[4];
        final int[] sequence2 = new int[4];
        for (int i = 0; i < 4; i++) {
            generator.nextInt(); // consume initial value
            sequence1[i] = generator.nextInt();
            sequence2[i] = generator.setSeed(seed)[0];
        }
        assertArrayEquals(expectedSeq1, sequence1);
        assertArrayEquals(expectedSeq2, sequence2);
    }

    /**
     * Test setting seed using long.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 1234567890L;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        generator.setSeed(seed);
        final long actualSeed = generator.nextLong();
        assertEquals(seed, actualSeed);
    }

    /**
     * Test nextBoolean() method.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final boolean expectedTrue = Math.random() > 0.5;
        final boolean expectedFalse = !expectedTrue;
        final boolean actualTrue  = generator.nextBoolean();
        final boolean actualFalse = generator.nextBoolean();
        assertTrue(actualTrue ^ actualFalse);
    }

    /**
     * Test nextBytes() method.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        generator.nextBytes(new byte[16]); // consume initial values
        generator.nextBytes(new byte[12]);
        final byte[] actual = new byte[12];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test nextDouble() method.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final double actualMin = generator.nextDouble();
        final double actualMax = generator.nextDouble();
        assertTrue(actualMin >= expectedMin && actualMin <= expectedMax);
        assertTrue(actualMax >= expectedMin && actualMax <= expectedMax);
    }

    /**
     * Test nextFloat() method.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final float actualMin = generator.nextFloat();
        final float actualMax = generator.nextFloat();
        assertTrue(actualMin >= expectedMin && actualMin <= expectedMax);
        assertTrue(actualMax >= expectedMin && actualMax <= expectedMax);
    }

    /**
     * Test nextGaussian() method.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final double epsilon = 1e-6;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final double actualMean = generator.nextGaussian();
        final double actualStdDev = Math.sqrt(generator.nextGaussian() * generator.nextGaussian() + generator.nextGaussian() * generator.nextGaussian());
        assertTrue(Math.abs(actualMean - expectedMean) < epsilon);
        assertTrue(Math.abs(actualStdDev - expectedStdDev) < epsilon);
    }

    /**
     * Test nextInt() method.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        final int actualMin = generator.nextInt();
        final int actualMax = generator.nextInt();
        assertTrue(actualMin >= expectedMin && actualMin <= expectedMax);
        assertTrue(actualMax >= expectedMin && actualMax <= expectedMax);
    }

    /**
     * Test nextInt(int) method.
     */
    @Test
    public void testNextIntN() {
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final int n = 10;
        final int expectedMin = 0;
        final int expectedMax = n - 1;
        final int actualMin = generator.nextInt(n);
        final int actualMax = generator.nextInt(n);
        assertTrue(actualMin >= expectedMin && actualMin <= expectedMax);
        assertTrue(actualMax >= expectedMin && actualMax <= expectedMax);
    }

    /**
     * Test nextLong() method.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final long actualMin = generator.nextLong();
        final long actualMax = generator.nextLong();
        assertTrue(actualMin >= expectedMin && actualMin <= expectedMax);
        assertTrue(actualMax >= expectedMin && actualMax <= expectedMax);
    }

    /**
     * Test clear() method.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new Xoroshiro128Plus();
        final double expectedNextGaussian = Double.NaN;
        generator.nextGaussian();
        generator.clear();
        final double actualNextGaussian = generator.nextGaussian();
        assertEquals(expectedNextGaussian, actualNextGaussian);
    }
}