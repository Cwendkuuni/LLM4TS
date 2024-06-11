package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.125, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.875, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertArrayEquals(new int[]{0, 3}, dist.getDomain(10, 3, 5));
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0, dist.getLowerDomain(10, 3, 5));
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(3, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(3, dist.getUpperDomain(3, 5));
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(0.35714, dist.probability(1), 0.0001);
        assertEquals(0.57142, dist.probability(2), 0.0001);
        assertEquals(0.07142, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.875, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.125, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0.0, dist.innerCumulativeProbability(3, 0, -1), 0.0001);
        assertEquals(0.0, dist.innerCumulativeProbability(3, 1, -1), 0.0001);
        assertEquals(0.07142, dist.innerCumulativeProbability(3, 2, -1), 0.0001);
        assertEquals(0.64285, dist.innerCumulativeProbability(3, 3, -1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0.9375, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0.9375, dist.calculateNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);

        assertTrue(dist.isSupportConnected());
    }
}

