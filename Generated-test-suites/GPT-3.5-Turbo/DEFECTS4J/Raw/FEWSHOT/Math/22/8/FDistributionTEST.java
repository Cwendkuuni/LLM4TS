package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class FDistributionTest {
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0.0625, fd.density(1.0), 0.0001);
        assertEquals(0.016, fd.density(2.0), 0.0001);
        assertEquals(0.0, fd.density(0.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0.5714, fd.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.8636, fd.cumulativeProbability(2.0), 0.0001);
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(3, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(5, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(3.3333, fd.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(42.8571, fd.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0, fd.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(3, 5);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(3, 5);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(3, 5);
        assertTrue(fd.isSupportConnected());
    }
}