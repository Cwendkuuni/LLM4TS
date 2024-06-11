package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test the constructor with invalid arguments: population size <= 0.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionPopulationSizeZero() {
        new HypergeometricDistribution(0, 5, 3);
    }

    /**
     * Test the constructor with invalid arguments: number of successes < 0.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionNumberOfSuccessesNegative() {
        new HypergeometricDistribution(10, -1, 3);
    }

    /**
     * Test the constructor with invalid arguments: sample size < 0.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionSampleSizeNegative() {
        new HypergeometricDistribution(10, 5, -1);
    }

    /**
     * Test the constructor with invalid arguments: population size >= number of successes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionPopulationSizeGreaterThanSuccesses() {
        new HypergeometricDistribution(6, 5, 3);
    }

    /**
     * Test the constructor with invalid arguments: sample size >= population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test the method {@link HypergeometricDistribution#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3, x;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (x = 0; x <= dist.getSupportUpperBound(); x++) {
            double p = dist.cumulativeProbability(x);
            double q = dist.probability(x);
            assertEquals(Precision.isClose(p + q, 1.0, 1e-12), true);
        }
    }

    /**
     * Test the method {@link HypergeometricDistribution#probability(int)} with valid arguments.
     */
    @Test
    public void testProbabilityValidArguments() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3, x;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (x = 0; x <= dist.getSupportUpperBound(); x++) {
            double p = dist.probability(x);
            assertTrue(p >= 0.0);
        }
    }

    /**
     * Test the method {@link HypergeometricDistribution#probability(int)} with an invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testProbabilityInvalidArgument() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.probability(-1);
    }

    /**
     * Test the method {@link HypergeometricDistribution#upperCumulativeProbability(int)} with valid arguments.
     */
    @Test
    public void testUpperCumulativeProbabilityValidArguments() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3, x;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (x = 0; x <= dist.getSupportUpperBound(); x++) {
            double p = dist.upperCumulativeProbability(x);
            double q = 1.0 - dist.cumulativeProbability(x);
            assertEquals(Precision.isClose(p + q, 1.0, 1e-12), true);
        }
    }

    /**
     * Test the method {@link HypergeometricDistribution#upperCumulativeProbability(int)} with an invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testUpperCumulativeProbabilityInvalidArgument() {
        int populationSize = 10, numberOfSuccesses = 5, sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.upperCumulativeProbability(-1);
    }
}