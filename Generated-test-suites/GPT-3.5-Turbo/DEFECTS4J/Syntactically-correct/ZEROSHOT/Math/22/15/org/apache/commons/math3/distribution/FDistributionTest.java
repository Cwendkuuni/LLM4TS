package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.020690949229304736, fDistribution.density(0.1), 1e-9);
        assertEquals(0.00006858943431064291, fDistribution.density(1.0), 1e-9);
        assertEquals(0.003535642415992408, fDistribution.density(5.0), 1e-9);
        assertEquals(0.011098428779144644, fDistribution.density(10.0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 1e-9);
        assertEquals(0.09890766229613355, fDistribution.cumulativeProbability(0.1), 1e-9);
        assertEquals(0.20290194929542656, fDistribution.cumulativeProbability(0.5), 1e-9);
        assertEquals(0.5, fDistribution.cumulativeProbability(1.0), 1e-9);
        assertEquals(0.6434228074397932, fDistribution.cumulativeProbability(1.5), 1e-9);
        assertEquals(0.8954303923634311, fDistribution.cumulativeProbability(5.0), 1e-9);
        assertEquals(0.986798899493791, fDistribution.cumulativeProbability(10.0), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(10.0, fDistribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(1.25, fDistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(7.73160173160173, fDistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5.0, 10.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}