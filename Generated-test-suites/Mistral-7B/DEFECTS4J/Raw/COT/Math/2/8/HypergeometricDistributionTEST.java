package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;

import java.util.Arrays;

public class HypergeometricDistributionTest {

    @org.junit.Before
    public void setUp() {
        // Initialize the random number generator for test method use
    }

    @org.junit.Test
    public void testHypergeometricDistributionConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorWithZeroOrNegativePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorWithZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 3;
        int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test
    public void testConstructorsWithValidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        // Test values within the domain
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = distribution.cumulativeProbability(x);
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }

        // Test values outside the domain (lower)
        for (int x = domain[0] - 1; x >= 0; x--) {
            double expected = 0.0;
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }

        // Test values outside the domain (upper)
        for (int x = domain[1] + 1; x < Integer.MAX_VALUE; x++) {
            double expected = 1.0;
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double[] probabilities = new double[sampleSize + 1];

        for (int x = 0; x <= sampleSize; x++) {
            double expected = distribution.probability(x);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 0.00001);
            probabilities[x] = actual;
        }

        // Check that the probabilities sum to 1.0
        double sum = 0.0;
        for (double p : probabilities) {
            sum += p;
        }
        assertEquals(1.0, sum, 0.00001);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        // Test values within the domain
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = 1.0 - distribution.cumulativeProbability(x);
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 0.00001);
        }

        // Test values outside the domain (lower)
        for (int x = domain[0] - 1; x >= 0; x--) {
            double expected = 1.0;
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual);
        }

        // Test values outside the domain (upper)
        for (int x = domain[1] + 1; x < Integer.MAX_VALUE; x++) {
            double expected = 0.0;
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual);
        }
    }

    @org.junit.Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.001);
    }

    @org.junit.Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = ((double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / (double) (populationSize * populationSize * (populationSize - 1)));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.001);
    }

    @org.junit.Test
    public void testSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        int upperBound = distribution.getSupportUpperBound();

        int[] domain = distribution.getDomain();

        assertTrue(lowerBound <= domain[0]);
        assertTrue(upperBound >= domain[1]);

        int testValue = lowerBound - 1;
        assertFalse(distribution.isPointMassAt(testValue));

        testValue = upperBound + 1;
        assertFalse(distribution.isPointMassAt(testValue));
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        assertTrue(distribution.isSupportConnected());

        for (int i = 0; i < domain.length - 1; i++) {
            int x = domain[i];
            int y = domain[i + 1];
            assertTrue(distribution.isConnected(x, y));
        }
    }

    @org.junit.Test
    public void testGenerateRandomValues() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 1000;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] expectedDomain = distribution.getDomain();
        int[] generatedValues = new int[sampleSize];

        for (int i = 0; i < sampleSize; i++) {
            int x = distribution.nextInt();
            generatedValues[i] = x;
        }

        Arrays.sort(generatedValues);

        for (int i = 0; i < sampleSize; i++) {
            int x = generatedValues[i];
            assertTrue(Arrays.binarySearch(expectedDomain, x) >= 0);
        }

        assertEquals(sampleSize, distribution.getSampleSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(populationSize, distribution.getPopulationSize());
    }
}