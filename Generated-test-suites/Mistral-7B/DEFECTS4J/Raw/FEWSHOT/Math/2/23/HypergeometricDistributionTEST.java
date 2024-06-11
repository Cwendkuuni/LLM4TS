package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain();
        int lowerDomain = domain[0];
        int upperDomain = domain[1];

        assertEquals(lowerDomain, FastMath.max(0, numberOfSuccesses + sampleSize - populationSize));
        assertEquals(upperDomain, FastMath.min(numberOfSuccesses, sampleSize));

        assertEquals(populationSize, hypergeometricDistribution.getPopulationSize());
        assertEquals(numberOfSuccesses, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(sampleSize, hypergeometricDistribution.getSampleSize());

        assertEquals(numberOfSuccesses, hypergeometricDistribution.getSupportLowerBound());
        assertEquals(sampleSize, hypergeometricDistribution.getSupportUpperBound());
        assertTrue(hypergeometricDistribution.isSupportConnected());

        assertEquals(populationSize, hypergeometricDistribution.getNumericalMean());

        double numericalVariance = hypergeometricDistribution.getNumericalVariance();
        assertTrue(isNaN(numericalVariance));
        numericalVariance = hypergeometricDistribution.calculateNumericalVariance();
        assertNotEquals(numericalVariance, 0.0);
        assertEquals(numericalVariance, hypergeometricDistribution.getNumericalVariance());
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double cumulativeProbability = hypergeometricDistribution.cumulativeProbability(x);
            double probability = hypergeometricDistribution.probability(x);

            double expectedCumulativeProbability = 0.0;
            for (int i = domain[0]; i < x; i++) {
                expectedCumulativeProbability += probability;
            }
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double probability = hypergeometricDistribution.probability(x);
            double expectedProbability = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, 1.0 / (double) populationSize, 1.0 - 1.0 / (double) populationSize);
            double logProbabilityDifference = Math.abs(FastMath.log(probability) - expectedProbability);
            assertTrue(logProbabilityDifference < 1e-5);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;

        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hypergeometricDistribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double upperCumulativeProbability = hypergeometricDistribution.upperCumulativeProbability(x);
            double expectedUpperCumulativeProbability = 1.0 - hypergeometricDistribution.cumulativeProbability(x - 1);
            assertEquals(expectedUpperCumulativeProbability, upperCumulativeProbability, 0.0);
        }
    }
}