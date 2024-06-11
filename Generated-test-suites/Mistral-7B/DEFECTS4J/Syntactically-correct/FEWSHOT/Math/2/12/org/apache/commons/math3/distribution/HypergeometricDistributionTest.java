package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 5;
        int numberOfSuccesses = -3;
        int sampleSize = 2;
        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            throw new AssertionError("Unexpected NumberIsTooLargeException", e);
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 0;
        int numberOfSuccesses = 2;
        int sampleSize = 3;
        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMoreSuccessesThanPopulationSize() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 4;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        RandomGenerator rng = new Well19937c();
        int populationSize = 5;
        int numberOfSuccesses = 2;
        int sampleSize = 6;
        try {
            new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException e) {
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test
    public void testHypergeometricDistribution() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 5, 4);

        assertEquals(4, dist.getSampleSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getPopulationSize());

        assertEquals(0.3, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.428571, dist.cumulativeProbability(3), 0.001);
        assertEquals(0.495022, dist.cumulativeProbability(4), 0.001);
        assertEquals(0.538678, dist.cumulativeProbability(5), 0.001);
        assertEquals(0.571429, dist.cumulativeProbability(6), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001);

        assertEquals(0.3, dist.probability(2), 0.001);
        assertEquals(0.045398, dist.probability(3), 0.001);
        assertEquals(0.110111, dist.probability(4), 0.001);
        assertEquals(0.166667, dist.probability(5), 0.001);
        assertEquals(0.1875, dist.probability(6), 0.001);
        assertEquals(0.013333, dist.probability(7), 0.001);

        assertEquals(0.6, dist.upperCumulativeProbability(2), 0.001);
        assertEquals(0.454978, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(0.333333, dist.upperCumulativeProbability(4), 0.001);
        assertEquals(0.25, dist.upperCumulativeProbability(5), 0.001);
        assertEquals(0.2, dist.upperCumulativeProbability(6), 0.001);
        assertEquals(0.05, dist.upperCumulativeProbability(7), 0.001);

        assertEquals(4, dist.getSupportLowerBound());
        assertEquals(5, dist.getSupportUpperBound());
        assertEquals(true, dist.isSupportConnected());

        assertEquals(4.5, dist.getNumericalMean(), 0.001);
        assertEquals(3.5625, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionWithZeroPopulationSize() {
        RandomGenerator rng = new Well19937c();
        try {
            new HypergeometricDistribution(rng, 0, 1, 1);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Population size must be positive", e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionWithZeroNumberOfSuccesses() {
        RandomGenerator rng = new Well19937c();
        try {
            new HypergeometricDistribution(rng, 10, 0, 4);
        } catch (NotPositiveException e) {
            assertEquals("Number of successes in the population must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionWithNegativeSampleSize() {
        RandomGenerator rng = new Well19937c();
        try {
            new HypergeometricDistribution(rng, 10, 5, -3);
        } catch (NotPositiveException e) {
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Sample size must be positive", e.getMessage());
        }
    }
}