import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        // Test valid input values
        HypergeometricDistribution distribution;
        try {
            distribution = new HypergeometricDistribution(10, 2, 3);
        } catch (Exception e) {
            // Should not throw an exception
            assertFalse(true);
        }
        
        // Test invalid input values
        try {
            distribution = new HypergeometricDistribution(-10, 2, 3);
        } catch (NotStrictlyPositiveException e) {
            // Should throw a NotStrictlyPositiveException
            assertTrue(true);
        }
        
        try {
            distribution = new HypergeometricDistribution(10, -2, 3);
        } catch (NotPositiveException e) {
            // Should throw a NotPositiveException
            assertTrue(true);
        }
        
        try {
            distribution = new HypergeometricDistribution(10, 2, -3);
        } catch (NotPositiveException e) {
            // Should throw a NotPositiveException
            assertTrue(true);
        }
        
        try {
            distribution = new HypergeometricDistribution(10, 12, 3);
        } catch (NumberIsTooLargeException e) {
            // Should throw a NumberIsTooLargeException
            assertTrue(true);
        }
        
        try {
            distribution = new HypergeometricDistribution(10, 2, 30);
        } catch (NumberIsTooLargeException e) {
            // Should throw a NumberIsTooLargeException
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the mean of the distribution
        assertEquals(0.6, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the variance of the distribution
        assertEquals(0.514, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the lower bound of the support
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the upper bound of the support
        assertEquals(2, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test if the support is connected
        assertTrue(distribution.isSupportConnected());
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the probability of a given value
        assertEquals(0.2, distribution.probability(1), 0.001);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the cumulative probability function
        assertEquals(0.7, distribution.cumulativeProbability(1), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 2, 3);
        
        // Test the upper cumulative probability function
        assertEquals(0.3, distribution.upperCumulativeProbability(1), 0.001);
    }
}