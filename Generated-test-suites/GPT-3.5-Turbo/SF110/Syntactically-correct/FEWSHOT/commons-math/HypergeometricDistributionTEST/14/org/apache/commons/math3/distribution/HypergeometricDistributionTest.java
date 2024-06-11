package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, dist.cumulativeProbability(-10), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.1428, dist.cumulativeProbability(4), 0.0001);
        assertEquals(0.5714, dist.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(6), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(100), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.2222, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 2);
        assertTrue(dist.isSupportConnected());
    }
}