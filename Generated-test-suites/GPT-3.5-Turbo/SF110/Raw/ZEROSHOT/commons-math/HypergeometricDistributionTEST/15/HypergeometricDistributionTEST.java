package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Test x < lower domain
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        
        // Test x within domain
        assertEquals(0.1, dist.cumulativeProbability(1), 0.001);
        
        // Test x > upper domain
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Test x < lower domain
        assertEquals(0.0, dist.probability(0), 0.001);
        
        // Test x within domain
        assertEquals(0.1, dist.probability(1), 0.001);
        
        // Test x > upper domain
        assertEquals(0.0, dist.probability(6), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        
        // Test x <= lower domain
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);
        
        // Test x within domain
        assertEquals(0.9, dist.upperCumulativeProbability(1), 0.001);
        
        // Test x > upper domain
        assertEquals(0.0, dist.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.375, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}