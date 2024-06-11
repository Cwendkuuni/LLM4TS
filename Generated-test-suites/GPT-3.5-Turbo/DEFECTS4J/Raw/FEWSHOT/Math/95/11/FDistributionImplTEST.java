package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.0134529, fd.cumulativeProbability(1), 0.000001);
        assertEquals(0.0801494, fd.cumulativeProbability(2), 0.000001);
        assertEquals(0.357977, fd.cumulativeProbability(3), 0.000001);
        assertEquals(0.623263, fd.cumulativeProbability(4), 0.000001);
        assertEquals(0.82898, fd.cumulativeProbability(5), 0.000001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.0874643, fd.inverseCumulativeProbability(0.1), 0.000001);
        assertEquals(0.151676, fd.inverseCumulativeProbability(0.2), 0.000001);
        assertEquals(0.279349, fd.inverseCumulativeProbability(0.3), 0.000001);
        assertEquals(0.458039, fd.inverseCumulativeProbability(0.4), 0.000001);
        assertEquals(0.809087, fd.inverseCumulativeProbability(0.6), 0.000001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(0.0, fd.getDomainLowerBound(0.1), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.3), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.4), 0.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.6), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.1), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.3), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.4), 0.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.6), 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        assertEquals(1.3333333333333333, fd.getInitialDomain(0.1), 0.000001);
        assertEquals(1.3333333333333333, fd.getInitialDomain(0.2), 0.000001);
        assertEquals(1.3333333333333333, fd.getInitialDomain(0.3), 0.000001);
        assertEquals(1.3333333333333333, fd.getInitialDomain(0.4), 0.000001);
        assertEquals(1.3333333333333333, fd.getInitialDomain(0.6), 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setNumeratorDegreesOfFreedom(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        FDistributionImpl fd = new FDistributionImpl(3, 4);
        fd.setDenominatorDegreesOfFreedom(-1);
    }
}