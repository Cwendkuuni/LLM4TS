package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroOrNegativePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        populationSize = -1;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        populationSize = 5;
        numberOfSuccesses = 11;
        sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        populationSize = 5;
        numberOfSuccesses = 5;
        sampleSize = 11;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = (i + 0.5) / (double) sampleSize;
            double actual = dist.cumulativeProbability(i);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityWithNegativeArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double argument = -1.5;
        dist.cumulativeProbability((int) argument);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = 5;
        int actual = dist.getNumberOfSuccesses();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = 10;
        int actual = dist.getPopulationSize();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expected = 5;
        int actual = dist.getSampleSize();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double expected =
                    (numberOfSuccesses + i) / (double) (populationSize) *
                    (populationSize - numberOfSuccesses + i) / (double) (populationSize);
            double actual = dist.probability(i);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testProbabilityWithNegativeArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double argument = -1.5;
        dist.probability((int) argument);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = 1.0 - dist.cumulativeProbability(i);
            double actual = dist.upperCumulativeProbability(i);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testUpperCumulativeProbabilityWithNegativeArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double argument = -1.5;
        dist.upperCumulativeProbability((int) argument);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) sampleSize * numberOfSuccesses / (double) populationSize;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 1e-12);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expected =
                (double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) *
                (populationSize - numberOfSuccesses) / (double) (populationSize * populationSize * (populationSize - 1));
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 1e-12);
    }

    @org.junit.Test
    public void testGenerateNumber() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        RandomGenerator rng = new Well19937c();

        for (int i = 0; i < 100; i++) {
            int actual = dist.generateNumber(rng);
            int[] domain = dist.getDomain();
            assertTrue(domain[0] <= actual && actual <= domain[1]);
        }
    }
}