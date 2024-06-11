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
            // Expected
        }
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositivePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 7;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = FastMath.comb(populationSize, numberOfSuccesses)
                    * FastMath.comb(populationSize - sampleSize, x - numberOfSuccesses)
                    * FastMath.comb(sampleSize, x)
                    / FastMath.pow(populationSize, sampleSize);
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = FastMath.comb(populationSize, numberOfSuccesses)
                    * FastMath.comb(populationSize - sampleSize, x - numberOfSuccesses)
                    * FastMath.comb(sampleSize, x)
                    / FastMath.pow(populationSize, sampleSize);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = FastMath.comb(populationSize - sampleSize, x - numberOfSuccesses)
                    * FastMath.comb(sampleSize, x)
                    / FastMath.comb(populationSize, x);
            double actual = distribution.upperCumulativeProbability(x);
            assertEquals(expected, actual, 1e-12);
        }
    }

    @org.junit.Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 1e-12);
    }

    @org.junit.Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)
                / (populationSize * populationSize * (populationSize - 1));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 1e-12);
    }

}