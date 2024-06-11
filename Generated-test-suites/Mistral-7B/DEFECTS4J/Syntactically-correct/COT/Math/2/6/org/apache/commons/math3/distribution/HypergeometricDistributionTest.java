package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testHypergeometricDistributionConstruction() throws Exception {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        new HypergeometricDistribution(0, 1, 1);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSizeNumberOfSuccesses() {
        new HypergeometricDistribution(0, 0, 1);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @org.junit.Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = (double) i / (double) dist.getPopulationSize();
            double actual = dist.cumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @org.junit.Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double[] probabilities = new double[dist.getSupportUpperBound() + 1];
        for (int i = 0; i <= dist.getSupportUpperBound(); i++) {
            probabilities[i] = dist.probability(i);
        }
        for (int i = 0; i <= dist.getSupportUpperBound(); i++) {
            double sum = 0.0;
            for (int j = 0; j <= i; j++) {
                sum += probabilities[j];
            }
            assertEquals(sum, dist.cumulativeProbability(i), 0.0001);
        }
    }

    @org.junit.Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = 1.0 - dist.cumulativeProbability(domain[1]);
            double actual = dist.upperCumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @org.junit.Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double expected = (double) 5.0 / (double) 10.0;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 0.0001);
    }

    @org.junit.Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double expected = 0.0024644313559375;
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 0.000001);
    }

    @org.junit.Test
    public void testHypergeometricDistributionRandomValues() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Well19937c rng = new Well19937c();
        dist.setSeed(rng.nextLong());
        int numberOfTrials = 10000;
        int[] values = new int[numberOfTrials];
        for (int i = 0; i < numberOfTrials; i++) {
            values[i] = dist.nextInt();
        }
        int[] domain = dist.getDomain();
        int sum = 0;
        for (int i = 0; i < numberOfTrials; i++) {
            sum += values[i];
        }
        int expectedSum = 0;
        for (int i = domain[0]; i <= domain[1]; i++) {
            expectedSum += i;
        }
        assertEquals(expectedSum, sum);
    }
}