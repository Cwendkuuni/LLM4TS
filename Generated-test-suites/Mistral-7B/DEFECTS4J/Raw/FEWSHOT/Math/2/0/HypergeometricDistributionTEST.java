package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionCreation() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 0;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 0;
        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 0.001);

        x = 3;
        cumulativeProbability = distribution.cumulativeProbability(x);
        assertTrue(cumulativeProbability > 0.5);

        x = 5;
        cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 0;
        double probability = distribution.probability(x);
        assertEquals(0.034363392662753, probability, 0.00001);

        x = 3;
        probability = distribution.probability(x);
        assertEquals(0.136039258197585, probability, 0.00001);

        x = 5;
        probability = distribution.probability(x);
        assertEquals(0.000437122865873, probability, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 0;
        double upperCumulativeProbability = distribution.upperCumulativeProbability(x);
        assertEquals(1.0, upperCumulativeProbability, 0.001);

        x = 3;
        upperCumulativeProbability = distribution.upperCumulativeProbability(x);
        assertEquals(0.461212461212461, upperCumulativeProbability, 0.00001);

        x = 5;
        upperCumulativeProbability = distribution.upperCumulativeProbability(x);
        assertEquals(0.034363392662753, upperCumulativeProbability, 0.00001);
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();
        assertEquals(3.5, numericalMean, 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(1.19569425834042, numericalVariance, 0.001);
    }

    @Test
    public void testHypergeometricDistributionSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        assertEquals(3, lowerBound);
    }

    @Test
    public void testHypergeometricDistributionSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int upperBound = distribution.getSupportUpperBound();
        assertEquals(5, upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        boolean isSupportConnected = distribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}