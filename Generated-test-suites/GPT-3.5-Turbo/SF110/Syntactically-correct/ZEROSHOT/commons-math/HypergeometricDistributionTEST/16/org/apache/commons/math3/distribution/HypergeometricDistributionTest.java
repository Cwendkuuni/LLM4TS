package org.apache.commons.math3.distribution;

import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        // Test constructor with valid input
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(2, dist.getSampleSize());
        
        // Test constructor with invalid population size
        try {
            dist = new HypergeometricDistribution(0, 5, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
        
        // Test constructor with invalid number of successes
        try {
            dist = new HypergeometricDistribution(10, -1, 2);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        
        // Test constructor with invalid sample size
        try {
            dist = new HypergeometricDistribution(10, 5, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        
        // Test constructor with number of successes larger than population size
        try {
            dist = new HypergeometricDistribution(10, 11, 2);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
        
        // Test constructor with sample size larger than population size
        try {
            dist = new HypergeometricDistribution(10, 5, 11);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        // Test cumulative probability with valid input
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, dist.cumulativeProbability(0), 1e-10);
        assertEquals(1.0, dist.cumulativeProbability(2), 1e-10);
        assertEquals(0.3636363636, dist.cumulativeProbability(1), 1e-10);
        
        // Test cumulative probability with invalid input
        try {
            dist.cumulativeProbability(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testProbability() {
        // Test probability with valid input
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, dist.probability(0), 1e-10);
        assertEquals(0.5454545455, dist.probability(1), 1e-10);
        assertEquals(0.0909090909, dist.probability(2), 1e-10);
        
        // Test probability with invalid input
        assertEquals(0.0, dist.probability(-1), 1e-10);
        assertEquals(0.0, dist.probability(3), 1e-10);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        // Test upper cumulative probability with valid input
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 1e-10);
        assertEquals(0.0909090909, dist.upperCumulativeProbability(1), 1e-10);
        assertEquals(0.0, dist.upperCumulativeProbability(2), 1e-10);
        
        // Test upper cumulative probability with invalid input
        try {
            dist.upperCumulativeProbability(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testNumericalMean() {
        // Test numerical mean calculation
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.getNumericalMean(), 1e-10);
    }
    
    @Test
    public void testNumericalVariance() {
        // Test numerical variance calculation
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.1052631579, dist.getNumericalVariance(), 1e-10);
    }
    
    @Test
    public void testSupportLowerBound() {
        // Test lower bound of support
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(3, dist.getSupportLowerBound());
    }
    
    @Test
    public void testSupportUpperBound() {
        // Test upper bound of support
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        // Test if support is connected
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertTrue(dist.isSupportConnected());
    }
}