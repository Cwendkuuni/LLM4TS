package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(100, dist.getPopulationSize());
        assertEquals(50, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(-100, 50, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(100, -50, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        new HypergeometricDistribution(100, 50, -10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 150, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(100, 50, 110);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(0.0, dist.cumulativeProbability(4), 0.00001);
        assertEquals(0.01211, dist.cumulativeProbability(5), 0.00001);
        assertEquals(0.20443, dist.cumulativeProbability(8), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.00001);
    }

    @Test
    public void testLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(0, dist.getLowerDomain(100, 50, 10));
        assertEquals(0, dist.getLowerDomain(100, 50, 101));
        assertEquals(40, dist.getLowerDomain(100, 50, 90));
    }

    @Test
    public void testUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(10, dist.getUpperDomain(50, 10));
        assertEquals(10, dist.getUpperDomain(50, 20));
        assertEquals(20, dist.getUpperDomain(100, 90));
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(3.49102E-05, dist.probability(4), 0.00001);
        assertEquals(0.00174, dist.probability(5), 0.00001);
        assertEquals(0.20443, dist.preProbability(8), 0.00001);
        assertEquals(0.50468, dist.probability(10), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(1.0, dist.upperCumulativeProbability(4), 0.00001);
        assertEquals(0.98789, dist.upperCumulativeProbability(5), 0.00001);
        assertEquals(0.04014, dist.upperCumulativeProbability(8), 0.00001);
        assertEquals(0.0, dist.upperCumulativeProbability(10), 0.00001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(5.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(0.26535, dist.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertEquals(10, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 50, 10);
        assertTrue(dist.isSupportConnected());
    }
}