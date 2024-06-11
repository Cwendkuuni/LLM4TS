package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for org.apache.commons.math3.distribution.HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroOrNegativePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        populationSize = -1;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 11;

        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Lower bound
        assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 0.001);

        // Middle value
        double middleProbability = dist.probability(domain[0]);
        assertEquals(middleProbability, dist.cumulativeProbability(domain[0]), 0.001);

        // Upper bound
        assertEquals(1.0, dist.cumulativeProbability(domain[1]), 0.001);

        // Value below lower bound
        assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 0.001);

        // Value above upper bound
        assertEquals(1.0, dist.cumulativeProbability(domain[1] + 1), 0.001);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Lower bound
        assertEquals(0.0, dist.probability(domain[0] - 1), 0.001);

        // Middle value
        double middleProbability = dist.probability(domain[0]);
        assertEquals(middleProbability, dist.cumulativeProbability(domain[0]) - dist.cumulativeProbability(domain[0] - 1), 0.001);

        // Upper bound
        assertEquals(0.0, dist.probability(domain[1] + 1), 0.001);

        // Middle value
        assertEquals(middleProbability, dist.probability(domain[0]), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();

        // Lower bound
        assertEquals(1.0, dist.upperCumulativeProbability(domain[0] - 1), 0.001);

        // Middle value
        double middleProbability = dist.probability(domain[0]);
        assertEquals(1.0 - middleProbability, dist.upperCumulativeProbability(domain[0]), 0.001);

        // Upper bound
        assertEquals(0.0, dist.upperCumulativeProbability(domain[1] + 1), 0.001);

        // Middle value
        assertEquals(0.0, dist.upperCumulativeProbability(domain[0]), 0.001);
    }

    @Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = dist.getNumericalMean();
        double analyticalMean = (double) sampleSize * numberOfSuccesses / (double) populationSize;

        assertEquals(analyticalMean, numericalMean, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = dist.getNumericalVariance();
        double analyticalVariance = ((double) sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));

        assertEquals(analyticalVariance, numericalVariance, 0.001);
    }
}