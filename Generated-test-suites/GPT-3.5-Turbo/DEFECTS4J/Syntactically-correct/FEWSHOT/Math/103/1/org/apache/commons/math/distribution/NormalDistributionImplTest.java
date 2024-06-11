package org.apache.commons.math.distribution;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(5);
        assertEquals(5, normalDistribution.getMean(), 0);
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
        assertEquals(0.9772498680518208, normalDistribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.022750131948179195, normalDistribution.cumulativeProbability(-2), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
        assertEquals(-0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.25), 0.0001);
        assertEquals(0.6744897501960817, normalDistribution.inverseCumulativeProbability(0.75), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.75), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0, normalDistribution.getDomainUpperBound(0.25), 0);
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.25), 0);
        assertEquals(1, normalDistribution.getInitialDomain(0.75), 0);
    }
}