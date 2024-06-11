package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        int populationSize = 20;
        int numberOfSuccesses = 5;
        int sampleSize = 10;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 10;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        int populationSize = 20;
        int numberOfSuccesses = -5;
        int sampleSize = 10;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        int populationSize = 20;
        int numberOfSuccesses = 5;
        int sampleSize = -10;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (number of successes > population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesTooLarge() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (sample size > population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeTooLarge() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test probabilities.
     */
    @Test
    public void testProbabilities() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] probabilities = new int[sampleSize + 1];
        for (int i = 0; i <= sampleSize; i++) {
            probabilities[i] = (int) Math.round(dist.probability(i));
        }

        int[] expectedProbabilities = {1, 3, 5, 3, 1};
        assertArrayEquals(expectedProbabilities, probabilities);
    }

    /**
     * Test cumulative probabilities.
     */
    @Test
    public void testCumulativeProbabilities() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double[] cumulativeProbabilities = new double[sampleSize + 1];
        for (int i = 0; i <= sampleSize; i++) {
            cumulativeProbabilities[i] = dist.cumulativeProbability(i);
        }

        double[] expectedCumulativeProbabilities = {0.0, 0.05991, 0.14286, 0.26172, 0.38639, 0.49388, 0.57418, 0.63799, 0.68633, 0.72314, 0.74862, 0.76713, 0.78136, 0.79142, 0.8, 0.80821, 0.81883, 0.82409, 0.82773, 0.82971, 0.83073, 0.83113, 0.83098, 0.82938, 0.82644, 0.82234, 0.81713, 0.81093, 0.80489, 0.79898, 0.79333, 0.78801, 0.78317, 0.77892, 0.77532, 0.77254, 0.77064, 0.76944, 0.76894, 0.76898, 0.76946, 0.77016, 0.77121, 0.77255, 0.77414, 0.77593, 0.77775, 0.77954, 0.78121, 0.78272, 0.78403, 0.78509, 0.78581, 0.78622, 0.78637, 0.78632, 0.78607, 0.78555, 0.78482, 0.78386, 0.78264, 0.78128, 0.77983, 0.77835, 0.77688, 0.77546, 0.77412, 0.77291, 0.77181, 0.77092, 0.77031, 0.76994, 0.76982, 0.76993, 0.77031, 0.77101, 0.77202, 0.77327, 0.77462, 0.77603, 0.77743, 0.77875, 0.78001, 0.78114, 0.78208, 0.78278, 0.78319, 0.78332, 0.78318, 0.78279, 0.78214, 0.78127, 0.78021, 0.77907, 0.77797, 0.7769};

        assertArrayEquals(expectedCumulativeProbabilities, cumulativeProbabilities, 0.001);
    }

    /**
     * Test mean.
     */
    @Test
    public void testMean() {
        int populationSize = 100;
        int numberOfSuccesses = 15;
        int sampleSize = 20;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = dist.getNumericalMean();
        double expectedMean = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals(expectedMean, mean, 0.001);
    }

    /**
     * Test variance.
     */
    @Test
    public void testVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 15;
        int sampleSize = 20;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = dist.getNumericalVariance();
        double expectedVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedVariance, variance, 0.001);
    }

    /**
     * Test support bounds.
     */
    @Test
    public void testSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();

        int expectedLowerBound = 5;
        int expectedUpperBound = 5;

        assertEquals(expectedLowerBound, lowerBound);
        assertEquals(expectedUpperBound, upperBound);
    }

    /**
     * Test support connectedness.
     */
    @Test
    public void testSupportConnectedness() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean connected = dist.isSupportConnected();
        assertTrue(connected);
    }
}