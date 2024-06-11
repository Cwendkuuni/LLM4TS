
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(0.406198, fDistribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.561481, fDistribution.cumulativeProbability(1.0), 0.0001);
        assertEquals(0.722285, fDistribution.cumulativeProbability(2.0), 0.0001);
        assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(1.0, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.0), 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.0), 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        assertEquals(1.333333, fDistribution.getInitialDomain(0.0), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomIllegalArgumentException() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        fDistribution.setNumeratorDegreesOfFreedom(-1.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomIllegalArgumentException() {
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 4.0);
        
        fDistribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}
