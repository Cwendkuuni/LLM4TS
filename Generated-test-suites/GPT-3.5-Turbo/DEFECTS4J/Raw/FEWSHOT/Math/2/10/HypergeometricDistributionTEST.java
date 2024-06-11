
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {
   
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.1667, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.6667, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.6667, dist.probability(0), 0.0001);
        assertEquals(0.2667, dist.probability(1), 0.0001);
        assertEquals(0.0619, dist.probability(2), 0.0001);
        assertEquals(0.0067, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.3333, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.8333, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.9459, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
    
}
