package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test the constructor with invalid arguments for population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructionPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the constructor with invalid arguments for number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the constructor with invalid arguments for sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidConstructionSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = -1;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the constructor with invalid arguments for sample size (larger than population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructionSampleSizeTooLarge() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the constructor with invalid arguments for number of successes (larger than population size).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructionNumberOfSuccessesTooLarge() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test the cumulative probability method with valid input.
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double expected = distribution.cumulativeProbability(i);
            double actual = distribution.cumulativeProbability(i);
            assertEquals("Cumulative probability is incorrect for input " + i, expected, actual, 1e-13);
        }
    }

    /**
     * Test the cumulative probability method with invalid input (x < lower bound).
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityLowerBound() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        double expected = 0.0;

        for (int i = 0; i < lowerBound; i++) {
            double actual = distribution.cumulativeProbability(i);
            assertEquals("Cumulative probability is incorrect for input " + i, expected, actual, 1e-13);
        }
    }

    /**
     * Test the probability method with valid input.
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityValidInput() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double expected = distribution.probability(i);
            double actual = distribution.probability(i);
            assertEquals("Probability is incorrect for input " + i, expected, actual, 1e-13);
        }
    }

    /**
     * Test the probability method with invalid input (x < lower bound).
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityLowerBound() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();

        for (int i = 0; i < lowerBound; i++) {
            double expected = 0.0;
            double actual = distribution.probability(i);
            assertEquals("Probability is incorrect for input " + i, expected, actual, 1e-13);
        }
    }

    /**
     * Test the upper cumulative probability method with valid input.
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testUpperCumulativeProbabilityValidInput() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int i = 0; i <= sampleSize; i++) {
            double expected = 1.0 - distribution.cumulativeProbability(i);
            double actual = distribution.upperCumulativeProbability(i);
            assertEquals("Upper cumulative probability is incorrect for input " + i, expected, actual, 1e-13);
        }
    }

    /**
     * Test the upper cumulative probability method with invalid input (x > upper bound).
     * @throws NotPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testUpperCumulativeProbabilityUpperBound() throws NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int upperBound = distribution.getSupportUpperBound();
        double expected = 1.0;

        for (int i = 0; i <= upperBound; i++) {
            double actual = distribution.upperCumulativeProbability(i);
            assertEquals("Upper cumulative probability is incorrect for input " + i, expected, actual, 1e-13);
        }

        double actual = distribution.upperCumulativeProbability(upperBound + 1);
        assertEquals("Upper cumulative probability is incorrect for input " + (upperBound + 1), 0.0, actual, 1e-13);
    }
}