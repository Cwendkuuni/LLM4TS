package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        assertFalse(generator.hasNextGaussian());
    }

    /**
     * Tests {@link #nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final boolean expectedTrue = true;
        final boolean expectedFalse = false;
        for (int i = 0; i < 10; i++) {
            final boolean actual = generator.nextBoolean();
            if (i % 2 == 0) {
                assertTrue(actual == expectedTrue);
            } else {
                assertFalse(actual == expectedTrue);
                assertTrue(actual == expectedFalse);
            }
        }
    }

    /**
     * Tests {@link #nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextBytes(new byte[12]); // consume initial state
        generator.nextBytes(expected);
        final byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests {@link #nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final double expectedMin = -1.0;
        final double expectedMax = 1.0;
        for (int i = 0; i < 10; i++) {
            final double actual = generator.nextDouble();
            assertTrue(actual >= expectedMin);
            assertTrue(actual <= expectedMax);
        }
    }

    /**
     * Tests {@link #nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final float expectedMin = -1.0f;
        final float expectedMax = 1.0f;
        for (int i = 0; i < 10; i++) {
            final float actual = generator.nextFloat();
            assertTrue(actual >= expectedMin);
            assertTrue(actual <= expectedMax);
        }
    }

    /**
     * Tests {@link #nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        for (int i = 0; i < 10; i++) {
            final double actualMean = generator.nextGaussian();
            final double actualStdDev = Math.sqrt(Math.pow(actualMean, 2) + Math.pow(generator.nextGaussian(), 2));
            assertTrue(Math.abs(actualMean - expectedMean) < 0.001);
            assertTrue(Math.abs(actualStdDev - expectedStdDev) < 0.01);
        }
    }

    /**
     * Tests {@link #nextInt()}.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            final int actual = generator.nextInt();
            final int expectedMin = Integer.MIN_VALUE;
            final int expectedMax = Integer.MAX_VALUE;
            assertTrue(actual >= expectedMin);
            assertTrue(actual <= expectedMax);
        }
    }

    /**
     * Tests {@link #nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        for (int limit = 1; limit <= 10; limit++) {
            for (int i = 0; i < 10; i++) {
                final int actual = generator.nextInt(limit);
                final int expectedMin = 0;
                final int expectedMax = limit - 1;
                assertTrue(actual >= expectedMin);
                assertTrue(actual <= expectedMax);
            }
        }
    }

    /**
     * Tests {@link #nextLong()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextLongWithLimit() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextLong(10);
    }

    /**
     * Tests {@link #clear()}.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final boolean hasNextGaussianBefore = generator.hasNextGaussian();
        generator.clear();
        assertFalse(generator.hasNextGaussian());
        generator.nextGaussian();
        assertTrue(generator.hasNextGaussian());
    }

    /**
     * Tests exception handling.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeLimit() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextInt(-1);
    }

    /**
     * Tests exception handling.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedWithNegativeValue() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(-1);
    }

}