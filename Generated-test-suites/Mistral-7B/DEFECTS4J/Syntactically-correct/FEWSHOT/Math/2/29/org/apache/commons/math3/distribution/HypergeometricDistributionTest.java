package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

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
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionNullPopulationSize() {
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        Integer populationSize = null;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionNullNumberOfSuccesses() {
        int populationSize = 10;
        Integer numberOfSuccesses = null;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionNullSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        Integer sampleSize = null;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        int lowerBound = domain[0];
        int x = lowerBound;

        double cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(x / (double) populationSize, cumulativeProbability, 0.0001);

        while (x < domain[1]) {
            x++;
            cumulativeProbability = distribution.cumulativeProbability(x);
            double p = (double) sampleSize / (double) populationSize;
            double q = (double) (populationSize - sampleSize) / (double) populationSize;
            double p1 = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q);
            double p2 = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q);
            double p3 = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
            double expectedCumulativeProbability = FastMath.exp(p1 + p2 - p3);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }

        x = domain[1];
        cumulativeProbability = distribution.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        int x = domain[0];

        double probability = distribution.probability(x);
        double expectedProbability = FastMath.comb(numberOfSuccesses, x) * FastMath.pow(sampleSize / (double) populationSize, x) * FastMath.pow((populationSize - sampleSize) / (double) populationSize, populationSize - x);
        assertEquals(expectedProbability, probability, 0.0001);

        while (x < domain[1]) {
            x++;
            probability = distribution.probability(x);
            expectedProbability = FastMath.comb(numberOfSuccesses, x) * FastMath.pow(sampleSize / (double) populationSize, x) * FastMath.pow((populationSize - sampleSize) / (double) populationSize, populationSize - x);
            assertEquals(expectedProbability, probability, 0.0001);
        }

        x = domain[1];
        probability = distribution.probability(x);
        assertEquals(0.0, probability, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        int x = domain[1];

        double upperCumulativeProbability = distribution.upperCumulativeProbability(x);
        assertEquals(1.0 - upperCumulativeProbability, distribution.cumulativeProbability(domain[0]), 0.0001);
        assertEquals(0.0, upperCumulativeProbability, 0.0001);

        while (x > domain[0]) {
            x--;
            upperCumulativeProbability = distribution.upperCumulativeProbability(x);
            int xPrevious = x - 1;
            double probability = distribution.probability(xPrevious);
            double cumulativeProbability = distribution.cumulativeProbability(x);
            assertEquals(cumulativeProbability, upperCumulativeProbability, 0.0001);
            assertEquals(1.0 - probability, 1.0 - cumulativeProbability, 0.0001);
        }

        x = domain[0];
        upperCumulativeProbability = distribution.upperCumulativeProbability(x);
        assertEquals(1.0 - upperCumulativeProbability, distribution.cumulativeProbability(domain[1]), 0.0001);
        assertEquals(1.0, upperCumulativeProbability, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionSupport() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        int upperBound = distribution.getSupportUpperBound();
        int[] domain = distribution.getDomain();

        assertTrue(lowerBound <= domain[0]);
        assertTrue(domain[0] <= domain[1]);
        assertTrue(domain[1] <= upperBound);
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = (double) (sampleSize * numberOfSuccesses) / populationSize;
        assertEquals(expectedNumericalMean, numericalMean, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = (double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedNumericalVariance, numericalVariance, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionGetDistribution() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        AbstractIntegerDistribution result = distribution.getDistribution();

        assertTrue(result instanceof HypergeometricDistribution);
        assertEquals(populationSize, result.getParams()[0]);
        assertEquals(numberOfSuccesses, result.getParams()[1]);
        assertEquals(sampleSize, result.getParams()[2]);
    }
}