package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with positive arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with negative arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = -5;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorPopulationSizeZero() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorSampleSizeZero() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with arguments larger than population size.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with arguments larger than population size.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test probability method with valid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testProbabilityValidArguments() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedProbability = 0.3279661000305848;
        assertEquals(expectedProbability, dist.probability(2), 1e-10);
    }

    /**
     * Test probability method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testProbabilityInvalidArgument() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.probability(-1);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testCumulativeProbabilityValidArguments() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedProbability = 0.3279661000305848;
        double expectedCumulativeProbability = 0.2380952380952381;
        assertEquals(expectedCumulativeProbability, dist.cumulativeProbability(2), 1e-10);
    }

    /**
     * Test cumulativeProbability method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCumulativeProbabilityInvalidArgument() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.cumulativeProbability(-1);
    }

    /**
     * Test upperCumulativeProbability method with valid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testUpperCumulativeProbabilityValidArguments() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedProbability = 0.6720338999694152;
        assertEquals(expectedProbability, dist.upperCumulativeProbability(7), 1e-10);
    }

    /**
     * Test upperCumulativeProbability method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testUpperCumulativeProbabilityInvalidArgument() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        dist.upperCumulativeProbability(-1);
    }

    /**
     * Test mean method with valid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testMeanValidArguments() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedMean = 3.0 * 5.0 / 10.0;
        assertEquals(expectedMean, dist.getNumericalMean(), 1e-10);
    }

    /**
     * Test variance method with valid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test
    public void testVarianceValidArguments() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedVariance = 3.0 * 3.0 * 5.0 * 7.0 / (10.0 * 10.0 * 9.0);
        assertEquals(expectedVariance, dist.getNumericalVariance(), 1e-10);
    }

    /**
     * Test mean method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testMeanNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = -5;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getNumericalMean();
    }

    /**
     * Test variance method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testVariancePopulationSizeZero() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getNumericalVariance();
    }

    /**
     * Test variance method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testVarianceSampleSizeZero() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getNumericalVariance();
    }

    /**
     * Test variance method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testVarianceNumberOfSuccessesLargerThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getNumericalVariance();
    }

    /**
     * Test variance method with invalid arguments.
     * @throws Exception if an exception is thrown
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testVarianceSampleSizeLargerThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize).getNumericalVariance();
    }
}