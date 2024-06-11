package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for the {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Tests the constructor with valid positive degrees of freedom.
     *
     * @throws Exception if the constructor throws an exception.
     */
    @Test
    public void testValidConstruction() throws Exception {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 5.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                             denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the constructor with invalid negative degrees of freedom.
     *
     * @throws Exception if the constructor is not thrown an exception.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() throws Exception {
        final double numeratorDegreesOfFreedom = -2.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Tests the density method with valid arguments.
     */
    @Test
    public void testDensity() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double x = 2.0;
        final double expectedDensity = 0.03364375;
        assertEquals(expectedDensity, distribution.density(x), 0.0);
    }

    /**
     * Tests the cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double x = 2.0;
        final double expectedCumProb = 0.1560458083513385;
        assertEquals(expectedCumProb, distribution.cumulativeProbability(x), 0.0);
    }

    /**
     * Tests the getNumericalMean method with valid degrees of freedom.
     */
    @Test
    public void testGetNumericalMean() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double expectedMean = 3.0 / 2.0;
        assertEquals(expectedMean, distribution.getNumericalMean(), 0.0);
    }

    /**
     * Tests the getNumericalVariance method with valid degrees of freedom.
     */
    @Test
    public void testGetNumericalVariance() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double expectedVariance = 0.3688311395635552;
        assertEquals(expectedVariance, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Tests the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double expectedLowerBound = 0.0;
        assertEquals(expectedLowerBound, distribution.getSupportLowerBound(), 0.0);
    }

    /**
     * Tests the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final double expectedUpperBound = Double.POSITIVE_INFINITY;
        assertEquals(expectedUpperBound, distribution.getSupportUpperBound(), 0.0);
    }

    /**
     * Tests the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final boolean expected = true;
        assertTrue(expected == distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Tests the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final boolean expected = false;
        assertTrue(expected == distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Tests the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final FDistribution distribution = new FDistribution(3.0, 5.0);
        final boolean expected = true;
        assertTrue(expected == distribution.isSupportConnected());
    }

    /**
     * Tests the distribution with random generator.
     *
     * @throws Exception if the test fails.
     */
    @Test
    public void testRandomGeneration() throws Exception {
        final int seed = 123;
        final FDistribution distribution = new FDistribution(new Well19937c(seed),
                                                           3.0, 5.0);

        final double mean = distribution.getNumericalMean();
        final int numberOfTests = 100_000;
        double sum = 0.0;
        for (int i = 0; i < numberOfTests; i++) {
            sum += distribution.inverseCumulativeProbability(
                    FastMath.nextGaussian(distribution.getRng()));
        }
        final double average = sum / numberOfTests;
        final double tolerance = 1e-6;
        assertTrue(Math.abs(average - mean) < tolerance);
    }
}