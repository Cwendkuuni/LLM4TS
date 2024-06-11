package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        new HypergeometricDistribution(-1, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(true, distribution.isSupportConnected());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(1), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(2), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(3), 0.0);
        assertEquals(0.0, distribution.cumulativeProbability(4), 0.0);
        assertEquals(0.011111111111111112, distribution.cumulativeProbability(5), 0.00001);
        assertEquals(0.13333333333333333, distribution.cumulativeProbability(6), 0.00001);
        assertEquals(0.4222222222222222, distribution.cumulativeProbability(7), 0.00001);
        assertEquals(0.8, distribution.cumulativeProbability(8), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(9), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(11), 0.0);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(0), 0.0);
        assertEquals(0.0, distribution.probability(1), 0.0);
        assertEquals(0.0, distribution.probability(2), 0.0);
        assertEquals(0.011111111111111112, distribution.probability(5), 0.00001);
        assertEquals(0.12222222222222223, distribution.probability(6), 0.00001);
        assertEquals(0.3, distribution.probability(7), 0.00001);
        assertEquals(0.24444444444444444, distribution.probability(8), 0.00001);
        assertEquals(0.022222222222222223, distribution.probability(9), 0.00001);
        assertEquals(0.0, distribution.probability(10), 0.0);
        assertEquals(0.0, distribution.probability(11), 0.0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0);
        assertEquals(0.9888888888888889, distribution.upperCumulativeProbability(1), 0.00001);
        assertEquals(0.8777777777777778, distribution.upperCumulativeProbability(2), 0.00001);
        assertEquals(0.5666666666666667, distribution.upperCumulativeProbability(3), 0.00001);
        assertEquals(0.1111111111111111, distribution.upperCumulativeProbability(4), 0.00001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(7), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(8), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(9), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.0);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.7833333333333333, distribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.7833333333333333, distribution.calculateNumericalVariance(), 0.00001);
    }
}