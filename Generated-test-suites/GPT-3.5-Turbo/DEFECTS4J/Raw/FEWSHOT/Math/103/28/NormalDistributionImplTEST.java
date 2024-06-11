package org.apache.commons.math.distribution;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.000001);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.000001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.000001);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        normalDistribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = normalDistribution.cumulativeProbability(0.0);
        assertEquals(0.5, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithPZero() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.000001);
    }

    @Test
    public void testInverseCumulativeProbabilityWithPOne() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.4);
        assertEquals(-Double.MAX_VALUE, result, 0.000001);
    }

    @Test
    public void testGetDomainLowerBoundWithPEqualsToHalf() {
        double result = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.000001);
    }

    @Test
    public void testGetDomainUpperBoundWithPEqualsToHalf() {
        double result = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.4);
        assertEquals(-1.0, result, 0.000001);
    }

    @Test
    public void testGetInitialDomainWithPEqualsToHalf() {
        double result = normalDistribution.getInitialDomain(0.5);
        assertEquals(0.0, result, 0.000001);
    }
}