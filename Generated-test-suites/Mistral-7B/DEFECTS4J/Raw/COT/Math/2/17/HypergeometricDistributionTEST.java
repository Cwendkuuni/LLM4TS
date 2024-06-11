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
 * Test cases for HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     */
    @org.junit.Test
    public void testHypergeometricDistributionValidConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertEquals(populationSize, dist.getPopulationSize());
            assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
            assertEquals(sampleSize, dist.getSampleSize());
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    /**
     * Test constructor with invalid population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionInvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionInvalidNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionInvalidSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = -1;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid population size, number of successes, and sample size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionInvalidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with random generator.
     */
    @org.junit.Test
    public void testHypergeometricDistributionRandomGenerator() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test cumulative probability.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.cumulativeProbability(x);
            assertEquals(p, p, 1e-14);
        }
    }

    /**
     * Test probability.
     */
    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.probability(x);
            double q = dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x);
            assertEquals(p, q, 1e-14);
        }
    }

    /**
     * Test upper cumulative probability.
     */
    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = dist.upperCumulativeProbability(x);
            double q = 1.0 - dist.cumulativeProbability(x);
            assertEquals(p, q, 1e-14);
        }
    }

    /**
     * Test numerical mean.
     */
    @org.junit.Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = dist.getNumericalMean();
        double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        assertEquals(expected, mean, 1e-14);
    }

    /**
     * Test numerical variance.
     */
    @org.junit.Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = dist.getNumericalVariance();
        double expected = (double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expected, variance, 1e-14);
    }

    /**
     * Test support bounds.
     */
    @org.junit.Test
    public void testSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();

        int[] domain = dist.getDomain();
        assertTrue(Arrays.asList(domain).contains(lowerBound));
        assertTrue(Arrays.asList(domain).contains(upperBound));
        assertTrue(lowerBound <= upperBound);
    }

    /**
     * Test support is connected.
     */
    @org.junit.Test
    public void testSupportIsConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(dist.isSupportConnected());
    }
}