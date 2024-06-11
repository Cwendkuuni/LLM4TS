package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntMaxValue() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        int maxValue = Integer.MAX_VALUE;
        int result = generator.nextInt(maxValue);
        assertEquals(maxValue, result);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        int powerOfTwo = 16;
        int result = generator.nextInt(powerOfTwo);
        assertTrue(result % powerOfTwo == 0);
    }

    @Test
    public void testNextIntNonPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        int nonPowerOfTwo = 13;
        int result = generator.nextInt(nonPowerOfTwo);
        assertTrue(result % nonPowerOfTwo > 0);
    }

    @Test
    public void testNextBytesArray() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        generator.nextBytes(expected);
        byte[] result = new byte[expected.length];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        double expectedMin = -1.0;
        double expectedMax = 1.0;
        double result = generator.nextDouble();
        assertTrue(expectedMin < result && result < expectedMax);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        float expectedMin = -1.0f;
        float expectedMax = 1.0f;
        float result = generator.nextFloat();
        assertTrue(expectedMin < result && result < expectedMax);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double expectedMean = 0.0;
        double expectedStandardDeviation = 1.0;
        double result = generator.nextGaussian();
        assertTrue(expectedMean - 3 * expectedStandardDeviation < result &&
                result < expectedMean + 3 * expectedStandardDeviation);
    }

    @Test
    public void testNextGaussianSameValues() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double expectedMean = 0.0;
        double expectedStandardDeviation = 1.0;
        double result1 = generator.nextGaussian();
        double result2 = generator.nextGaussian();
        assertEquals(result1, result2, 0.001);
    }

    @Test
    public void testNextGaussianDifferentValues() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        double expectedMean = 0.0;
        double expectedStandardDeviation = 1.0;
        double result1 = generator.nextGaussian();
        double result2 = generator.nextGaussian();
        assertNotSame(result1, result2);
    }

    @Test
    public void testSetSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        long seed = 123456789;
        generator.setSeed(seed);
        long newSeed = generator.getSeed();
        assertEquals(seed, newSeed);
    }

    @Test
    public void testSetSeedNull() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        generator.setSeed(null);
        assertNull(generator.getSeed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSeedInvalidArgument() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        generator.setSeed(-1);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastGenerator();
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;
        long result = generator.nextLong();
        assertTrue(expectedMin < result && result < expectedMax);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new NormalDistributionGenerator();
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }
}