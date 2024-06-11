package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid arguments.
     */
    @Test
    public void testConstructionValidArguments() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(populationSize, dist.getPopulationSize());
        Assert.assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        Assert.assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid arguments - population size <= 0.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments - number of successes < 0.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidNumberOfSuccesses() {
        final int populationSize = 5;
        final int numberOfSuccesses = -1;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments - sample size <= 0.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidSampleSize() {
        final int populationSize = 5;
        final int numberOfSuccesses = 5;
        final int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments - number of successes > population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidNumberOfSuccessesGreaterThanPopulationSize() {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments - sample size > population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidSampleSizeGreaterThanPopulationSize() {
        final int populationSize = 5;
        final int numberOfSuccesses = 5;
        final int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test probability calculation with valid arguments.
     */
    @Test
    public void testProbabilityValidArguments() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 2;
        double expectedProbability = dist.probability(x);
        double actualProbability = dist.cumulativeProbability(x) - dist.cumulativeProbability(x - 1);
        Assert.assertEquals(expectedProbability, actualProbability, 0.001);
    }

    /**
     * Test probability calculation with edge cases.
     */
    @Test
    public void testProbabilityEdgeCases() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerLimit = dist.getSupportLowerBound();
        int upperLimit = dist.getSupportUpperBound();
        for (int x = lowerLimit; x <= upperLimit; x++) {
            double expectedProbability = dist.probability(x);
            double actualProbability = dist.cumulativeProbability(x) - dist.cumulativeProbability(x - 1);
            Assert.assertEquals(expectedProbability, actualProbability, 0.001);
        }
    }

    /**
     * Test probability calculation with error scenarios.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testProbabilityErrorScenarios() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = -1;
        dist.probability(x);
    }

    /**
     * Test cumulative probability calculation with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArguments() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 2;
        double expectedCumulativeProbability = dist.cumulativeProbability(x);
        double actualCumulativeProbability = dist.probability(x) + dist.probability(x - 1);
        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
    }

    /**
     * Test cumulative probability calculation with edge cases.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerLimit = dist.getSupportLowerBound();
        int upperLimit = dist.getSupportUpperBound();
        for (int x = lowerLimit; x <= upperLimit; x++) {
            double expectedCumulativeProbability = dist.cumulativeProbability(x);
            double actualCumulativeProbability = 0.0;
            for (int i = 0; i <= x; i++) {
                actualCumulativeProbability += dist.probability(i);
            }
            Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
        }
    }

    /**
     * Test cumulative probability calculation with error scenarios.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCumulativeProbabilityErrorScenarios() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = -1;
        dist.cumulativeProbability(x);
    }

    /**
     * Test mean calculation with valid arguments.
     */
    @Test
    public void testMeanValidArguments() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedMean = (double) (sampleSize * numberOfSuccesses / populationSize);
        double actualMean = dist.getNumericalMean();
        Assert.assertEquals(expectedMean, actualMean, 0.001);
    }

    /**
     * Test mean calculation with error scenarios.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMeanErrorScenarios() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.getNumericalVariance();
    }

    /**
     * Test variance calculation with valid arguments.
     */
    @Test
    public void testVarianceValidArguments() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedVariance = (double) ((sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1)));
        double actualVariance = dist.getNumericalVariance();
        Assert.assertEquals(expectedVariance, actualVariance, 0.001);
    }

    /**
     * Test variance calculation with error scenarios.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testVarianceErrorScenarios() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.getNumericalMean();
    }
}