
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(0.059737, fDist.cumulativeProbability(0.5), 0.000001);
        assertEquals(0.228906, fDist.cumulativeProbability(1.5), 0.000001);
        assertEquals(0.497693, fDist.cumulativeProbability(2.5), 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(0.007336, fDist.inverseCumulativeProbability(0.1), 0.000001);
        assertEquals(0.030169, fDist.inverseCumulativeProbability(0.3), 0.000001);
        assertEquals(0.082041, fDist.inverseCumulativeProbability(0.5), 0.000001);
        assertEquals(1.38834, fDist.inverseCumulativeProbability(0.9), 0.00001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 0.000001);
        assertEquals(0.0, fDist.getDomainLowerBound(0.8), 0.000001);
        assertEquals(0.0, fDist.getDomainLowerBound(1.0), 0.000001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 0.000001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.8), 0.000001);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(1.0), 0.000001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(0.6, fDist.getInitialDomain(0.5), 0.000001);
        assertEquals(0.6, fDist.getInitialDomain(0.8), 0.000001);
        assertEquals(0.6, fDist.getInitialDomain(1.0), 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        fDist.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(10.0, fDist.getNumeratorDegreesOfFreedom(), 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        fDist.setDenominatorDegreesOfFreedom(-5.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(10, 15);
        
        assertEquals(15.0, fDist.getDenominatorDegreesOfFreedom(), 0.000001);
    }
}
