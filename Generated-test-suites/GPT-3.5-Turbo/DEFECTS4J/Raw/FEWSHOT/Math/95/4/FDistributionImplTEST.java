package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        assertEquals(0.000, fDist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.500, fDist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.999, fDist.cumulativeProbability(2.0), 0.001);
        
        fDist.setNumeratorDegreesOfFreedom(10);
        fDist.setDenominatorDegreesOfFreedom(20);
        
        assertEquals(0.000, fDist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.500, fDist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.999, fDist.cumulativeProbability(2.0), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1.0), 0.001);
        assertEquals(0.500, fDist.inverseCumulativeProbability(0.5), 0.001);
        
        fDist.setNumeratorDegreesOfFreedom(10);
        fDist.setDenominatorDegreesOfFreedom(20);
        
        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1.0), 0.001);
        assertEquals(0.500, fDist.inverseCumulativeProbability(0.5), 0.001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        assertEquals(0.0, fDist.getDomainLowerBound(0.0), 0.001);
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 0.001);
        assertEquals(0.0, fDist.getDomainLowerBound(1.0), 0.001);

        fDist.setNumeratorDegreesOfFreedom(10);
        fDist.setDenominatorDegreesOfFreedom(20);
        
        assertEquals(0.0, fDist.getDomainLowerBound(0.0), 0.001);
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 0.001);
        assertEquals(0.0, fDist.getDomainLowerBound(1.0), 0.001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.0), 0.001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 0.001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(1.0), 0.001);

        fDist.setNumeratorDegreesOfFreedom(10);
        fDist.setDenominatorDegreesOfFreedom(20);
        
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.0), 0.001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 0.001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(1.0), 0.001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        assertEquals(5 / 3.0, fDist.getInitialDomain(0.0), 0.001);
        assertEquals(5 / 3.0, fDist.getInitialDomain(0.5), 0.001);
        assertEquals(5 / 3.0, fDist.getInitialDomain(1.0), 0.001);

        fDist.setNumeratorDegreesOfFreedom(10);
        fDist.setDenominatorDegreesOfFreedom(20);
        
        assertEquals(10 / 8.0, fDist.getInitialDomain(0.0), 0.001);
        assertEquals(10 / 8.0, fDist.getInitialDomain(0.5), 0.001);
        assertEquals(10 / 8.0, fDist.getInitialDomain(1.0), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        fDist.setNumeratorDegreesOfFreedom(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(5, 10);
        
        fDist.setDenominatorDegreesOfFreedom(-10);
    }
}