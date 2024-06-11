package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the FDistribution class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        Assert.assertNotNull(distribution);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidArguments() {
        FDistribution distribution = new FDistribution(-1.0, 5.0);
    }

    /**
     * Test constructor with inverse cumulative probability accuracy.
     */
    @Test
    public void testConstructorWithInverseCumulativeProbabilityAccuracy() {
        FDistribution distribution = new FDistribution(3.0, 5.0, 0.001);
        Assert.assertNotNull(distribution);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double density = distribution.density(1.5);
        Assert.assertNotNull(density);
        Assert.assertEquals(0.06288825, density, 0.000001);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double cumulativeProbability = distribution.cumulativeProbability(1.0);
        Assert.assertEquals(0.11165355, cumulativeProbability, 0.000001);
        cumulativeProbability = distribution.cumulativeProbability(5.0);
        Assert.assertEquals(1.0, cumulativeProbability);
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(3.0, numeratorDegreesOfFreedom);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(5.0, denominatorDegreesOfFreedom);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(3.125, numericalMean, 0.0001);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(1.79288125, numericalVariance, 0.0001);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, supportLowerBound);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean supportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(supportLowerBoundInclusive);
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean supportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(supportUpperBoundInclusive);
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        boolean supportConnected = distribution.isSupportConnected();
        Assert.assertTrue(supportConnected);
    }
}