package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        assertEquals(0.0, normal.getMean(), 0.0001);
        
        normal.setMean(2.5);
        assertEquals(2.5, normal.getMean(), 0.0001);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(1.0);
        assertEquals(1.0, normal.getMean(), 0.0001);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        assertEquals(1.0, normal.getStandardDeviation(), 0.0001);
        
        normal.setStandardDeviation(2.5);
        assertEquals(2.5, normal.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(2.0);
        assertEquals(2.0, normal.getStandardDeviation(), 0.0001);
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        double result = normal.cumulativeProbability(0);
        assertEquals(0.5, result, 0.0001);
        
        result = normal.cumulativeProbability(2.0);
        assertEquals(0.9772, result, 0.0001);
        
        normal.setMean(1.0);
        normal.setStandardDeviation(0.5);
        result = normal.cumulativeProbability(0);
        assertEquals(0.3524, result, 0.0001);
        
        result = normal.cumulativeProbability(1.5);
        assertEquals(0.9332, result, 0.0001);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        double result = normal.inverseCumulativeProbability(0.5);
        assertEquals(0, result, 0.0001);
        
        result = normal.inverseCumulativeProbability(0.9772);
        assertEquals(2.0, result, 0.0001);
        
        normal.setMean(1.0);
        normal.setStandardDeviation(0.5);
        result = normal.inverseCumulativeProbability(0.3524);
        assertEquals(0, result, 0.0001);
        
        result = normal.inverseCumulativeProbability(0.9332);
        assertEquals(1.5, result, 0.0001);
    }
}