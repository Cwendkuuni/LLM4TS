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
     * Test constructing a hypergeometric distribution with valid parameters.
     */
    @Test
    public void testHypergeometricDistributionConstruction() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());
    }

    /**
     * Test constructing a hypergeometric distribution with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionNegativeNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -3;
        final int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructing a hypergeometric distribution with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionZeroPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructing a hypergeometric distribution with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionNegativeSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = -5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructing a hypergeometric distribution with invalid sample size greater than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructionInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test getting the domain of the hypergeometric distribution.
     */
    @Test
    public void testGetDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = hd.getDomain();
        assertEquals(2, domain.length);
        assertEquals(3, domain[0]);
        assertEquals(5, domain[1]);
    }

    /**
     * Test getting the lower domain value for the given hypergeometric distribution
     */
    @Test
    public void testGetLowerDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerDomain = hd.getLowerDomain();
        assertEquals(3, lowerDomain);
    }

    /**
     * Test getting the upper domain value for the given hypergeometric distribution
     */
    @Test
    public void testGetUpperDomain() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperDomain = hd.getUpperDomain();
        assertEquals(5, upperDomain);
    }

    /**
     * Test probabilities for valid and invalid values.
     */
    @Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test probability for valid value
        int x = 3;
        double probability = hd.probability(x);
        assertEquals(0.3, probability, 1e-6);

        // Test probability for invalid lower bound
        x = 2;
        probability = hd.probability(x);
        assertEquals(0.0, probability, 1e-6);

        // Test probability for invalid upper bound
        x = 6;
        probability = hd.probability(x);
        assertEquals(0.35714286, probability, 1e-6);
    }

    /**
     * Test cumulative probabilities for valid and invalid values.
     */
    @Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test cumulative probability for valid values
        int x1 = 3;
        double cdf1 = hd.cumulativeProbability(x1);
        assertEquals(0.3, cdf1, 1e-6);

        int x2 = 4;
        double cdf2 = hd.cumulativeProbability(x2);
        assertEquals(0.42857143, cdf2, 1e-6);

        // Test cumulative probability for invalid lower bound
        int x3 = 2;
        double cdf3 = hd.cumulativeProbability(x3);
        assertEquals(0.0, cdf3, 1e-6);

        // Test cumulative probability for invalid upper bound
        int x4 = 7;
        double cdf4 = hd.cumulativeProbability(x4);
        assertEquals(0.61542858, cdf4, 1e-6);
    }

    /**
     * Test upper cumulative probabilities for valid and invalid values.
     */
    @Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        // Test upper cumulative probability for valid values
        int x1 = 3;
        double upperCdf1 = hd.upperCumulativeProbability(x1);
        assertEquals(0.7, upperCdf1, 1e-6);

        int x2 = 4;
        double upperCdf2 = hd.upperCumulativeProbability(x2);
        assertEquals(0.57142857, upperCdf2, 1e-6);

        // Test upper cumulative probability for invalid lower bound
        int x3 = 2;
        double upperCdf3 = hd.upperCumulativeProbability(x3);
        assertEquals(1.0, upperCdf3, 1e-6);

        // Test upper cumulative probability for invalid upper bound
        int x4 = 7;
        double upperCdf4 = hd.upperCumulativeProbability(x4);
        assertEquals(0.38457143, upperCdf4, 1e-6);
    }

    /**
     * Test mean and numerical variance.
     */
    @Test
    public void testMeanAndNumericalVariance() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = hd.getNumericalMean();
        assertEquals(5.0, mean, 1e-6);

        double numericalVariance = hd.getNumericalVariance();
        assertEquals(1.10471976, numericalVariance, 1e-6);
    }
}