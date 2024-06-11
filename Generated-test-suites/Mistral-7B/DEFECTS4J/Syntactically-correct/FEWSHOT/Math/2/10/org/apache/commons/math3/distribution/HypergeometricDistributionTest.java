package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 3, -2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 2, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 3, 0, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithTooManySuccessesInPopulation() {
        new HypergeometricDistribution(new Well19937c(), 3, 4, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 3, 3, 6);
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability_LowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double p = dist.cumulativeProbability(0);
        assertTrue(p >= 0.0 && p <= 0.01);
    }

    @Test
    public void testCumulativeProbability_UpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double p = dist.cumulativeProbability(dist.getSupportUpperBound());
        assertEquals(1.0, p, 0.001);
    }

    @Test
    public void testCumulativeProbability_MiddleValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double p = dist.cumulativeProbability(3);
        assertEquals(0.3974651922712189, p, 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double p = dist.probability(2);
        assertEquals(0.2431747941251168, p, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double p = dist.upperCumulativeProbability(3);
        assertEquals(0.602534807728781, p, 0.0001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double mean = dist.getNumericalMean();
        assertEquals(3.5, mean, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        double variance = dist.getNumericalVariance();
        assertEquals(1.923056410254249, variance, 0.001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int lowerBound = dist.getSupportLowerBound();
        assertEquals(2, lowerBound);
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int upperBound = dist.getSupportUpperBound();
        assertEquals(5, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }

}