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
     * @throws Exception if initialization fails
     */
    @Test
    public void testConstructor() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
        assertEquals(numberOfSuccesses, distribution.getSupportLowerBound());
        assertEquals(Math.min(numberOfSuccesses, sampleSize), distribution.getSupportUpperBound());
    }

    /**
     * Test constructor with invalid number of successes.
     * @throws Exception if initialization fails
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, 3);
    }

    /**
     * Test constructor with invalid population size.
     * @throws Exception if initialization fails
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() throws Exception {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sample size.
     * @throws Exception if initialization fails
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes greater than population size.
     * @throws Exception if initialization fails
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size greater than population size.
     * @throws Exception if initialization fails
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test method cumulativeProbability in normal case.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testCumulativeProbabilityNormalCase() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.2734375;
        double actual = distribution.cumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method cumulativeProbability with lower bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testCumulativeProbabilityLowerBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = distribution.cumulativeProbability(0);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method cumulativeProbability with upper bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testCumulativeProbabilityUpperBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 1.0;
        double actual = distribution.cumulativeProbability(5);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method probability in normal case.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testProbabilityNormalCase() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.13888888888888889;
        double actual = distribution.probability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method probability with lower bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testProbabilityLowerBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = distribution.probability(0);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method probability with upper bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testProbabilityUpperBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.2734375;
        double actual = distribution.probability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method upperCumulativeProbability in normal case.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testUpperCumulativeProbabilityNormalCase() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.7265625;
        double actual = distribution.upperCumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method upperCumulativeProbability with lower bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testUpperCumulativeProbabilityLowerBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 1.0;
        double actual = distribution.upperCumulativeProbability(5);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method upperCumulativeProbability with upper bound.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testUpperCumulativeProbabilityUpperBound() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = distribution.upperCumulativeProbability(6);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test method numericalVariance in normal case.
     * @throws Exception if random number generation fails
     */
    @Test
    public void testNumericalVarianceNormalCase() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 0.0388888888888889;
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 1e-12);
    }
}