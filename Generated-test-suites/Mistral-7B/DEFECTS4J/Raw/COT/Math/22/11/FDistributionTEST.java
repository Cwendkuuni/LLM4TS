package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for FDistribution class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws NotStrictlyPositiveException if the degrees of freedom are not strictly positive
     */
    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
        assertEquals(Double.NaN, dist.getNumericalMean(), 0.0);
        assertEquals(Double.NaN, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test density method for FDistribution.
     */
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double density = dist.density(0.5);
        assertEquals(0.03972590264226263, density, 0.0000001);

        density = dist.density(1.0);
        assertEquals(0.11036207223693675, density, 0.0000001);

        density = dist.density(2.0);
        assertEquals(0.2858718264179742, density, 0.0000001);

        density = dist.density(10.0);
        assertEquals(0.1238299663921061, density, 0.0000001);
    }

    /**
     * Test cumulativeProbability method for FDistribution.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        double cdf = dist.cumulativeProbability(0.0);
        assertEquals(0.0, cdf, 0.0);

        cdf = dist.cumulativeProbability(0.1);
        assertEquals(0.02112730843318725, cdf, 0.0000001);

        cdf = dist.cumulativeProbability(0.5);
        assertEquals(0.5, cdf, 0.0);

        cdf = dist.cumulativeProbability(1.0);
        assertEquals(0.9788726915668128, cdf, 0.0000001);

        cdf = dist.cumulativeProbability(10.0);
        assertEquals(1.0, cdf, 0.0);
    }

    /**
     * Test getSupportLowerBound method for FDistribution.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method for FDistribution.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method for FDistribution.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method for FDistribution.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method for FDistribution.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test getNumericalMean method for FDistribution.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertEquals(5.5, dist.getNumericalMean(), 0.1);

        dist = new FDistribution(4.0, 4.0);
        assertEquals(3.0, dist.getNumericalMean(), 0.1);

        dist = new FDistribution(1.0, 1.0);
        assertEquals(Double.NaN, dist.getNumericalMean(), 0.0);
    }

    /**
     * Test getNumericalVariance method for FDistribution.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3.0, 5.0);
        assertEquals(12.06498575451356, dist.getNumericalVariance(), 0.1);

        dist = new FDistribution(4.0, 4.0);
        assertEquals(2.0, dist.getNumericalVariance(), 0.1);

        dist = new FDistribution(1.0, 1.0);
        assertEquals(Double.NaN, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-3.0, 5.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionZeroNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 5.0);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionZeroDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, 0.0);
    }
}