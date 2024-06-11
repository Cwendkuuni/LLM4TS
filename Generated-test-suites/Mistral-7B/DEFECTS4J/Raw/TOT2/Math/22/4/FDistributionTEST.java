package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Tests for {@link FDistribution} class.
 */
public class FDistributionTest {

    /**
     * Test the constructor.
     * @throws NotStrictlyPositiveException if degrees of freedom are not positive
     */
    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double x = 1.5;
        final double expectedResult = 0.2349812155037145;
        final double result = distribution.density(x);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double x = 1.5;
        final double expectedResult = 0.89287220165264;
        final double result = distribution.cumulativeProbability(x);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        final double numeratorDegreesOfFreedom = 10.0;
        final double denominatorDegreesOfFreedom = 2.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double expectedResult = 5.0;
        final double result = distribution.getNumericalMean();
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        final double numeratorDegreesOfFreedom = 15.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double expectedResult = 5.354274680983246;
        final double result = distribution.getNumericalVariance();
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double lowerBound = 0.0;
        final double result = distribution.getSupportLowerBound();
        assertEquals(lowerBound, result, 1e-16);
    }

    /**
     * Test the isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final boolean expectedResult = true;
        final boolean result = distribution.isSupportLowerBoundInclusive();
        assertTrue(expectedResult == result);
    }

    /**
     * Test the getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final double upperBound = Double.POSITIVE_INFINITY;
        final double result = distribution.getSupportUpperBound();
        assertEquals(upperBound, result, 1e-16);
    }

    /**
     * Test the isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final boolean expectedResult = false;
        final boolean result = distribution.isSupportUpperBoundInclusive();
        assertTrue(expectedResult == result);
    }

    /**
     * Test the isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        final double numeratorDegreesOfFreedom = 5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        final FDistribution distribution = new FDistribution(numeratorDegreesOfFreedom,
                                                            denominatorDegreesOfFreedom);
        final boolean expectedResult = true;
        final boolean result = distribution.isSupportConnected();
        assertTrue(expectedResult == result);
    }

    /**
     * Test the throw exception when constructor called with negative degrees of freedom.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = -5.0;
        final double denominatorDegreesOfFreedom = 3.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
}