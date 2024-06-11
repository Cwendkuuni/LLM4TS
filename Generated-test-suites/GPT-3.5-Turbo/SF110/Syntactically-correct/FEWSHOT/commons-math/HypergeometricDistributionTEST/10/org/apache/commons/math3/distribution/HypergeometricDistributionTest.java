package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(2, dist.getSampleSize());
        } catch (Exception e) {
            assertTrue(false); // Fail the test if an exception is thrown
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.357, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.642, dist.cumulativeProbability(1), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.071, dist.probability(0), 0.001);
        assertEquals(0.357, dist.probability(1), 0.001);
        assertEquals(0.571, dist.probability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);
        assertEquals(0.642, dist.upperCumulativeProbability(1), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(2), 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.263, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(3, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertTrue(dist.isSupportConnected());
    }
}