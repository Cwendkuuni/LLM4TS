package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPopulationSize() {
        new HypergeometricDistribution(-1, 5, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6153846153846154, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(0.15384615384615385, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.probability(-1), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
        assertEquals(0.23076923076923078, dist.probability(2), 0.0001);
        assertEquals(0.3076923076923077, dist.probability(1), 0.0001);
        assertEquals(0.3076923076923077, dist.probability(3), 0.0001);
        assertEquals(0.15384615384615385, dist.probability(0), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.38461538461538464, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.8461538461538461, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.42857142857142855, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }
}