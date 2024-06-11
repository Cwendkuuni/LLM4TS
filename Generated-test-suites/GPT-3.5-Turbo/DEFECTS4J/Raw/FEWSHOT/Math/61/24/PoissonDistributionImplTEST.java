package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PoissonDistributionImplTest {
    
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.011, distribution.probability(2), 0.001);
        assertEquals(0.149, distribution.probability(4), 0.001);
        assertEquals(0.224, distribution.probability(5), 0.001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0.323, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.618, distribution.cumulativeProbability(4), 0.001);
        assertEquals(0.847, distribution.cumulativeProbability(6), 0.001);
    }
    
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.930, distribution.normalApproximateProbability(8), 0.001);
        assertEquals(0.996, distribution.normalApproximateProbability(14), 0.001);
        assertEquals(0.999, distribution.normalApproximateProbability(16), 0.001);
    }
    
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        int sample = distribution.sample();
        // cannot test the exact value, just make sure it is within range
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(0, distribution.getDomainLowerBound(0.95));
    }
    
    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.95));
    }
}