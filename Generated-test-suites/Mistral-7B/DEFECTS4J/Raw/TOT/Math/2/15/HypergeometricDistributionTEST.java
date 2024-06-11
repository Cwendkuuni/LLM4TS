package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test that an exception is thrown when population size is non-positive.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    /**
     * Test that an exception is thrown when number of successes is non-positive.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
    }

    /**
     * Test that an exception is thrown when sample size is non-positive.
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
    }

    /**
     * Test that an exception is thrown when number of successes is larger than
     * population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManySuccesses() {
        new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
    }

    /**
     * Test that an exception is thrown when sample size is larger than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooLargeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, 2);
    }

    /**
     * Test that the distribution is unimodal.
     */
    @Test
    public void testUnimodality() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        final int[] expected = new int[] { 0, 1, 2, 3, 4, 3, 2, 1, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], dist.cumulativeProbability(i));
        }
    }

    /**
     * Test that the mean is correct.
     */
    @Test
    public void testMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        final double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        assertEquals(expected, dist.getNumericalMean(), 0.001);
    }

    /**
     * Test that the variance is correct.
     */
    @Test
    public void testVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        final double expected = (double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize)
                * (populationSize - numberOfSuccesses))
                / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expected, dist.getNumericalVariance(), 0.001);
    }

    /**
     * Test that the probability mass function is correct.
     */
    @Test
    public void testProbabilityMassFunction() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        final double p = (double) sampleSize / populationSize;
        final double q = (double) (populationSize - sampleSize) / populationSize;
        final double expected[] = new double[] {
                0.000389783359466, 0.000779566719933, 0.00135843342926, 0.0021373001486,
                0.0029654272149, 0.0037033324429, 0.0042415133389, 0.004481474879, 0.0042415133389,
                0.0037033324429, 0.0029654272149, 0.0021373001486, 0.00135843342926, 0.000779566719933,
                0.000389783359466
        };
        for (int i = 0; i < dist.domain.length; i++) {
            assertEquals(expected[i], dist.probability(i), 0.001);
        }
    }

    /**
     * Test that the distribution has a support.
     */
    @Test
    public void testSupport() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        assertEquals(dist.getSupportLowerBound(), 3);
        assertEquals(dist.getSupportUpperBound(), 5);
    }

    /**
     * Test that the support is connected.
     */
    @Test
    public void testSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        final HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(),
                populationSize, numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }
}