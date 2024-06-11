package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl distribution;
    
    @Test
    public void testGetMean() {
        distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);
        
        distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
    }
    
    @Test
    public void testSetMean() {
        distribution = new NormalDistributionImpl();
        distribution.setMean(2.0);
        assertEquals(2.0, distribution.getMean(), 0.001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
        
        distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegativeValue() {
        distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-2.0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(0.158, distribution.cumulativeProbability(-1.0), 0.001);
        assertEquals(0.691, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.977, distribution.cumulativeProbability(2.0), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl();
        assertEquals(-1.281, distribution.inverseCumulativeProbability(0.1), 0.001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.281, distribution.inverseCumulativeProbability(0.9), 0.001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        distribution = new NormalDistributionImpl();
        assertEquals(Double.NEGATIVE_INFINITY, distribution.getDomainLowerBound(0.0), 0.001);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getDomainUpperBound(0.0), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getDomainUpperBound(0.5), 0.001);
    }
    
    @Test
    public void testGetInitialDomain() {
        distribution = new NormalDistributionImpl();
        assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.001);
        assertEquals(1.0, distribution.getInitialDomain(1.0), 0.001);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.001);
    }
}