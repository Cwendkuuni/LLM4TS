package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {
    
    @Test
    public void testConstructor() {
        // Test with valid bounds
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
        
        // Test with lower bound equal to upper bound
        try {
            new UniformRealDistribution(0, 0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, e.getSpecifier());
        }
        
        // Test with lower bound greater than upper bound
        try {
            new UniformRealDistribution(1, 0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, e.getSpecifier());
        }
    }
    
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1, distribution.density(0.5), 0);
        assertEquals(0, distribution.density(1.5), 0);
        assertEquals(0, distribution.density(-0.5), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-0.5), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
        assertEquals(1, distribution.cumulativeProbability(1), 0);
        assertEquals(1, distribution.cumulativeProbability(1.5), 0);
    }
    
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }
    
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), 0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), 0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.getSupportUpperBound(), 0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }
    
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 100; i++) {
            double value = distribution.sample();
            assertTrue(value >= 0 && value < 1);
        }
    }
}