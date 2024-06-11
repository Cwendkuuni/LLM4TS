package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;

    @Before
    public void initialize() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1);
        assertEquals(1, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        normalDistribution.setStandardDeviation(-1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
        assertEquals(0.022750131948179195, normalDistribution.cumulativeProbability(-2), 1e-15);
        assertEquals(0.9772498680518208, normalDistribution.cumulativeProbability(2), 1e-15);
        assertEquals(0.9999997133484281, normalDistribution.cumulativeProbability(5), 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(-2.3263478740408408, normalDistribution.inverseCumulativeProbability(0.01), 1e-15);
        assertEquals(0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.75), 1e-15);
        assertEquals(1.959963984540054, normalDistribution.inverseCumulativeProbability(0.975), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidP() throws MathException {
        normalDistribution.inverseCumulativeProbability(-0.5);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.5), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.75), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDistribution.getDomainUpperBound(0.25), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.5), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.25), 0);
        assertEquals(0, normalDistribution.getInitialDomain(0.5), 0);
        assertEquals(1, normalDistribution.getInitialDomain(0.75), 0);
    }
}