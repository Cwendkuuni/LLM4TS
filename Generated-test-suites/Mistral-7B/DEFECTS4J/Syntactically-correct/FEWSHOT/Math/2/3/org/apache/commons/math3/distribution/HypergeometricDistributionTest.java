package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
            assertEquals("Number of successes must be non-negative: -1", e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Population size must be strictly positive: 0", e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Number of successes must be strictly positive: 0", e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 0;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Sample size must be strictly positive: 0", e.getMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Number of successes must not be greater than population size: 11",
                         e.getMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Sample size must not be greater than population size: 11",
                         e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(3.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        double variance = distribution.getNumericalVariance();
        assertTrue(variance > 0.0);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);

        int numberOfSuccessesInSample = 2;
        double expectedProbability = 0.24375;
        double actualProbability = distribution.probability(numberOfSuccessesInSample);
        assertEquals(expectedProbability, actualProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);

        int numberOfSuccessesInSample = 2;
        double expectedCumulativeProbability = 0.46875;
        double actualCumulativeProbability = distribution.cumulativeProbability(numberOfSuccessesInSample);
        assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);

        int numberOfSuccessesInSample = 7;
        double expectedUpperCumulativeProbability = 0.9375;
        double actualUpperCumulativeProbability = distribution.upperCumulativeProbability(numberOfSuccessesInSample);
        assertEquals(expectedUpperCumulativeProbability, actualUpperCumulativeProbability, 0.001);
    }
}