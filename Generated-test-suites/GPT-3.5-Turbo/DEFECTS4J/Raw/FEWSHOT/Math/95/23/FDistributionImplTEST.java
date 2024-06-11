package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result1 = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.134023, result1, 0.000001);
        
        double result2 = fDistribution.cumulativeProbability(1.0);
        assertEquals(0.196647, result2, 0.000001);
        
        double result3 = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.307878, result3, 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result1 = fDistribution.inverseCumulativeProbability(0.2);
        assertEquals(0.415623, result1, 0.000001);
        
        double result2 = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.953451, result2, 0.000001);
        
        double result3 = fDistribution.inverseCumulativeProbability(0.8);
        assertEquals(1.663037, result3, 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getDomainLowerBound(0.2);
        assertEquals(0.0, result, 0.000001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getDomainUpperBound(0.2);
        assertEquals(Double.MAX_VALUE, result, 0.000001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getInitialDomain(0.2);
        assertEquals(0.666667, result, 0.000001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setNumeratorDegreesOfFreedom(7);
        
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(7, result, 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithZero() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, result, 0.000001);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setDenominatorDegreesOfFreedom(15);
        
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(15, result, 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithZero() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, result, 0.000001);
    }

}