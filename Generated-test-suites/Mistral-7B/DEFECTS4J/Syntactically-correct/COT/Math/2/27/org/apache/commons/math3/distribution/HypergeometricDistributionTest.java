package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     * @throws Exception if initialization fails.
     */
    @Test
    public void testValidConstructor() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test constructor with invalid parameters - population size <= 0.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructorPopulationSizeNotNull() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid parameters - population size <= 0.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructorPopulationSizeZero() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid parameters - numberOfSuccesses < 0.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructorNumberOfSuccessesNegative() throws Exception {
        final int populationSize = 3;
        final int numberOfSuccesses = -2;
        final int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid parameters - sampleSize <= 0.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructorSampleSizeNegative() throws Exception {
        final int populationSize = 3;
        final int numberOfSuccesses = 2;
        final int sampleSize = -2;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid parameters - numberOfSuccesses > populationSize.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructorNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid parameters - sampleSize > populationSize.
     * @throws Exception if initialization fails.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructorSampleSizeGreaterThanPopulationSize() throws Exception {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 6;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double expected = 0.0;
            if (x >= numberOfSuccesses) {
                expected = ((double) populationSize - sampleSize + numberOfSuccesses - x) / ((double) populationSize);
            }
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 1e-10);
        }
    }

    /**
     * Test method probability.
     */
    @Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double actual = distribution.probability(x);
            double expected = 0.0;
            if (x >= numberOfSuccesses) {
                double p = ((double) sampleSize) / ((double) populationSize);
                double q = ((double) populationSize - sampleSize) / ((double) populationSize);
                expected = SaddlePointExpansion.logBinomialProbability(x,
                        numberOfSuccesses, p, q) + SaddlePointExpansion.logBinomialProbability(sampleSize - x,
                                populationSize - numberOfSuccesses, p, q) - SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
                expected = Math.exp(expected);
            }
            assertEquals(expected, actual, 1e-10);
        }
    }

    /**
     * Test method upperCumulativeProbability.
     */
    @Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double expected = 1.0;
            if (x < numberOfSuccesses) {
                expected = 1.0 - ((double) populationSize - sampleSize + numberOfSuccesses - x) / ((double) populationSize);
            }
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 1e-10);
        }
    }

    /**
     * Test method numericalMean.
     */
    @Test
    public void testNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double actual = distribution.getNumericalMean();
        double expected = ((double) sampleSize * numberOfSuccesses) / ((double) populationSize);
        assertEquals(expected, actual, 1e-10);
    }

    /**
     * Test method numericalVariance.
     */
    @Test
    public void testNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double actual = distribution.getNumericalVariance();
        double expected = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / ((double) Math.pow(populationSize, 2) * (populationSize - 1));
        assertEquals(expected, actual, 1e-10);
    }
}