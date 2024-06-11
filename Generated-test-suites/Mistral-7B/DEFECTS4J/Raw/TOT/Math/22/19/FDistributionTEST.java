package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link FDistribution}.
 */
public class FDistributionTest {

    private FDistribution dist;
    private Well19937c rng;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        rng = new Well19937c();
        dist = new FDistribution(rng, 3, 5);
    }

    /**
     * Test method for {@link FDistribution#FDistribution(double, double)} .
     * Tests the case where both numerator and denominator degrees of freedom are positive.
     */
    @Test
    public void testFDistribution() {
        Assert.assertEquals(1.0, dist.density(1.0), 1e-10);
        Assert.assertEquals(0.0, dist.density(0.5), 1e-10);
        Assert.assertEquals(1.158887845192925, dist.cumulativeProbability(2.25), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#FDistribution(double, double)} .
     * Tests the case where the numerator degree of freedom is zero.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreeOfFreedom() {
        new FDistribution(rng, 0, 5);
    }

    /**
     * Test method for {@link FDistribution#FDistribution(double, double)} .
     * Tests the case where the denominator degree of freedom is zero.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreeOfFreedom() {
        new FDistribution(rng, 3, 0);
    }

    /**
     * Test method for {@link FDistribution#density(double)}.
     * Tests the case where x is negative.
     */
    @Test
    public void testDensityNegativeX() {
        Assert.assertEquals(0.0, dist.density(-1.0), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#density(double)}.
     * Tests the case where x is close to the lower bound (0).
     */
    @Test
    public void testDensityCloseToLowerBound() {
        Assert.assertEquals(1.268474888394775e-12, dist.density(1e-12), 1e-12);
    }

    /**
     * Test method for {@link FDistribution#density(double)}.
     * Tests the case where x is large.
     */
    @Test
    public void testDensityLargeX() {
        Assert.assertEquals(1.714523586949322e-16, dist.density(1e10), 1e-16);
    }

    /**
     * Test method for {@link FDistribution#cumulativeProbability(double)}.
     * Tests the case where x is negative.
     */
    @Test
    public void testCumulativeProbabilityNegativeX() {
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1.0), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#cumulativeProbability(double)}.
     * Tests the case where x is close to the lower bound (0).
     */
    @Test
    public void testCumulativeProbabilityCloseToLowerBound() {
        Assert.assertEquals(1.261357159849426e-12, dist.cumulativeProbability(1e-12), 1e-12);
    }

    /**
     * Test method for {@link FDistribution#cumulativeProbability(double)}.
     * Tests the case where x is large.
     */
    @Test
    public void testCumulativeProbabilityLargeX() {
        Assert.assertEquals(1.0, dist.cumulativeProbability(1e10), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getNumeratorDegreesOfFreedom()}.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        Assert.assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getDenominatorDegreesOfFreedom()}.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        Assert.assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getNumericalMean()}.
     */
    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(3.5, dist.getNumericalMean(), 1e-10);
    }

    /**
     * Test method for {@link FDistribution#getNumericalVariance()}.
     */
    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(3.284949458133428, dist.getNumericalVariance(), 1e-10);
    }
}