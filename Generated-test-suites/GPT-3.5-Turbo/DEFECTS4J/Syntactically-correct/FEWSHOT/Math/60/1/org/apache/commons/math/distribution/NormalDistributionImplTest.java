package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0, normalDist.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(1, normalDist.getStandardDeviation(), 0);
    }
    
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, normalDist.density(0), 1e-9);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, normalDist.cumulativeProbability(0), 1e-9);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0), 1e-9);
    }
    
    @Test
    public void testSample() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        double value = normalDist.sample();
        assertTrue(value >= Double.NEGATIVE_INFINITY && value <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, normalDist.getSolverAbsoluteAccuracy(), 1e-9);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normalDist.getDomainLowerBound(0), 0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, normalDist.getDomainUpperBound(0), 0);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normalDist.getInitialDomain(0), 1e-9);
    }
}