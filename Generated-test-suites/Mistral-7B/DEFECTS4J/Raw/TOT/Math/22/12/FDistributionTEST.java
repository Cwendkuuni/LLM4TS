package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test that an {@code FDistribution} can be instantiated with positive
     * degrees of freedom.
     */
    @Test
    public void testFInstance() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertTrue(distribution instanceof FDistribution);
    }

    /**
     * Test that an {@code FDistribution} throws an exception when given
     * non-positive degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFNegativeDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = 0.0;
        final double denominatorDegreesOfFreedom = 2.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test the density method for various input values.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(0.398942280401431, distribution.density(1.0), 1e-9);
        assertEquals(0.135335283236612, distribution.density(2.0), 1e-9);
        assertEquals(-0.215068319889757, distribution.density(0.5), 1e-9);
    }

    /**
     * Test the cumulative probability method for various input values.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(0.15865525, distribution.cumulativeProbability(1.0), 1e-9);
        assertEquals(0.68268949, distribution.cumulativeProbability(2.0), 1e-9);
        assertFalse(Double.isFinite(distribution.cumulativeProbability(0.5)));
    }

    /**
     * Test the getNumericalMean method.
     */
    @Test
    public void testNumericalMean() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(1.5, distribution.getNumericalMean(), 1e-9);
    }

    /**
     * Test the getNumericalVariance method.
     */
    @Test
    public void testNumericalVariance() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(1.20814443, distribution.getNumericalVariance(), 1e-9);
    }

    /**
     * Test the getSupportLowerBound method.
     */
    @Test
    public void testSupportLowerBound() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(0, distribution.getSupportLowerBound(), 1e-9);
    }

    /**
     * Test the getSupportUpperBound method.
     */
    @Test
    public void testSupportUpperBound() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 1e-9);
    }

    /**
     * Test the getSupportLowerBoundInclusive method.
     */
    @Test
    public void testSupportLowerBoundInclusive() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test the getSupportUpperBoundInclusive method.
     */
    @Test
    public void testSupportUpperBoundInclusive() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test the isSupportConnected method.
     */
    @Test
    public void testSupportConnected() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test the random sampling using a {@code Well19937c} random number
     * generator.
     */
    @Test
    public void testRandomSampling() {
        final double numeratorDegreesOfFreedom = 1.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        final RandomGenerator rng = distribution.getRandomGenerator();
        final int numberOfSamples = 10000;
        final double[] samples = new double[numberOfSamples];
        for (int i = 0; i < numberOfSamples; i++) {
            samples[i] = distribution.inverseCumulativeProbability(rng.nextDouble());
        }
        // the mean of the samples is close to the theoretical mean
        assertEquals(1.5, mean(samples), 1e-5);
    }

    /**
     * Calculates the mean of a given double array.
     *
     * @param values the array to calculate the mean from
     * @return the arithmetic mean of the given double array
     */
    private double mean(final double[] values) {
        double sum = 0;
        for (double d : values) {
            sum += d;
        }
        return sum / values.length;
    }
}