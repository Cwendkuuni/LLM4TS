package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid arguments.
     */
    @Test
    public void testConstruction() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid arguments (population size <= 0).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionPopulationSizeInvalid() {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments (number of successes < 0).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionNumberOfSuccessesInvalid() {
        final int populationSize = 5;
        final int numberOfSuccesses = -3;
        final int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments (sample size <= 0).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionSampleSizeInvalid() {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 0;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments (number of successes > population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionNumberOfSuccessesTooLarge() {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid arguments (sample size > population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionSampleSizeTooLarge() {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 6;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double prob;

        // Test lower bound
        x = getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        prob = dist.cumulativeProbability(x);
        assertTrue(prob >= 0.0);

        // Test upper bound
        x = getUpperDomain(numberOfSuccesses, sampleSize);
        prob = dist.cumulativeProbability(x);
        assertTrue(prob <= 1.0);

        // Test values in between
        x = x + 1;
        prob = dist.cumulativeProbability(x);
        assertTrue(prob > prob);
    }

    /**
     * Test {@link #probability(int)}.
     */
    @Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double prob;

        // Test lower bound
        x = getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        prob = dist.probability(x);
        assertTrue(prob > 0.0);

        // Test upper bound
        x = getUpperDomain(numberOfSuccesses, sampleSize);
        prob = dist.probability(x);
        assertTrue(prob > 0.0);

        // Test values in between
        x++;
        prob = dist.probability(x);
        assertTrue(prob < prob);
    }

    /**
     * Test {@link #upperCumulativeProbability(int)}.
     */
    @Test
    public void testUpperCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x;
        double prob;

        // Test lower bound
        x = getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        prob = dist.upperCumulativeProbability(x);
        assertTrue(prob == 1.0 - dist.cumulativeProbability(x));

        // Test upper bound
        x = getUpperDomain(numberOfSuccesses, sampleSize);
        prob = dist.upperCumulativeProbability(x);
        assertTrue(prob == 0.0);

        // Test values in between
        x++;
        prob = dist.upperCumulativeProbability(x);
        assertTrue(prob == 1.0 - dist.cumulativeProbability(x - 1));
    }

    /**
     * Test {@link #getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double mean = dist.getNumericalMean();
        assertEquals(mean, (double) (sampleSize * numberOfSuccesses) / populationSize, 0.0);
    }

    /**
     * Test {@link #getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double variance = dist.getNumericalVariance();
        double expectedVariance = (double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(variance, expectedVariance, 0.0);
    }

    /**
     * Test {@link #isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }

    private int getLowerDomain(int populationSize, int numberOfSuccesses, int sampleSize) {
        return FastMath.max(0, numberOfSuccesses - (populationSize - sampleSize));
    }

    private int getUpperDomain(int numberOfSuccesses, int sampleSize) {
        return FastMath.min(numberOfSuccesses, sampleSize);
    }
}