package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test the constructor of the BitsStreamGenerator class.
     */
    @Test
    public void testConstructor() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        Assert.assertFalse(generator.isSeedSet());
        Assert.assertNull(generator.nextGaussian);
    }

    /**
     * Test the setSeed method for integer seed.
     */
    @Test
    public void testSetSeedInt() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(123456789);
        Assert.assertTrue(generator.isSeedSet());
        Assert.assertEquals(123456789, generator.getSeed());
        // Test the generation of random numbers after setting the seed
        final int intValue = generator.nextInt();
        Assert.assertNotEquals(123456789, intValue);
        final double doubleValue = generator.nextDouble();
        Assert.assertNotEquals(1.0, doubleValue);
    }

    /**
     * Test the setSeed method for long seed.
     */
    @Test
    public void testSetSeedLong() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(1234567890123L);
        Assert.assertTrue(generator.isSeedSet());
        Assert.assertEquals(1234567890123L, generator.getSeed());
        // Test the generation of random numbers after setting the seed
        final int intValue = generator.nextInt();
        Assert.assertNotEquals(1234567890123L, intValue);
        final double doubleValue = generator.nextDouble();
        Assert.assertNotEquals(1.0, doubleValue);
    }

    /**
     * Test the setSeed method for array seed.
     */
    @Test
    public void testSetSeedArray() {
        final int[] seedArray = { 1, 2, 3, 4 };
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(seedArray);
        Assert.assertTrue(generator.isSeedSet());
        final int[] seedCopy = new int[seedArray.length];
        System.arraycopy(seedArray, 0, seedCopy, 0, seedArray.length);
        Assert.assertArrayEquals(seedCopy, generator.getSeedArray());
        // Test the generation of random numbers after setting the seed
        final int intValue = generator.nextInt();
        Assert.assertNotEquals(seedArray[0], intValue);
        final double doubleValue = generator.nextDouble();
        Assert.assertNotEquals(1.0, doubleValue);
    }

    /**
     * Test the nextBoolean method.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        final int numTrials = 10000;
        int countTrue = 0;
        for (int i = 0; i < numTrials; i++) {
            countTrue += (int) generator.nextBoolean();
        }
        final double expectedPercentageTrue = 0.5 * numTrials;
        Assert.assertEquals(expectedPercentageTrue, countTrue, 1);
    }

    /**
     * Test the nextBytes method with byte array of a specific size.
     */
    @Test
    public void testNextBytesByteArray() {
        final byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextBytes(expected);
        final byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test the nextBytes method with byte array of a larger size.
     */
    @Test
    public void testNextBytesByteArrayLarger() {
        final byte[] expected = new byte[256];
        final BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextBytes(expected);
        final byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test the nextDouble method.
     */
    @Test
    public void testNextDouble() {
        final int numTrials = 10000;
        final double minBound = 0.0;
        final double maxBound = 1.0;
        double sum = 0.0;
        for (int i = 0; i < numTrials; i++) {
            sum += generator.nextDouble();
        }
        final double expectedAverage = (minBound + maxBound) / 2.0;
        Assert.assertEquals(expectedAverage, sum / numTrials, 0.001);
    }

    /**
     * Test the nextFloat method.
     */
    @Test
    public void testNextFloat() {
        final int numTrials = 10000;
        final float minBound = 0.0f;
        final float maxBound = 1.0f;
        float sum = 0.0f;
        for (int i = 0; i < numTrials; i++) {
            sum += generator.nextFloat();
        }
        final float expectedAverage = (minBound + maxBound) / 2.0f;
        Assert.assertEquals(expectedAverage, sum / numTrials, 0.001f);
    }

    /**
     * Test the nextGaussian method with default parameters.
     */
    @Test
    public void testNextGaussian() {
        final int numTrials = 10000;
        final double mean = 0.0;
        final double stdDev = 1.0;
        double sumSquares = 0.0;
        for (int i = 0; i < numTrials; i++) {
            sumSquares += Math.pow(generator.nextGaussian() - mean, 2.0);
        }
        final double expectedVariance = stdDev * stdDev;
        final double expectedStandardDeviation = Math.sqrt(expectedVariance);
        Assert.assertEquals(expectedVariance, sumSquares / numTrials, 0.001);
        Assert.assertEquals(expectedStandardDeviation, Math.sqrt(sumSquares / numTrials), 0.001);
    }

    /**
     * Test the nextGaussian method with custom mean and standard deviation.
     */
    @Test
    public void testNextGaussianCustom() {
        final int numTrials = 10000;
        final double mean = 2.5;
        final double stdDev = 0.5;
        double sumSquares = 0.0;
        for (int i = 0; i < numTrials; i++) {
            sumSquares += Math.pow(generator.nextGaussian(mean, stdDev) - mean, 2.0);
        }
        final double expectedVariance = stdDev * stdDev;
        final double expectedStandardDeviation = Math.sqrt(expectedVariance);
        Assert.assertEquals(expectedVariance, sumSquares / numTrials, 0.001);
        Assert.assertEquals(expectedStandardDeviation, Math.sqrt(sumSquares / numTrials), 0.001);
    }

    /**
     * Test the nextInt method.
     */
    @Test
    public void testNextInt() {
        final int numTrials = 10000;
        final int minBound = 0;
        final int maxBound = 10;
        int sum = 0;
        for (int i = 0; i < numTrials; i++) {
            sum += generator.nextInt(maxBound + 1);
        }
        final int expectedAverage = (minBound + maxBound + 1) / 2;
        Assert.assertEquals(expectedAverage, sum / numTrials, 1);
    }

    /**
     * Test the nextInt method with negative bound.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeBound() {
        final int minBound = -1;
        generator.nextInt(minBound);
    }

    /**
     * Test the nextInt method with zero bound.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroBound() {
        generator.nextInt(0);
    }

    /**
     * Test the nextInt method with power-of-2 bound.
     */
    @Test
    public void testNextIntPowerOf2Bound() {
        final int numTrials = 10000;
        final int minBound = 16;
        final int maxBound = 16;
        int sum = 0;
        for (int i = 0; i < numTrials; i++) {
            sum += generator.nextInt(maxBound);
        }
        final long bitsInOneMaxBound = (long) Long.bitCount(maxBound - 1) << 1;
        final long maxBits = sum * bitsInOneMaxBound;
        Assert.assertEquals(maxBound, Long.numberOfTrailingZeros(maxBits));
    }

    /**
     * Test the nextLong method.
     */
    @Test
    public void testNextLong() {
        final int numTrials = 10000;
        final long minBound = 0L;
        final long maxBound = 10L;
        long sum = 0L;
        for (int i = 0; i < numTrials; i++) {
            sum += generator.nextLong();
        }
        final long expectedAverage = (minBound + maxBound) / 2;
        Assert.assertEquals(expectedAverage, sum / numTrials, 1);
    }

    /**
     * Test the clear method.
     */
    @Test
    public void testClear() {
        final double oldGaussian = generator.nextGaussian();
        generator.clear();
        Assert.assertFalse(generator.isSeedSet());
        Assert.assertNull(generator.nextGaussian);
        Assert.assertThrows(IllegalStateException.class, () -> generator.nextGaussian());
    }

}