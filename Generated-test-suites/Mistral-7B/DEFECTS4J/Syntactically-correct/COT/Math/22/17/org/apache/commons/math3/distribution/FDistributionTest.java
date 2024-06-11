package org.apache.commons.math3.distribution;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for FDistribution.
 */
public class FDistributionTest {
    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testFDistributionValidConstruction() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid parameters.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidConstruction() {
        new FDistribution(-1.0, 3.0);
    }

    /**
     * Test constructor with random number generator.
     */
    @Test
    public void testFDistributionRandomNumberGenerator() {
        RandomGenerator rng = new Well19937c();
        FDistribution dist = new FDistribution(rng, 2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(rng, dist.getRandomGenerator());
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.1221353772753341, dist.density(1.0), 1e-6);
    }

    /**
     * Test cumulativeProbability method with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityValidParameters() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.1280378324545273, dist.cumulativeProbability(1.0), 1e-6);
    }

    /**
     * Test cumulativeProbability method with invalid parameter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidParameter() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        dist.cumulativeProbability(-1.0);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(2.5, 3.0);
        assertEquals(3.0 / 1.5, dist.getNumericalMean(), 1e-6);

        dist = new FDistribution(1.0, 3.0);
        assertNotNull(dist.getNumericalMean());
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(2.5, 3.0);
        double variance = dist.getNumericalVariance();
        assertEquals(2.425494929248119, variance, 1e-6);

        dist = new FDistribution(1.0, 3.0);
        assertNotNull(dist.getNumericalVariance());
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 1e-6);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-6);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test regularizedBeta method from SpecialFunctions for cumulativeProbability method.
     */
    @Test
    public void testSpecialFunctionsRegularizedBeta() {
        double x = 1.0;
        double a = 2.0;
        double b = 3.0;
        double expectedResult = 0.1280378324545273;

        double result = SpecialFunctions.betaRegularized(a * (x) / (b + a * x), 0.5 * a, 0.5 * b);

        assertEquals(expectedResult, result, 1e-6);
    }
}