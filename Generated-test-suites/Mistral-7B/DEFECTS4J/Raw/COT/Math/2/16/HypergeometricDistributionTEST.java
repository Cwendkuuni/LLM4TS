package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorZeroSuccesses() {
        new HypergeometricDistribution(1, 0, 1);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() {
        new HypergeometricDistribution(1, 2, 1);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSampleSize() {
        new HypergeometricDistribution(1, 2, 0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 3, 1);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 2, 3);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = (double) i / 10.0;
            double actual = dist.cumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @org.junit.Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = dist.probability(i);
            double actual = dist.cumulativeProbability(i + 1) - dist.cumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = 1.0 - dist.cumulativeProbability(i);
            double actual = dist.upperCumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @org.junit.Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        assertEquals(2, domain.length);
        assertEquals(4, domain[0]);
        assertEquals(5, domain[1]);
    }

    @org.junit.Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int lower = dist.getLowerDomain();
        assertEquals(4, lower);
    }

    @org.junit.Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int upper = dist.getUpperDomain();
        assertEquals(5, upper);
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        double mean = dist.getNumericalMean();
        assertEquals(5.0, mean, 0.0001);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        double variance = dist.getNumericalVariance();
        assertEquals(2.916666666666667, variance, 0.00001);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        assertTrue(dist.isSupportConnected());
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int lower = dist.getSupportLowerBound();
        assertEquals(4, lower);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int upper = dist.getSupportUpperBound();
        assertEquals(5, upper);
    }
}