
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.0002, fd.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.0324, fd.cumulativeProbability(2.0), 0.0001);
        assertEquals(0.0951, fd.cumulativeProbability(3.0), 0.0001);
        assertEquals(0.1808, fd.cumulativeProbability(4.0), 0.0001);
        assertEquals(0.2920, fd.cumulativeProbability(5.0), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.3333, fd.inverseCumulativeProbability(0.1), 0.0001);
        assertEquals(0.5260, fd.inverseCumulativeProbability(0.2), 0.0001);
        assertEquals(0.7345, fd.inverseCumulativeProbability(0.3), 0.0001);
        assertEquals(0.9893, fd.inverseCumulativeProbability(0.4), 0.0001);
        assertEquals(1.4636, fd.inverseCumulativeProbability(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.0, fd.getDomainLowerBound(0.1), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.3), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.4), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.5), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.1), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.3), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.4), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.5), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(2.0, fd.getInitialDomain(0.1), 0.0001);
        assertEquals(2.0, fd.getInitialDomain(0.2), 0.0001);
        assertEquals(2.0, fd.getInitialDomain(0.3), 0.0001);
        assertEquals(2.0, fd.getInitialDomain(0.4), 0.0001);
        assertEquals(2.0, fd.getInitialDomain(0.5), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setNumeratorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setDenominatorDegreesOfFreedom(0.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}
