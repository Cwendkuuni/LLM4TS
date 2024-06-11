package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccessesException() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSizeException() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccessesException() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSizeException() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSizeException() {
        int populationSize = 10;
        int numberOfSuccesses = 12;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSizeException() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 12;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testHypergeometricDistributionGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testHypergeometricDistributionGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
    }

    @Test
    public void testHypergeometricDistributionGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double probability = distribution.cumulativeProbability(x);
            int binomialProbability = SaddlePointExpansion.binomialProbability(x, populationSize, numberOfSuccesses / (double) populationSize);
            assertEquals(binomialProbability, probability, 1e-9);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= populationSize; x++) {
            double probability = distribution.probability(x);
            if (x >= numberOfSuccesses && x <= numberOfSuccesses + sampleSize - 1) {
                double binomialProbability = SaddlePointExpansion.binomialProbability(x, populationSize, numberOfSuccesses / (double) populationSize);
                double cumulativeProbability = distribution.cumulativeProbability(x);
                assertEquals(binomialProbability, probability, 1e-9);
                assertEquals(cumulativeProbability, probability, 1e-9);
            } else {
                assertEquals(0.0, probability, 1e-9);
            }
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double upperCumulativeProbability = distribution.upperCumulativeProbability(x);
            assertEquals(1.0 - distribution.cumulativeProbability(x), upperCumulativeProbability, 1e-9);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperTailProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = populationSize + 1; x <= populationSize + numberOfSuccesses; x++) {
            double upperTailProbability = distribution.upperTailProbability(x);
            double binomialProbability = SaddlePointExpansion.binomialProbability(x, populationSize, 1.0 - (double) numberOfSuccesses / populationSize);
            assertEquals(binomialProbability, upperTailProbability, 1e-9);
        }
    }

    @Test
    public void testHypergeometricDistributionGetDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        int lowerDomain = domain[0];
        int upperDomain = domain[1];

        assertTrue(lowerDomain >= 0);
        assertTrue(upperDomain >= lowerDomain);
        assertTrue(upperDomain <= populationSize);

        int x = lowerDomain;
        while (x <= upperDomain) {
            assertEquals(x, distribution.getSupportLowerBound() + x - populationSize);
            assertEquals(x, distribution.getSupportUpperBound() & x);
            x++;
        }
    }

    @Test
    public void testHypergeometricDistributionSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testHypergeometricDistributionMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(sampleSize * numberOfSuccesses / populationSize, distribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();

        double expectedVariance = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));

        assertEquals(expectedVariance, numericalVariance, 1e-9);
    }
}