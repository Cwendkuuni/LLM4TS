package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(-1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(1, -1, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(1, 1, 2);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(1, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(1, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(1, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(1), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(0.5, dist.probability(0), 0.001);
        assertEquals(0.5, dist.probability(1), 0.001);
        assertEquals(0.0, dist.probability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(0.5, dist.upperCumulativeProbability(0), 0.001);
        assertEquals(1.0, dist.upperCumulativeProbability(1), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(2), 0.001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(1.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(0.5, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertEquals(1, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
        int sampledValue = dist.sample();
        assertEquals(0, sampledValue);
        sampledValue = dist.sample();
        assertEquals(1, sampledValue);
    }
}