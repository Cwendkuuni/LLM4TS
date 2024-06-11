package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {
    private final double DELTA = 1e-9;

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(0.5, f.density(1), DELTA);

        f = new FDistribution(2, 2);
        assertEquals(0.5, f.density(1), DELTA);

        f = new FDistribution(2, 3);
        assertEquals(0.318310, f.density(1), DELTA);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(0.5, f.cumulativeProbability(1), DELTA);

        f = new FDistribution(2, 2);
        assertEquals(0.5, f.cumulativeProbability(1), DELTA);

        f = new FDistribution(2, 3);
        assertEquals(0.572408, f.cumulativeProbability(1), DELTA);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(1, f.getNumeratorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(1, f.getDenominatorDegreesOfFreedom(), DELTA);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(3, 4);
        assertEquals(1.6, f.getNumericalMean(), DELTA);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(5, 6);
        assertEquals(175.714286, f.getNumericalVariance(), DELTA);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(0, f.getSupportLowerBound(), DELTA);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(1, 1);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), DELTA);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(1, 1);
        assertTrue(f.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(1, 1);
        assertTrue(!f.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(1, 1);
        assertTrue(f.isSupportConnected());
    }
}