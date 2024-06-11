package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {
    
    private static final double EPSILON = 1e-9;
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normal = new NormalDistributionImpl(10, 2);
        assertEquals(10, normal.getMean(), EPSILON);
        
        normal.setMean(15);
        assertEquals(15, normal.getMean(), EPSILON);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normal = new NormalDistributionImpl(10, 2);
        assertEquals(2, normal.getStandardDeviation(), EPSILON);
        
        normal.setStandardDeviation(3);
        assertEquals(3, normal.getStandardDeviation(), EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationZero() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl normal = new NormalDistributionImpl();
        normal.setStandardDeviation(-1);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        
        try {
            assertEquals(0.5, normal.cumulativeProbability(0), EPSILON);
            assertEquals(0.841344746, normal.cumulativeProbability(1), EPSILON);
            assertEquals(0.977249868, normal.cumulativeProbability(2), EPSILON);
            assertEquals(0.99996833, normal.cumulativeProbability(5), EPSILON);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        
        try {
            assertEquals(0, normal.inverseCumulativeProbability(0), EPSILON);
            assertEquals(1, normal.inverseCumulativeProbability(0.841344746), EPSILON);
            assertEquals(2, normal.inverseCumulativeProbability(0.977249868), EPSILON);
            assertEquals(5, normal.inverseCumulativeProbability(0.99996833), EPSILON);
        } catch (MathException e) {
            fail("MathException thrown");
        }
        
        try {
            normal.inverseCumulativeProbability(-0.1);
            fail("MathException not thrown");
        } catch (MathException e) {
            assertTrue(true);
        }
        
        try {
            normal.inverseCumulativeProbability(1.1);
            fail("MathException not thrown");
        } catch (MathException e) {
            assertTrue(true);
        }
        
        assertEquals(Double.NEGATIVE_INFINITY, normal.inverseCumulativeProbability(0), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, normal.inverseCumulativeProbability(1), EPSILON);
    }
    
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, normal.getDomainLowerBound(0.4), EPSILON);
        assertEquals(0, normal.getDomainLowerBound(0.5), EPSILON);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(0, normal.getDomainUpperBound(0.4), EPSILON);
        assertEquals(Double.MAX_VALUE, normal.getDomainUpperBound(0.6), EPSILON);
    }
    
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normal = new NormalDistributionImpl(0, 1);
        assertEquals(-1, normal.getInitialDomain(0.4), EPSILON);
        assertEquals(1, normal.getInitialDomain(0.6), EPSILON);
        assertEquals(0, normal.getInitialDomain(0.5), EPSILON);
    }
}