package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, distribution.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.125794409, distribution.density(0), 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.5, distribution.cumulativeProbability(1), 1e-9);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        double sample = distribution.sample();
        assertNotEquals(0, sample);
    }
}