package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @org.junit.Test
    public void testConstructionValid() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            assertEquals(populationSize, distribution.getPopulationSize());
            assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
            assertEquals(sampleSize, distribution.getSampleSize());
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution with valid parameters: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution with valid parameters: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution with valid parameters: " + e.getMessage());
        }
    }

    /**
     * Test construction with invalid population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructionInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructionInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (number of successes > population size).
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters() {
        final int populationSize = 5;
        final int numberOfSuccesses = 6;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters (sample size > population size).
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters2() {
        final int populationSize = 5;
        final int numberOfSuccesses = 3;
        final int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test probability calculation.
     */
    @org.junit.Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            final int x = 1;
            final double expected = 0.31469602717344027;
            final double actual = distribution.probability(x);
            assertEquals(expected, actual, 1e-8);
            final int y = 4;
            final double expected2 = 0.21132486547931523;
            final double actual2 = distribution.probability(y);
            assertEquals(expected2, actual2, 1e-8);
            final int z = 6;
            final double expected3 = 0.02235853008651928;
            final double actual3 = distribution.probability(z);
            assertEquals(expected3, actual3, 1e-8);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }

    /**
     * Test cumulative probability calculation.
     */
    @org.junit.Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            final int x = 1;
            final double expected = 0.31469602717344027;
            final double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 1e-8);
            final int y = 4;
            final double expected2 = 0.55646925994685337;
            final double actual2 = distribution.cumulativeProbability(y);
            assertEquals(expected2, actual2, 1e-8);
            final int z = 6;
            final double expected3 = 0.67516703331745347;
            final double actual3 = distribution.cumulativeProbability(z);
            assertEquals(expected3, actual3, 1e-8);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }

    /**
     * Test variance calculation.
     */
    @org.junit.Test
    public void testVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            final double expected = 0.2484642337406836;
            final double actual = distribution.getNumericalVariance();
            assertEquals(expected, actual, 1e-8);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }

    /**
     * Test support bounds.
     */
    @org.junit.Test
    public void testSupportBounds() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            final int lowerBound = distribution.getSupportLowerBound();
            final int upperBound = distribution.getSupportUpperBound();
            assertEquals(0, lowerBound);
            assertEquals(3, upperBound);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }

    /**
     * Test support connectedness.
     */
    @org.junit.Test
    public void testSupportConnectedness() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                    numberOfSuccesses, sampleSize);
            final boolean expected = true;
            final boolean actual = distribution.isSupportConnected();
            assertEquals(expected, actual);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }

    /**
     * Test random generation.
     */
    @org.junit.Test
    public void testRandomGeneration() {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 10000;

        try {
            final HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(),
                    populationSize, numberOfSuccesses, sampleSize);
            final int[] x = new int[sampleSize];
            for (int i = 0; i < sampleSize; i++) {
                x[i] = distribution.sample();
            }
            final int sum = FastMath.sum(x);
            final int expected = numberOfSuccesses * sampleSize / populationSize;
            assertEquals(expected, sum);
        } catch (NotPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Failed to construct distribution: " + e.getMessage());
        }
    }
}