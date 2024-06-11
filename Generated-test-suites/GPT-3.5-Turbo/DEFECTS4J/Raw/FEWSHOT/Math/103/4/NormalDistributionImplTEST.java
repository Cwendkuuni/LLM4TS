package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        assertEquals(0.0, normal.getMean(), 0.0);
        
        normal.setMean(3.2);
        assertEquals(3.2, normal.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setMean(3.2);
        assertEquals(3.2, normal.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        assertEquals(1.0, normal.getStandardDeviation(), 0.0);
        
        normal.setStandardDeviation(2.5);
        assertEquals(2.5, normal.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(2.5);
        assertEquals(2.5, normal.getStandardDeviation(), 0.0);
        
        assertThrows(IllegalArgumentException.class, () -> {normal.setStandardDeviation(0.0);});
    }
    
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        
        assertEquals(0.5, normal.cumulativeProbability(0.0), 0.0);
        assertEquals(0.6914624613, normal.cumulativeProbability(1.0), 1e-10);
        assertEquals(0.0227501319, normal.cumulativeProbability(-2.0), 1e-10);
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        
        assertEquals(-Double.MAX_VALUE, normal.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1.0), 0.0);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.0), 0.0);
        assertEquals(0.0, normal.getDomainLowerBound(0.5), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        
        assertEquals(0.0, normal.getDomainUpperBound(0.0), 0.0);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.5), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        
        assertEquals(-1.0, normal.getInitialDomain(0.0), 0.0);
        assertEquals(1.0, normal.getInitialDomain(0.5), 0.0);
    }
}