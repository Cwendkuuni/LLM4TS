package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(0.020008, fd.density(0.1), 0.000001);
        assertEquals(0.113799, fd.density(1), 0.000001);
        assertEquals(0.195216, fd.density(2), 0.000001);
        assertEquals(0.119154, fd.density(3), 0.000001);
        assertEquals(0.0845107, fd.density(4), 0.000001);
        assertEquals(0.0681506, fd.density(5), 0.000001);
        assertEquals(0.0583524, fd.density(6), 0.000001);
        assertEquals(0.0525832, fd.density(7), 0.000001);
        assertEquals(0.0490195, fd.density(8), 0.000001);
        assertEquals(0.046093, fd.density(9), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(0, fd.cumulativeProbability(0), 0);
        assertEquals(0.168269, fd.cumulativeProbability(1), 0.000001);
        assertEquals(0.362696, fd.cumulativeProbability(2), 0.000001);
        assertEquals(0.536526, fd.cumulativeProbability(3), 0.000001);
        assertEquals(0.659066, fd.cumulativeProbability(4), 0.000001);
        assertEquals(0.746424, fd.cumulativeProbability(5), 0.000001);
        assertEquals(0.809117, fd.cumulativeProbability(6), 0.000001);
        assertEquals(0.853767, fd.cumulativeProbability(7), 0.000001);
        assertEquals(0.884075, fd.cumulativeProbability(8), 0.000001);
        assertEquals(0.903043, fd.cumulativeProbability(9), 0.000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(5, fd.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(6, fd.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(1.5, fd.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(18, fd.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(0, fd.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(true, fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(false, fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(5, 6);
        assertEquals(true, fd.isSupportConnected());
    }
}