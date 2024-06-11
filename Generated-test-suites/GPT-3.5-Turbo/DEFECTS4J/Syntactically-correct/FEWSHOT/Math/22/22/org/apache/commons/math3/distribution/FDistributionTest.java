package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {
    @Test
    public void testDensity() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double density = fdistribution.density(2.0);
        assertEquals(0.174533247, density, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double cumulativeProbability = fdistribution.cumulativeProbability(2.0);
        assertEquals(0.905934462, cumulativeProbability, 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double numeratorDegreesOfFreedom = fdistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, numeratorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double denominatorDegreesOfFreedom = fdistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, denominatorDegreesOfFreedom, 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double numericalMean = fdistribution.getNumericalMean();
        assertEquals(0.625, numericalMean, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double numericalVariance = fdistribution.getNumericalVariance();
        assertEquals(1.4270833333333332, numericalVariance, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double supportLowerBound = fdistribution.getSupportLowerBound();
        assertEquals(0.0, supportLowerBound, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fdistribution = new FDistribution(5, 10);
        double supportUpperBound = fdistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fdistribution = new FDistribution(5, 10);
        assertTrue(fdistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fdistribution = new FDistribution(5, 10);
        assertTrue(!fdistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fdistribution = new FDistribution(5, 10);
        assertTrue(fdistribution.isSupportConnected());
    }
}