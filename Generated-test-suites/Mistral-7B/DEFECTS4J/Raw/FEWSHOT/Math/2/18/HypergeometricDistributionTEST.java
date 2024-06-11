package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.special.SaddlePointExpansion;

import java.util.Arrays;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstruction() {
        // Test valid construction
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Test invalid construction
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, -1, sampleSize));
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, 0, sampleSize));
        assertThrows(NotPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, -1));
        assertThrows(NotStrictlyPositiveException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, 0));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize + 1));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, populationSize + 1, sampleSize));
        assertThrows(NumberIsTooLargeException.class, () -> new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize + populationSize));
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 20;
        int numberOfSuccesses = 15;
        int sampleSize = 10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Test lower domain value
        assertEquals(0, dist.cumulativeProbability(domain[0] - 1), 0.0001);

        // Test domain values
        for (int x : Arrays.copyOfRange(domain, 0, domain.length)) {
            assertEquals(dist.probability(x), dist.cumulativeProbability(x), 0.0001);
        }

        // Test upper domain value
        assertEquals(1, dist.cumulativeProbability(domain[1] + 1), 0.0001);
    }

    @Test
    public void testProbability() {
        int populationSize = 20;
        int numberOfSuccesses = 15;
        int sampleSize = 10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Test lower domain value
        assertNull(dist.probability(domain[0] - 1));

        // Test domain values
        for (int x : Arrays.copyOfRange(domain, 0, domain.length)) {
            assertEquals(dist.cumulativeProbability(x + 1) - dist.cumulativeProbability(x), dist.probability(x), 0.0001);
        }

        // Test upper domain value
        assertNull(dist.probability(domain[1] + 1));

        // Test extreme probabilities
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(1.0, dist.probability(populationSize), 0.0001);
    }

    @Test
    public void testMeanAndVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(6.0, dist.getNumericalMean(), 0.001);
        assertEquals(3.5, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 20;
        int numberOfSuccesses = 15;
        int sampleSize = 10;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Test lower domain value
        assertEquals(1.0, dist.upperCumulativeProbability(domain[0] - 1), 0.0001);

        // Test domain values
        for (int x : Arrays.copyOfRange(domain, 0, domain.length)) {
            assertEquals(1.0 - dist.cumulativeProbability(x), dist.upperCumulativeProbability(x), 0.0001);
        }

        // Test upper domain value
        assertEquals(0.0, dist.upperCumulativeProbability(domain[1] + 1), 0.0001);
    }

    @Test
    public void testSaddlePointExpansion() {
        int populationSize = 10;
        int numberOfSuccesses = 4;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 3;
        double logProb = dist.logProbability(x);
        double logBinomialProbabilityX = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, 6.0 / 11.0, 5.0 / 11.0);
        double logBinomialProbabilityN = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, 6.0 / 11.0, 5.0 / 11.0);
        double logBinomialProbabilityS = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, 6.0 / 11.0, 5.0 / 11.0);

        assertEquals(logProb, logBinomialProbabilityX + logBinomialProbabilityN - logBinomialProbabilityS, 0.001);
    }
}