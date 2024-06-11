
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class FDistributionImplTest {
    
    private static final double DELTA = 1e-9;
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        assertEquals(0.01823811, distribution.cumulativeProbability(1), DELTA);
        assertEquals(0.1475836, distribution.cumulativeProbability(2), DELTA);
        assertEquals(0.1520273, distribution.cumulativeProbability(3), DELTA);
        assertEquals(0.3293081, distribution.cumulativeProbability(4), DELTA);
        assertEquals(0.6879682, distribution.cumulativeProbability(5), DELTA);
        assertEquals(0.7998915, distribution.cumulativeProbability(6), DELTA);
    }
    
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalid() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        distribution.cumulativeProbability(-1);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(0.4179807, distribution.inverseCumulativeProbability(0.1), DELTA);
        assertEquals(0.707193, distribution.inverseCumulativeProbability(0.25), DELTA);
        assertEquals(1, distribution.inverseCumulativeProbability(0.5), DELTA);
        assertEquals(1.55, distribution.inverseCumulativeProbability(0.75), DELTA);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), DELTA);
    }
    
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalid() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        distribution.inverseCumulativeProbability(-1);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        assertEquals(0, distribution.getDomainLowerBound(0), DELTA);
        assertEquals(0, distribution.getDomainLowerBound(0.1), DELTA);
        assertEquals(0, distribution.getDomainLowerBound(0.5), DELTA);
        assertEquals(0, distribution.getDomainLowerBound(0.75), DELTA);
        assertEquals(0, distribution.getDomainLowerBound(1), DELTA);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0), DELTA);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.1), DELTA);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), DELTA);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), DELTA);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1), DELTA);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3, 6);
        assertEquals(1, distribution.getInitialDomain(0), DELTA);
        assertEquals(1, distribution.getInitialDomain(0.1), DELTA);
        assertEquals(1, distribution.getInitialDomain(0.5), DELTA);
        assertEquals(1, distribution.getInitialDomain(0.75), DELTA);
        assertEquals(1, distribution.getInitialDomain(1), DELTA);
    }
}
