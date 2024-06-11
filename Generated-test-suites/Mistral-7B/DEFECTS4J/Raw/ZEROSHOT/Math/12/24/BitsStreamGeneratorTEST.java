package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwister();
    }

    @Test
    public void testNextBoolean() {
        final boolean expected = true;
        final boolean actual = generator.nextBoolean();
        assertTrue("Next boolean should be true.", actual == expected);
    }

    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] { 1, 2, 3, 4 };
        final byte[] actual = new byte[4];
        generator.nextBytes(actual);
        assertArrayEquals("Next bytes should match.", expected, actual);
    }

    @Test
    public void testNextDouble() {
        final double expected = 0.25;
        final double actual = generator.nextDouble();
        assertEquals("Next double should be close to expected.", expected, actual, 0.001);
    }

    @Test
    public void testNextFloat() {
        final float expected = 0.5f;
        final float actual = generator.nextFloat();
        assertEquals("Next float should be close to expected.", expected, actual, 0.001f);
    }

    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final int numTrials = 1000;
        final double sumX = 0.0;
        final double sumX2 = 0.0;
        for (int i = 0; i < numTrials; i++) {
            final double x = generator.nextGaussian();
            sumX += x;
            sumX2 += x * x;
        }
        final double mean = sumX / numTrials;
        final double variance = (sumX2 / numTrials) - mean * mean;
        assertEquals("Mean should be close to expected.", expectedMean, mean, 0.01);
        assertEquals("Variance should be close to expected.", expectedStdDev * expectedStdDev, variance, 0.01);
    }

    @Test
    public void testNextInt() {
        final int expected = 5;
        final int actual = generator.nextInt();
        assertEquals("Next int should be close to expected.", expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntMaxValue() {
        final int expected = Integer.MAX_VALUE;
        final int actual = generator.nextInt();
        assertEquals("Next int should be close to max value.", expected, actual);
    }

    @Test
    public void testNextLong() {
        final long expected = Long.MAX_VALUE;
        final long actual = generator.nextLong();
        assertEquals("Next long should be close to max value.", expected, actual);
    }

    @Test
    public void testClear() {
        final boolean gaussianSet = generator.nextGaussian() != Double.NaN;
        generator.clear();
        assertFalse("Next gaussian should not be set after clear.", generator.nextGaussian() != Double.NaN);
    }
}