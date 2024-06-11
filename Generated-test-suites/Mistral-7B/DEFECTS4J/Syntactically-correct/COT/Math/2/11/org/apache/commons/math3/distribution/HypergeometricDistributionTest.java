package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testValidConstructor() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        assertEquals(popSize, dist.getPopulationSize());
        assertEquals(successes, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
        assertEquals(successes, dist.getSupportLowerBound());
        assertEquals(Math.min(successes, sampleSize), dist.getSupportUpperBound());
        assertEquals(true, dist.isSupportConnected());
    }

    /**
     * Test constructor with invalid arguments - population size is not positive.
     * @throws Exception if an exception is not thrown.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() throws Exception {
        final int popSize = 0;
        final int successes = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(popSize, successes, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - number of successes is not non-negative.
     * @throws Exception if an exception is not thrown.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() throws Exception {
        final int popSize = 10;
        final int successes = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(popSize, successes, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - sample size is not non-negative.
     * @throws Exception if an exception is not thrown.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = -1;
        new HypergeometricDistribution(popSize, successes, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - number of successes is larger than population size.
     * @throws Exception if an exception is not thrown.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidNumberOfSuccessesLargerThanPopulationSize() throws Exception {
        final int popSize = 10;
        final int successes = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(popSize, successes, sampleSize);
    }

    /**
     * Test constructor with invalid arguments - sample size is larger than population size.
     * @throws Exception if an exception is not thrown.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidSampleSizeLargerThanPopulationSize() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(popSize, successes, sampleSize);
    }

    /**
     * Test probability method with valid arguments.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testProbability() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        for (int i = dist.getSupportLowerBound(); i <= dist.getSupportUpperBound(); i++) {
            double probability = dist.probability(i);
            assertEquals(probability, dist.cumulativeProbability(i + 1) - dist.cumulativeProbability(i), 0.00001);
        }
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        for (int i = dist.getSupportLowerBound(); i <= dist.getSupportUpperBound(); i++) {
            double cdf = dist.cumulativeProbability(i);
            assertEquals(cdf, dist.probability(i) + dist.cumulativeProbability(i - 1), 0.00001);
        }
    }

    /**
     * Test upperCumulativeProbability method with valid arguments.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testUpperCumulativeProbability() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        for (int i = dist.getSupportLowerBound(); i <= dist.getSupportUpperBound(); i++) {
            double cdf = dist.upperCumulativeProbability(i);
            assertEquals(1.0 - dist.cumulativeProbability(i), cdf, 0.00001);
        }
    }

    /**
     * Test getNumericalMean method.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testGetNumericalMean() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        double mean = dist.getNumericalMean();
        assertEquals(mean, (double)sampleSize * successes / (double)popSize, 0.00001);
    }

    /**
     * Test getNumericalVariance method.
     * @throws Exception if an exception is thrown.
     */
    @Test
    public void testGetNumericalVariance() throws Exception {
        final int popSize = 10;
        final int successes = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(popSize, successes, sampleSize);
        double variance = dist.getNumericalVariance();
        double expectedVariance = ((double)sampleSize * successes * (popSize - sampleSize) * (popSize - successes)) / ((popSize * popSize) * (popSize - 1));
        assertEquals(variance, expectedVariance, 0.00001);
    }
}