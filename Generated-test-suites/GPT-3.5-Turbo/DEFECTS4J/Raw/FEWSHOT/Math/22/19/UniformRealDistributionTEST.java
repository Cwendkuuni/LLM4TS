package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    private UniformRealDistribution distribution;
    
    @Before
    public void setUp() throws NumberIsTooLargeException {
        distribution = new UniformRealDistribution(0, 1);
    }
    
    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(-1), 0.00001);
        assertEquals(1.0, distribution.density(0.5), 0.00001);
        assertEquals(0.0, distribution.density(2), 0.00001);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.00001);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.00001);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.0/12, distribution.getNumericalVariance(), 0.00001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double value = distribution.sample();
            assertTrue(value >= 0.0 && value < 1.0);
        }
    }
}