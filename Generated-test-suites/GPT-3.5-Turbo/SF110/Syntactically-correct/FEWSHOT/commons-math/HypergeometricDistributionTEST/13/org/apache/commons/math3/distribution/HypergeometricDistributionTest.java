package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometric.cumulativeProbability(-1), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(0), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(1), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(2), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(3), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(4), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(5), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(6), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(7), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(8), 0.000001);
        assertEquals(0.0, hypergeometric.cumulativeProbability(9), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(10), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(11), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(12), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(13), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(14), 0.000001);
        assertEquals(1.0, hypergeometric.cumulativeProbability(15), 0.000001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometric.probability(-1), 0.000001);
        assertEquals(0.0, hypergeometric.probability(0), 0.000001);
        assertEquals(0.0, hypergeometric.probability(1), 0.000001);
        assertEquals(0.05, hypergeometric.probability(2), 0.000001);
        assertEquals(0.3, hypergeometric.probability(3), 0.000001);
        assertEquals(0.55, hypergeometric.probability(4), 0.000001);
        assertEquals(0.6, hypergeometric.probability(5), 0.000001);
        assertEquals(0.55, hypergeometric.probability(6), 0.000001);
        assertEquals(0.3, hypergeometric.probability(7), 0.000001);
        assertEquals(0.05, hypergeometric.probability(8), 0.000001);
        assertEquals(0.0, hypergeometric.probability(9), 0.000001);
        assertEquals(0.0, hypergeometric.probability(10), 0.000001);
        assertEquals(0.0, hypergeometric.probability(11), 0.000001);
        assertEquals(0.0, hypergeometric.probability(12), 0.000001);
        assertEquals(0.0, hypergeometric.probability(13), 0.000001);
        assertEquals(0.0, hypergeometric.probability(14), 0.000001);
        assertEquals(0.0, hypergeometric.probability(15), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(-1), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(0), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(1), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(2), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(3), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(4), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(5), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(6), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(7), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(8), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(9), 0.000001);
        assertEquals(1.0, hypergeometric.upperCumulativeProbability(10), 0.000001);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(11), 0.000001);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(12), 0.000001);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(13), 0.000001);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(14), 0.000001);
        assertEquals(0.0, hypergeometric.upperCumulativeProbability(15), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometric.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.65, hypergeometric.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometric.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, hypergeometric.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometric = new HypergeometricDistribution(10, 5, 3);
        assertEquals(true, hypergeometric.isSupportConnected());
    }
}