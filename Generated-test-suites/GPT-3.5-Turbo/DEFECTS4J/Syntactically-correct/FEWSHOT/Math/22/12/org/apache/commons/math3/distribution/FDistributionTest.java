package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {
    private FDistribution fDistribution;

    @Test
    public void testDensity() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.2419711010862045, fDistribution.density(1.0), 0.0000001);
    }

    @Test
    public void testCumulativeProbability() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.7135704379295262, fDistribution.cumulativeProbability(1.0), 0.0000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.00000010);
    }

    @Test
    public void testGetNumericalMean() {
        fDistribution = new FDistribution(2.0, 4.0);
        assertEquals(4.0, fDistribution.getNumericalMean(), 0.0000001);
    }

    @Test
    public void testGetNumericalVariance() {
        fDistribution = new FDistribution(2.0, 5.0);
        assertEquals(31.25, fDistribution.getNumericalVariance(), 0.0000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        fDistribution = new FDistribution(2.0, 3.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}