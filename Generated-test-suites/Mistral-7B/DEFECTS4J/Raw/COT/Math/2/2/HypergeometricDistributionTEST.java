package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.Test
    public void testHypergeometricDistributionConstruction() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooManySuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionTooLargeSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int i = 0; i <= sampleSize; i++) {
            double prob = dist.probability(i);
            int[] domain = dist.getDomain();
            assertTrue(i >= domain[0] || i >= domain[1]);
            assertEquals(dist.cumulativeProbability(i), prob, 1e-10);
        }
    }

    @org.junit.Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double prob = dist.probability(i);
            double cumProb = dist.cumulativeProbability(i);
            assertEquals(cumProb, prob + dist.cumulativeProbability(i - 1), 1e-10);
        }
    }

    @org.junit.Test
    public void testHypergeometricDistributionMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = dist.getNumericalMean();
        assertEquals(sampleSize * numberOfSuccesses.doubleValue() / populationSize, mean, 1e-10);
    }

    @org.junit.Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = dist.getNumericalVariance();
        double expectedVariance = (sampleSize * numberOfSuccesses.doubleValue() * (populationSize - sampleSize) * (populationSize - numberOfSuccesses.doubleValue())) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedVariance, variance, 1e-10);
    }

}