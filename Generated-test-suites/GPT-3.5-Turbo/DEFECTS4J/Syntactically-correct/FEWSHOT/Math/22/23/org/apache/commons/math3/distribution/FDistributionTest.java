package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FDistributionTest {
    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(0.071428571, dist.density(1.0), 1e-9);
        assertEquals(0.127551020, dist.density(2.5), 1e-9);
        assertEquals(0.114285714, dist.density(5.0), 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(0.255089820, dist.cumulativeProbability(1.0), 1e-9);
        assertEquals(0.673139358, dist.cumulativeProbability(2.5), 1e-9);
        assertEquals(0.900648978, dist.cumulativeProbability(5.0), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(3, dist.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(4, dist.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(1.333333333, dist.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(4.5, dist.getNumericalVariance(), 1e-9);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(0, dist.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(false, dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(true, dist.isSupportConnected());
    }
}