package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstructionValidParameters() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
        assertFalse(Double.isNaN(dist.getNumericalMean()));
        assertFalse(Double.isNaN(dist.getNumericalVariance()));
    }

    /**
     * Test construction with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 10;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -2;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = -2;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (sample size larger than population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with random generator.
     */
    @Test
    public void testConstructionWithRandomGenerator() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(
                new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
        assertFalse(Double.isNaN(dist.getNumericalMean()));
        assertFalse(Double.isNaN(dist.getNumericalVariance()));
    }

    /**
     * Test probability method with valid parameters.
     */
    @Test
    public void testProbabilityValidParameters() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int x = 3;
        final double expected = dist.probability(x);
        // The exact value of P(X = x) cannot be calculated analytically, but
        // we can test that the calculated value is close enough to the one
        // obtained from the CDF.
        final double cdfValue = dist.cumulativeProbability(x);
        final double eps = 1e-10;
        assertTrue(Math.abs(expected - cdfValue) < eps);
    }

    /**
     * Test probability method with invalid parameters (x value outside support).
     */
    @Test
    public void testProbabilityInvalidParameters() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int x = 25;
        try {
            dist.probability(x);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test cumulativeProbability method with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityValidParameters() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int x = 3;
        final double expected = dist.cumulativeProbability(x);
        final int[] domain = dist.getDomain();
        assertTrue(x >= domain[0] && x <= domain[1]);
        // The exact value of P(X <= x) cannot be calculated analytically, but
        // we can test that the calculated value is close enough to the one
        // obtained from the PMF.
        final double pmfValue = dist.probability(x);
        final double eps = 1e-10;
        assertTrue(Math.abs(expected - pmfValue * (x + 1.0)) < eps);
    }

    /**
     * Test cumulativeProbability method with invalid parameters (x value outside support).
     */
    @Test
    public void testCumulativeProbabilityInvalidParameters() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final int x = 25;
        try {
            dist.cumulativeProbability(x);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        assertEquals(expected, dist.getNumericalMean(), 1e-10);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final int populationSize = 20;
        final int numberOfSuccesses = 10;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        final double expected =
                (double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) /
                (populationSize * populationSize * (populationSize - 1));
        assertEquals(expected, dist.getNumericalVariance(), 1e-10);
    }
}