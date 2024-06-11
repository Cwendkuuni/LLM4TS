package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, -1, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 0, 2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, 0, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = dist.getDomain();

        assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 0.001);
        assertEquals(0.278398550561798, dist.cumulativeProbability(domain[0]), 0.001);
        assertEquals(0.4999999999999999, dist.cumulativeProbability(domain[1] - 1), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(domain[1]), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = dist.getDomain();

        assertEquals(0.0, dist.probability(domain[0] - 1), 0.001);
        assertEquals(0.0972972972972973, dist.probability(domain[0]), 0.001);
        assertEquals(0.16666666666666663, dist.probability(domain[1] - 1), 0.001);
        assertEquals(0.1111111111111111, dist.probability(domain[1]), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = dist.getDomain();

        assertEquals(1.0, dist.upperCumulativeProbability(domain[0] - 1), 0.001);
        assertEquals(0.7216027397260275, dist.upperCumulativeProbability(domain[0]), 0.001);
        assertEquals(0.5, dist.upperCumulativeProbability(domain[1] - 1), 0.001);
        assertEquals(0.4999999999999999, dist.upperCumulativeProbability(domain[1]), 0.001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double numericalMean = dist.getNumericalMean();
        assertEquals(3.5, numericalMean, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(2.123105800236739, numericalVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int lowerBound = dist.getSupportLowerBound();
        assertEquals(2, lowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int upperBound = dist.getSupportUpperBound();
        assertEquals(5, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}