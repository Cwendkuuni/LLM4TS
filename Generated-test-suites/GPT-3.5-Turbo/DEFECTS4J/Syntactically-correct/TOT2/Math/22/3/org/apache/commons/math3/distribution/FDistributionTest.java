package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.density(0.0), 0.0);
        assertEquals(0.01638141021464557, fDistribution.density(1.0), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0);
        assertEquals(0.3846153846153846, fDistribution.cumulativeProbability(1.0), 1e-10);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(1.5, fDistribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(4.0, 6.0);
        assertEquals(9.6, fDistribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(2.0, 3.0);
        assertTrue(fDistribution.isSupportConnected());
    }

}