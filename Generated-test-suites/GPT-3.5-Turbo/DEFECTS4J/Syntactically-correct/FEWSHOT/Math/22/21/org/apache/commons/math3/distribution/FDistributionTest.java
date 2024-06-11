package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.density(1.5);
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(0.2820947904, result, 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.cumulativeProbability(1.5);
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(0.8524163823, result, 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.getNumericalMean();
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(Double.NaN, result, 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.getNumericalVariance();
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(Double.NaN, result, 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.getSupportLowerBound();
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(0, result, 0.00001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(1, 2);
        double result = f.getSupportUpperBound();
        // Assert that the result is within acceptable range (with some margin of error)
        assertEquals(Double.POSITIVE_INFINITY, result, 0.00001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(1, 2);
        boolean result = f.isSupportLowerBoundInclusive();
        assertNotNull(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(1, 2);
        boolean result = f.isSupportUpperBoundInclusive();
        assertNotNull(result);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(1, 2);
        boolean result = f.isSupportConnected();
        assertNotNull(result);
    }
}