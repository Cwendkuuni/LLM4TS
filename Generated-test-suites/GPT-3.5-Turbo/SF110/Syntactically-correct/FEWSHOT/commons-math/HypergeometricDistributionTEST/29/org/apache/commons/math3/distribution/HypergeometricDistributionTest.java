
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution distr = new HypergeometricDistribution(1, 0, 0);
        } catch (Exception e) {
            assertEquals("org.apache.commons.math3.exception.NotStrictlyPositiveException: Population size must be positive, but got: 1", e.toString());
        }
        
        try {
            HypergeometricDistribution distr = new HypergeometricDistribution(0, 1, 0);
        } catch (Exception e) {
            assertEquals("org.apache.commons.math3.exception.NotStrictlyPositiveException: Number of successes must be positive, but got: 1", e.toString());
        }
        
        try {
            HypergeometricDistribution distr = new HypergeometricDistribution(0, 0, 1);
        } catch (Exception e) {
            assertEquals("org.apache.commons.math3.exception.NotStrictlyPositiveException: Number of samples must be positive, but got: 1", e.toString());
        }
        
        try {
            HypergeometricDistribution distr = new HypergeometricDistribution(2, 3, 4);
        } catch (Exception e) {
            assertEquals("org.apache.commons.math3.exception.NumberIsTooLargeException: Number of successes (3) must be less than or equal to population size (2)", e.toString());
        }
        
        try {
            HypergeometricDistribution distr = new HypergeometricDistribution(4, 2, 5);
        } catch (Exception e) {
            assertEquals("org.apache.commons.math3.exception.NumberIsTooLargeException: Sample size (5) must be less than or equal to population size (4)", e.toString());
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.012587, distr.cumulativeProbability(0), 0.000001);
        assertEquals(0.055556, distr.cumulativeProbability(1), 0.000001);
        assertEquals(0.209877, distr.cumulativeProbability(2), 0.000001);
        assertEquals(0.506173, distr.cumulativeProbability(3), 0.000001);
        assertEquals(0.825309, distr.cumulativeProbability(4), 0.000001);
        assertEquals(1.000000, distr.cumulativeProbability(5), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.500000, distr.getNumericalMean(), 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.428571, distr.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distr.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distr.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distr = new HypergeometricDistribution(10, 5, 3);
        assertEquals(true, distr.isSupportConnected());
    }
}
