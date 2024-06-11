package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        // Test with valid parameters
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int expected = 2; // The expected number of successes in the sample

        // Test probability of getting x successes
        for (int x = 0; x <= populationSize; x++) {
            double prob = dist.probability(x);
            if (x >= sampleSize && x <= populationSize - sampleSize) {
                assertEquals("Probability for x=" + x + " is not correct.", 1.0 / (double) (populationSize - sampleSize), prob, 0.001);
            } else {
                assertEquals("Probability for x=" + x + " is not correct.", 0.0, prob, 0.001);
            }
        }

        // Test cumulative probability of getting x successes
        for (int x = 0; x <= populationSize; x++) {
            double cumProb = dist.cumulativeProbability(x);
            if (x < expected) {
                assertEquals("Cumulative probability for x=" + x + " is not correct.", 0.0, cumProb, 0.001);
            } else if (x > expected) {
                assertEquals("Cumulative probability for x=" + x + " is not correct.", 1.0, cumProb, 0.001);
            }
        }

        // Test mean and variance
        double mean = dist.getNumericalMean();
        double variance = dist.getNumericalVariance();
        assertEquals("Mean is not correct.", (double) (sampleSize * numberOfSuccesses / populationSize), mean, 0.001);
        assertEquals("Variance is not correct.", (populationSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1)), variance, 0.001);

        // Test support bounds
        int supportLowerBound = dist.getSupportLowerBound();
        int supportUpperBound = dist.getSupportUpperBound();
        assertEquals("Lower support bound is not correct.", populationSize + numberOfSuccesses - sampleSize, supportLowerBound, 1);
        assertEquals("Upper support bound is not correct.", Math.min(numberOfSuccesses, sampleSize), supportUpperBound, 1);

        // Test isSupportConnected
        assertTrue("Support is not connected.", dist.isSupportConnected());

        // Test error cases with invalid parameters
        try {
            new HypergeometricDistribution(-5, 5, 3);
            fail("Expected NotPositiveException.");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(3, 5, -3);
            fail("Expected NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(3, 10, 11);
            fail("Expected NumberIsTooLargeException.");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(3, 10, 5);
            HypergeometricDistribution dist2 = new HypergeometricDistribution(3, 10, 5);
            for (int i = 0; i < 10000; i++) {
                int x = dist.nextInt();
                int y = dist2.nextInt();
                assertEquals("Generated samples are not identical.", x, y);
            }
        } finally {
            // Test that distribution remains consistent after sampling
            assertEquals("Number of successes after sampling is not correct.", numberOfSuccesses, dist.getNumberOfSuccesses());
            assertEquals("Population size after sampling is not correct.", populationSize, dist.getPopulationSize());
            assertEquals("Sample size after sampling is not correct.", sampleSize, dist.getSampleSize());
        }
    }
}