package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Random;

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

    private Random random;
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        random = new Random();
        generator = new MersenneTwister();
        generator.setSeed(System.currentTimeMillis());
    }

    @Test
    public void testNextBoolean() {
        final boolean expected = true;
        assertEquals(expected, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextDouble() {
        final double expected = 0.5;
        assertEquals(expected, generator.nextDouble(), 0.00001);
    }

    @Test
    public void testNextFloat() {
        final float expected = 0.5f;
        assertEquals(expected, generator.nextFloat(), 0.00001f);
    }

    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedVar = 1.0;
        final int numTests = 1000;
        int count = 0;
        double sum = 0.0;
        for (int i = 0; i < numTests; i++) {
            final double value = generator.nextGaussian();
            sum += value * value;
            count++;
        }
        final double mean = sum / numTests;
        final double variance = (sum / numTests) - (expectedMean * expectedMean);
        assertEquals(expectedMean, mean, 0.00001);
        assertEquals(expectedVar, variance, 0.00001);
    }

    @Test
    public void testNextInt() {
        final int expected = 123;
        assertEquals(expected, generator.nextInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        final int expected = 256;
        assertEquals(expected, generator.nextInt(expected));
    }

    @Test
    public void testNextLong() {
        final long expected = 123456L;
        assertEquals(expected, generator.nextLong());
    }

    @Test
    public void testClear() {
        final double expectedMean = 0.0;
        final int numTests = 10;
        double sum = 0.0;
        for (int i = 0; i < numTests; i++) {
            generator.nextGaussian();
            sum += generator.nextGaussian();
        }
        generator.clear();
        final double mean = sum / numTests;
        assertEquals(expectedMean, mean, 0.00001);
    }

    @Test
    public void testSetSeed() {
        final long seed = System.currentTimeMillis();
        generator.setSeed(seed);
        final long expected = seed;
        assertEquals(expected, generator.nextLong());
        generator.setSeed(seed + 1);
        assertNotSame(expected, generator.nextLong());
    }

    @Test
    public void testNextBytesWithLargeArray() {
        final byte[] expected = new byte[1024];
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextBooleanManyTimes() {
        final int numTests = 10000;
        int countTrue = 0;
        for (int i = 0; i < numTests; i++) {
            final boolean value = generator.nextBoolean();
            if (value) {
                countTrue++;
            }
        }
        final double expectedProbability = 0.5;
        final double probability = (double) countTrue / numTests;
        assertEquals(expectedProbability, probability, 0.0001);
    }

}