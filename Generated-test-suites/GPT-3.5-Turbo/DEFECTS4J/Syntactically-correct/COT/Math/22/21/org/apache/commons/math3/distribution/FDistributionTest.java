package org.apache.commons.math3.distribution;

import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    // Test constructor
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistribution(1, 2);
        Assert.assertNotNull(distribution);
    }

    // Test density
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(1, 2);
        double density = distribution.density(0.5);
        Assert.assertEquals(0.464780274, density, 1e-9);
    }

    // Test cumulativeProbability
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(1, 2);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.437500000, cumulativeProbability, 1e-9);
    }

    // Test getNumeratorDegreesOfFreedom
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(1, 2);
        double numeratorDegreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(1, numeratorDegreesOfFreedom, 1e-9);
    }

    // Test getDenominatorDegreesOfFreedom
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(1, 2);
        double denominatorDegreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(2, denominatorDegreesOfFreedom, 1e-9);
    }

    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(1, 2);
        double numericalMean = distribution.getNumericalMean();
        Assert.assertEquals(4.0, numericalMean, 1e-9);
    }

    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(1, 2);
        double numericalVariance = distribution.getNumericalVariance();
        Assert.assertEquals(Double.NaN, numericalVariance, 1e-9);
    }

    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(1, 2);
        double supportLowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, supportLowerBound, 1e-9);
    }

    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(1, 2);
        double supportUpperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 1e-9);
    }

    // Test isSupportLowerBoundInclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(1, 2);
        boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, isSupportLowerBoundInclusive);
    }

    // Test isSupportUpperBoundInclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(1, 2);
        boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, isSupportUpperBoundInclusive);
    }

    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(1, 2);
        boolean isSupportConnected = distribution.isSupportConnected();
        Assert.assertEquals(true, isSupportConnected);
    }

    // Test exception
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        FDistribution distribution = new FDistribution(0, 2);
    }

}