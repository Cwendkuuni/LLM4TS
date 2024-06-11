package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    
    private static final double EPSILON = 1e-9;
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), EPSILON);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), EPSILON);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.3989422804014327, distribution.density(0.0), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(1.0), EPSILON);
        assertEquals(0.05399096651318806, distribution.density(-1.0), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), EPSILON);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.15865525393145707, distribution.cumulativeProbability(-1.0), EPSILON);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), EPSILON);
        assertEquals(1.0, distribution.inverseCumulativeProbability(0.8413447460685429), EPSILON);
        assertEquals(-1.0, distribution.inverseCumulativeProbability(0.15865525393145707), EPSILON);
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double value = distribution.sample();
        assertTrue(value >= Double.NEGATIVE_INFINITY && value <= Double.POSITIVE_INFINITY);
    }
    
}