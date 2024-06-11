package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(0.03462162990159145, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(0.1155058690945243, distribution.cumulativeProbability(15), 0.0001);
        assertEquals(0.6694813410087105, distribution.cumulativeProbability(25), 0.0001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(50, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(100, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(20, distribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(0.05580154269605302, distribution.probability(10), 0.0001);
        assertEquals(0.08213247284901433, distribution.probability(15), 0.0001);
        assertEquals(0.06435739763879993, distribution.probability(25), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(0.9653783700984085, distribution.upperCumulativeProbability(10), 0.0001);
        assertEquals(0.8753185840485938, distribution.upperCumulativeProbability(15), 0.0001);
        assertEquals(0.04522812563121254, distribution.upperCumulativeProbability(25), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(10.0, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(10.0, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(30, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertEquals(20, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(100, 50, 20);
        assertTrue(distribution.isSupportConnected());
    }

}