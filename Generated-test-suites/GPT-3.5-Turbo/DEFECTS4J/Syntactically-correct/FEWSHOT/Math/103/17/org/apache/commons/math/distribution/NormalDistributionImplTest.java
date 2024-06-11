package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl distribution;
    
    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }
    
    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        distribution.setMean(1.5);
        assertEquals(1.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.1587, distribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(0.8413, distribution.cumulativeProbability(1.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        assertTrue(Double.isInfinite(distribution.getDomainLowerBound(0.0)));
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, distribution.getDomainUpperBound(0.5), 0.0001);
        assertTrue(Double.isInfinite(distribution.getDomainUpperBound(1.0)));
    }
    
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, distribution.getInitialDomain(0.1), 0.0001);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0001);
        assertEquals(1.0, distribution.getInitialDomain(0.9), 0.0001);
    }
}