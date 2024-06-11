
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testConstructor() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0);
        assertEquals(5, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidNumerator() {
        FDistributionImpl fDistribution = new FDistributionImpl(0, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidDenominator() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 0);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(0.18039, fDistribution.cumulativeProbability(1), 0.00001);
        assertEquals(0, fDistribution.cumulativeProbability(0), 0);
        assertEquals(1, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(0, fDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(0, fDistribution.getDomainLowerBound(0), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(1.666666, fDistribution.getInitialDomain(0), 0.000001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setNumeratorDegreesOfFreedom(6);
        assertEquals(6, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setNumeratorDegreesOfFreedom(0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        assertEquals(5, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setDenominatorDegreesOfFreedom(10);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithInvalidValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 5);
        fDistribution.setDenominatorDegreesOfFreedom(0);
    }
}
