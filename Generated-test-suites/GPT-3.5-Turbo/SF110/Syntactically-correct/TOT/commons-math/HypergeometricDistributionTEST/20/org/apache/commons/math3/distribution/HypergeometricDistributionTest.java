
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        // Typical case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, dist.getPopulationSize());
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
        Assert.assertEquals(3, dist.getSampleSize());
        
        // Edge case: populationSize = 0
        try {
            dist = new HypergeometricDistribution(0, 5, 3);
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
        
        // Error scenario: numberOfSuccesses < 0
        try {
            dist = new HypergeometricDistribution(10, -5, 3);
            Assert.fail("Expected NotPositiveException to be thrown");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        
        // Error scenario: sampleSize < 0
        try {
            dist = new HypergeometricDistribution(10, 5, -3);
            Assert.fail("Expected NotPositiveException to be thrown");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        
        // Error scenario: numberOfSuccesses > populationSize
        try {
            dist = new HypergeometricDistribution(10, 15, 3);
            Assert.fail("Expected NumberIsTooLargeException to be thrown");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
        
        // Error scenario: sampleSize > populationSize
        try {
            dist = new HypergeometricDistribution(10, 5, 12);
            Assert.fail("Expected NumberIsTooLargeException to be thrown");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        // Typical case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.428571429, dist.cumulativeProbability(2), 0.000001);
        
        // Edge case: x < lower domain
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0.000001);
        
        // Edge case: x = lower domain
        Assert.assertEquals(0.0, dist.cumulativeProbability(0), 0.000001);
        
        // Edge case: x > upper domain
        Assert.assertEquals(1.0, dist.cumulativeProbability(4), 0.000001);
        
        // Edge case: x = upper domain
        Assert.assertEquals(1.0, dist.cumulativeProbability(3), 0.000001);
    }
    
    @Test
    public void testProbability() {
        // Typical case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.357142857, dist.probability(2), 0.000001);
        
        // Edge case: x < lower domain
        Assert.assertEquals(0.0, dist.probability(-1), 0.000001);
        
        // Edge case: x = lower domain
        Assert.assertEquals(0.0, dist.probability(0), 0.000001);
        
        // Edge case: x > upper domain
        Assert.assertEquals(0.0, dist.probability(4), 0.000001);
        
        // Edge case: x = upper domain
        Assert.assertEquals(0.0, dist.probability(3), 0.000001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        // Typical case
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.571428571, dist.upperCumulativeProbability(2), 0.000001);
        
        // Edge case: x < lower domain
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.000001);
        
        // Edge case: x = lower domain
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(0), 0.000001);
        
        // Edge case: x > upper domain
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(4), 0.000001);
        
        // Edge case: x = upper domain
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(3), 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.357142857, dist.getNumericalVariance(), 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(dist.isSupportConnected());
    }
}
