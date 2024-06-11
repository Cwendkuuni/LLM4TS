package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        assertEquals(0.001288925492437, fd.cumulativeProbability(1.0), 1e-15);
        assertEquals(0.166858532727646, fd.cumulativeProbability(2.0), 1e-15);
        assertEquals(0.500000000000000, fd.cumulativeProbability(3.0), 1e-15);
        assertEquals(0.833141467272354, fd.cumulativeProbability(4.0), 1e-15);
        assertEquals(0.998711074507563, fd.cumulativeProbability(5.0), 1e-15);
        assertEquals(0.255706132098159, fd.cumulativeProbability(0.5), 1e-15);
        assertEquals(0.791281063255642, fd.cumulativeProbability(1.5), 1e-15);
        assertEquals(0.953046270894573, fd.cumulativeProbability(2.5), 1e-15);
        assertEquals(0.995976057415527, fd.cumulativeProbability(3.5), 1e-15);
        assertEquals(0.999643723920762, fd.cumulativeProbability(4.5), 1e-15);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        assertEquals(0.417729377483760, fd.inverseCumulativeProbability(0.1), 1e-15);
        assertEquals(0.625328766300278, fd.inverseCumulativeProbability(0.2), 1e-15);
        assertEquals(0.808132693251180, fd.inverseCumulativeProbability(0.3), 1e-15);
        assertEquals(1.031496247265057, fd.inverseCumulativeProbability(0.4), 1e-15);
        assertEquals(1.386368820948018, fd.inverseCumulativeProbability(0.5), 1e-15);
        assertEquals(2.080811262442006, fd.inverseCumulativeProbability(0.6), 1e-15);
        assertEquals(2.945073452042355, fd.inverseCumulativeProbability(0.7), 1e-15);
        assertEquals(4.244163227346274, fd.inverseCumulativeProbability(0.8), 1e-15);
        assertEquals(6.400705347754956, fd.inverseCumulativeProbability(0.9), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        assertEquals(0.0, fd.getDomainLowerBound(0.1), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.3), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.4), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.5), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.6), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.7), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.8), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.9), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(1.0), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.1), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.3), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.4), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.6), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.7), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.8), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.9), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(1.0), 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        assertEquals(2.0, fd.getInitialDomain(0.1), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.2), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.3), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.4), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.5), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.6), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.7), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.8), 0.0);
        assertEquals(2.0, fd.getInitialDomain(0.9), 0.0);
        assertEquals(2.0, fd.getInitialDomain(1.0), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_Negative() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        fd.setNumeratorDegreesOfFreedom(-4);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom_Positive() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        fd.setNumeratorDegreesOfFreedom(4);
        
        assertEquals(4, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_Negative() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        fd.setDenominatorDegreesOfFreedom(-4);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom_Positive() {
        FDistributionImpl fd = new FDistributionImpl(6, 9);
        
        fd.setDenominatorDegreesOfFreedom(4);
        
        assertEquals(4, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
}