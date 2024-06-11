package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testConstructHypergeometricDistribution() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NotPositiveException");
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() throws Exception {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NotStrictlyPositiveException");
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testZeroNumberOfSuccesses() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NotStrictlyPositiveException");
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSampleSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 0;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NotStrictlyPositiveException");
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 3;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NumberIsTooLargeException");
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        fail("Expected NumberIsTooLargeException");
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        int[] domain = hd.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expectedCumulativeProbability = hd.cumulativeProbability(x);
            double calculatedCumulativeProbability = hd.innerCumulativeProbability(domain[0], x, 1);
            assertEquals(expectedCumulativeProbability, calculatedCumulativeProbability, 1e-10);
        }
    }

    @org.junit.Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        int[] domain = hd.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expectedProbability = hd.probability(x);
            double calculatedProbability = hd.probability(x);
            assertEquals(expectedProbability, calculatedProbability, 1e-10);
        }
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        int[] domain = hd.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expectedUpperCumulativeProbability = hd.upperCumulativeProbability(x);
            double calculatedUpperCumulativeProbability = hd.innerCumulativeProbability(domain[1], x, -1);
            assertEquals(expectedUpperCumulativeProbability, calculatedUpperCumulativeProbability, 1e-10);
        }
    }

    @org.junit.Test
    public void testNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        double expectedNumericalMean = (double) hd.getSampleSize() * hd.getNumberOfSuccesses() / (double) hd.getPopulationSize();
        double calculatedNumericalMean = hd.getNumericalMean();
        assertEquals(expectedNumericalMean, calculatedNumericalMean, 1e-10);
    }

    @org.junit.Test
    public void testNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        double expectedNumericalVariance = (hd.getSampleSize() * hd.getNumberOfSuccesses() * (hd.getPopulationSize() - hd.getSampleSize()) * (hd.getPopulationSize() - hd.getNumberOfSuccesses())) / (hd.getPopulationSize() * hd.getPopulationSize() * (hd.getPopulationSize() - 1));
        double calculatedNumericalVariance = hd.getNumericalVariance();
        assertEquals(expectedNumericalVariance, calculatedNumericalVariance, 1e-10);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        int expectedSupportLowerBound = FastMath.max(0, hd.getSampleSize() + hd.getNumberOfSuccesses() - hd.getPopulationSize());
        int calculatedSupportLowerBound = hd.getSupportLowerBound();
        assertEquals(expectedSupportLowerBound, calculatedSupportLowerBound);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        int expectedSupportUpperBound = FastMath.min(hd.getNumberOfSuccesses(), hd.getSampleSize());
        int calculatedSupportUpperBound = hd.getSupportUpperBound();
        assertEquals(expectedSupportUpperBound, calculatedSupportUpperBound);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 3);
        boolean expectedIsSupportConnected = true;
        boolean calculatedIsSupportConnected = hd.isSupportConnected();
        assertEquals(expectedIsSupportConnected, calculatedIsSupportConnected);
    }
}