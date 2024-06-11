package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test the construction of an F distribution with valid parameters.
     *
     * @throws NotStrictlyPositiveException if an exception is expected
     */
    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 5;
        final double denominatorDegreesOfFreedom = 7;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the construction of an F distribution with invalid parameters.
     *
     * @throws NotStrictlyPositiveException if an exception is expected
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidParameters() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 0;
        final double denominatorDegreesOfFreedom = 7;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Test the construction of an F distribution with a random number generator.
     *
     * @throws NotStrictlyPositiveException if an exception is expected
     */
    @Test
    public void testConstructionWithRandomGenerator() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 5;
        final double denominatorDegreesOfFreedom = 7;
        final Well19937c rng = new Well19937c();
        final FDistribution distribution = new FDistribution(rng, numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the density of an F distribution.
     */
    @Test
    public void testDensity() {
        final FDistribution distribution = new FDistribution(5, 7);

        final double x = 1.5;
        final double density = distribution.density(x);
        final double expectedDensity = 0.2393917259156285;
        assertEquals(expectedDensity, density, 1e-9);
    }

    /**
     * Test the inverse cumulative probability of an F distribution.
     */
    @Test
    public void testInverseCumulativeProbability() {
        final FDistribution distribution = new FDistribution(5, 7);

        final double p = 0.95;
        final double expectedX = 2.57128728479629;
        final double inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedX, inverseCumulativeProbability, 1e-9);
    }

    /**
     * Test the cumulative probability of an F distribution.
     */
    @Test
    public void testCumulativeProbability() {
        final FDistribution distribution = new FDistribution(5, 7);

        final double x = 1.5;
        final double cumulativeProbability = distribution.cumulativeProbability(x);
        final double expectedCumulativeProbability = 0.68748143111469;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-9);
    }

    /**
     * Test the support of an F distribution.
     */
    @Test
    public void testSupport() {
        final FDistribution distribution = new FDistribution(5, 7);

        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    /**
     * Test the numerical mean of an F distribution.
     */
    @Test
    public void testNumericalMean() {
        final FDistribution distribution = new FDistribution(5, 7);

        final double numericalMean = distribution.getNumericalMean();
        final double expectedNumericalMean = 3.11067571722068;
        assertEquals(expectedNumericalMean, numericalMean, 1e-9);
    }

    /**
     * Test the numerical variance of an F distribution.
     */
    @Test
    public void testNumericalVariance() {
        final FDistribution distribution = new FDistribution(5, 7);

        final double numericalVariance = distribution.getNumericalVariance();
        final double expectedNumericalVariance = 0.31651917183296;
        assertEquals(expectedNumericalVariance, numericalVariance, 1e-9);
    }
}