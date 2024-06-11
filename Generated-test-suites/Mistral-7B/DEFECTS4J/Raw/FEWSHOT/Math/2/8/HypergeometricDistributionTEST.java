package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Random random = new MersenneTwister();

        // Test getters
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());

        // Test probability
        for (int i = 0; i <= sampleSize; i++) {
            double prob = dist.probability(i);
            double cumProb = dist.cumulativeProbability(i);
            assertEquals("Probability of i should equal cumulative probability of i and next integers.",
                         prob, dist.cumulativeProbability(i + 1), 1e-10);
            assertTrue("Probability of success should be greater than or equal to 0 and less than or equal to 1.",
                       prob >= 0.0 && prob <= 1.0);
        }

        // Test cumulativeProbability
        for (int i = 0; i <= sampleSize; i++) {
            double cumProb = dist.cumulativeProbability(i);
            assertTrue("Cumulative probability of i should be greater than or equal to 0 and less than or equal to 1.",
                       cumProb >= 0.0 && cumProb <= 1.0);
        }

        // Test mean
        double mean = dist.getNumericalMean();
        assertEquals("Mean should match expected value.", mean, (double) (sampleSize * numberOfSuccesses / populationSize));

        // Test variance
        double variance = dist.getNumericalVariance();
        assertEquals("Variance should match expected value.", variance,
                     ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                     / ((double) Math.pow(populationSize, 2) * (populationSize - 1)));

        // Test support bounds
        assertEquals("Lower bound of support should match expected value.",
                     dist.getSupportLowerBound(), Math.max(0, sampleSize + numberOfSuccesses - populationSize));
        assertEquals("Upper bound of support should match expected value.",
                     dist.getSupportUpperBound(), Math.min(numberOfSuccesses, sampleSize));

        // Test isSupportConnected
        assertTrue("Support should be connected.", dist.isSupportConnected());

        // Test generateNextInt
        for (int i = 0; i < 100; i++) {
            int x = dist.nextInt(random);
            assertTrue("Generated integer should be within the support bounds.", x >= dist.getSupportLowerBound()
                       && x <= dist.getSupportUpperBound());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionInvalidParameters() {
        int populationSize = 0;
        int numberOfSuccesses = -1;
        int sampleSize = 1;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionInvalidParameters2() {
        int populationSize = 1;
        int numberOfSuccesses = 10;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHypergeometricDistributionInvalidParameters3() {
        int populationSize = 1;
        int numberOfSuccesses = 1;
        int sampleSize = 10;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }
}