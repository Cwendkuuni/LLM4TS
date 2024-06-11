package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testConstructor(){
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetMean(){
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation(){
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue(){
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }
    
    @Test
    public void testCumulativeProbability(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.000001);
        assertEquals(0.841344746, distribution.cumulativeProbability(1.0), 0.000001);
        assertEquals(0.158655254, distribution.cumulativeProbability(-1.0), 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0);
    }
    
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(2.0);   
    }
    
    @Test
    public void testGetDomainLowerBound(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.1), 0.0);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getDomainUpperBound(0.1), 0.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0);
    }
    
    @Test
    public void testGetInitialDomain(){
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, distribution.getInitialDomain(0.1), 0.0);
        assertEquals(1.0, distribution.getInitialDomain(0.9), 0.0);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0);
    }
}