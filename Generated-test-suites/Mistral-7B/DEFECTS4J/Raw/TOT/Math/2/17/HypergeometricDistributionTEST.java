package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import java.util.Arrays;

/**
 * Test cases for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Tests construction with valid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testConstruction() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructionWithNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = -3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, 5);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithZeroPopulationSize() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithZeroPopulationSizeAndNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = -3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithZeroNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 0;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithZeroNumberOfSuccessesAndNegativePopulationSize() throws Exception {
        final int populationSize = -5;
        final int numberOfSuccesses = 0;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionWithNumberOfSuccessesLargerThanPopulationSize() throws Exception {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with invalid parameters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionWithSampleSizeLargerThanPopulationSize() throws Exception {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Tests construction with valid parameters and verification of getters.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testConstructionAndGetters() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Tests cumulative probability.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testCumulativeProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        int x, expected;
        for (x = 0; x <= dist.getSupportUpperBound(); x++) {
            expected = (int) Math.floor(dist.cumulativeProbability(x) * 10000);
            assertEquals(expected, dist.cumulativeProbability(x) * 10000);
        }
    }

    /**
     * Tests probability.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        int x;
        for (x = 0; x <= dist.getSupportUpperBound(); x++) {
            assertEquals(dist.probability(x), dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x), 0.0001);
        }
    }

    /**
     * Tests support bounds.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testSupportBounds() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(dist.getSupportLowerBound(), 3);
        assertEquals(dist.getSupportUpperBound(), 5);
    }

    /**
     * Tests isSupportConnected.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testIsSupportConnected() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Tests generation of random samples.
     *
     * @throws Exception if any error occurs
     */
    @org.junit.Test
    public void testRandomSamples() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 10000;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);

        int[] observed = new int[sampleSize];
        final RandomGenerator rng = dist.getRandomGenerator();
        for (int i = 0; i < sampleSize; i++) {
            observed[i] = dist.sample();
        }

        final int[] expected = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            dist.nextInt(); // discard
            expected[i] = dist.sample();
        }

        Arrays.sort(observed);
        Arrays.sort(expected);
        for (int i = 0; i < sampleSize; i++) {
            assertEquals(expected[i], observed[i]);
        }
    }
}