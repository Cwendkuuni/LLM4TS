package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {
    
    private static final double EPSILON = 1e-9;
    
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(0.073385, f.density(0.5), EPSILON);
        assertEquals(0.075757, f.density(1.0), EPSILON);
        assertEquals(0.078963, f.density(1.5), EPSILON);
        
        f = new FDistribution(2, 2);
        assertEquals(0.064516, f.density(0.5), EPSILON);
        assertEquals(0.0625, f.density(1.0), EPSILON);
        assertEquals(0.060606, f.density(1.5), EPSILON);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(0.560112, f.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.649328, f.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.704777, f.cumulativeProbability(1.5), EPSILON);
        
        f = new FDistribution(2, 2);
        assertEquals(0.409091, f.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.333333, f.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.272727, f.cumulativeProbability(1.5), EPSILON);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(5.0, f.getNumeratorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(10.0, f.getDenominatorDegreesOfFreedom(), EPSILON);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(1.25, f.getNumericalMean(), EPSILON);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(6.666667, f.getNumericalVariance(), EPSILON);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(0.0, f.getSupportLowerBound(), EPSILON);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(5, 10);
        
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), EPSILON);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(5, 10);
        
        assertTrue(f.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(5, 10);
        
        assertFalse(f.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(5, 10);
        
        assertTrue(f.isSupportConnected());
    }
}