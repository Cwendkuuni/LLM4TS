package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test object. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("resultofmethodcallsignature")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                Random.setSeed(seed);
            }
        };
    }

    /**
     * Test method for {@link BitsStreamGenerator#BitsStreamGenerator()}.
     */
    @Test
    public void testConstructor() {
        // nothing to test here
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 12345;
        generator.setSeed(seed);
        final int expected = seed;
        assertEquals(expected, generator.nextInt());
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3, 4 };
        generator.setSeed(seed);
        final int expected = seed[0];
        assertEquals(expected, generator.nextInt());
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 1234567890123L;
        generator.setSeed(seed);
        final long expected = seed;
        assertEquals(expected, generator.nextLong());
    }

    /**
     * Test method for {@link BitsStreamGenerator#next(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNextIntAbstract() {
        generator.next(1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expected = true;
        assertTrue(generator.nextBoolean());
        final boolean actual = generator.nextBoolean();
        assertNotSame(expected, actual);

        final boolean expectedFalse = false;
        assertFalse(generator.nextBoolean());
        final boolean actualFalse = generator.nextBoolean();
        assertNotSame(expectedFalse, actualFalse);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = { 1, 2, 3, 4, 5, 6, 7, 8 };
        final byte[] bytes = new byte[8];
        generator.nextBytes(bytes);
        assertArrayEquals(expected, bytes);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        double actual = generator.nextDouble();
        assertTrue(actual >= expectedMin);
        assertTrue(actual <= expectedMax);

        actual = generator.nextDouble();
        assertTrue(actual >= expectedMin);
        assertTrue(actual <= expectedMax);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        float actual = generator.nextFloat();
        assertTrue(actual >= expectedMin);
        assertTrue(actual <= expectedMax);

        actual = generator.nextFloat();
        assertTrue(actual >= expectedMin);
        assertTrue(actual <= expectedMax);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        double actualMean = 0.0;
        double actualStdDev = 0.0;
        int count = 100000;
        for (int i = 0; i < count; i++) {
            double actual = generator.nextGaussian();
            actualMean += actual;
        }
        actualMean /= count;

        actualStdDev = 0.0;
        for (int i = 0; i < count; i++) {
            double actual = generator.nextGaussian();
            double diff = Math.pow(actual - expectedMean, 2.0);
            actualStdDev += diff;
        }
        actualStdDev /= count;

        assertEquals(expectedMean, actualMean, 0.01);
        assertEquals(expectedStdDev, actualStdDev, 0.01);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int max = 10;
        for (int i = 0; i < 100; i++) {
            final int expected = (int) (Math.random() * max);
            int actual = generator.nextInt();
            assertEquals(expected, actual);
        }

        final int min = Integer.MIN_VALUE;
        final int maxValue = Integer.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            int actual = generator.nextInt();
            if (actual < min) {
                min = actual;
            }
            if (actual > maxValue) {
                maxValue = actual;
            }
        }
        assertEquals(min, Integer.MIN_VALUE);
        assertEquals(maxValue, Integer.MAX_VALUE);

        final int n = 16;
        final int expected = 42;
        generator.nextInt(n);
        int actual = generator.nextInt();
        assertEquals(expected, actual);

        generator.nextInt(n);
        actual = generator.nextInt();
        assertEquals(expected, actual);

        generator.nextInt(n);
        actual = generator.nextInt();
        assertEquals(expected, actual);

        generator.nextInt(n);
        actual = generator.nextInt();
        assertNotSame(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long max = Long.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            final long expected = (long) (Math.random() * max);
            long actual = generator.nextLong();
            assertEquals(expected, actual);
        }

        final long min = Long.MIN_VALUE;
        long actualMin = Long.MIN_VALUE;
        long actualMax = Long.MIN_VALUE;
        for (int i = 0; i < 100; i++) {
            actual = generator.nextLong();
            if (actual < actualMin) {
                actualMin = actual;
            }
            if (actual > actualMax) {
                actualMax = actual;
            }
        }
        assertEquals(actualMin, Long.MIN_VALUE);
        assertEquals(actualMax, Long.MIN_VALUE);

        final long n = 20;
        final long expected = 42;
        generator.nextLong(n);
        long actual = generator.nextLong();
        assertEquals(expected, actual);

        generator.nextLong(n);
        actual = generator.nextLong();
        assertEquals(expected, actual);

        generator.nextLong(n);
        actual = generator.nextLong();
        assertEquals(expected, actual);

        generator.nextLong(n);
        actual = generator.nextLong();
        assertNotSame(expected, actual);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final int seed = 12345;
        generator.setSeed(seed);
        final int expected = seed;
        assertEquals(expected, generator.nextInt());

        generator.clear();
        final int actual = generator.nextInt();
        assertNotSame(expected, actual);

        generator.clear();
        final int actual2 = generator.nextInt();
        assertNotSame(expected, actual2);
    }

    /**
     * Test method for {@link BitsStreamGenerator#next(int)}.
     * @throws NotStrictlyPositiveException if the number of bits is not positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.next(123);
    }

}