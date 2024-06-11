package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

/**
 * JUnit test cases for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testHypergeometricDistributionValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test constructor with population size less than or equal to zero.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionPopulationSizeInvalid() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes less than zero.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNumberOfSuccessesInvalid() {
        int populationSize = 5;
        int numberOfSuccesses = -1;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size less than zero.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionSampleSizeInvalid() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes greater than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size greater than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test method getNumericalMean().
     */
    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        assertEquals(expected, distribution.getNumericalMean(), 0.0001);
    }

    /**
     * Test method getNumericalVariance().
     */
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expected, distribution.getNumericalVariance(), 0.0001);
    }

    /**
     * Test method cumulativeProbability().
     */
    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = distribution.getDomain();

        // Test for lower bound
        int x = domain[0];
        double expected = 0.0;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Test for upper bound
        x = domain[1];
        expected = 1.0;
        actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Test for value between bounds
        int y = domain[0] + 1;
        expected = distribution.probability(y);
        actual = distribution.cumulativeProbability(y);
        assertEquals(expected, actual, 0.0001);
    }

    /**
     * Test method probability().
     */
    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double expected = distribution.probability(x);
            double actual = distribution.cumulativeProbability(x + 1) - distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    /**
     * Test method upperCumulativeProbability().
     */
    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = distribution.getDomain();

        // Test for upper bound
        int x = domain[1];
        double expected = 1.0;
        double actual = distribution.upperCumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);

        // Test for value less than lower bound
        int y = domain[0] - 1;
        expected = 0.0;
        actual = distribution.upperCumulativeProbability(y);
        assertEquals(expected, actual, 0.0001);

        // Test for value between bounds
        int z = domain[0];
        expected = 1.0 - distribution.cumulativeProbability(z);
        actual = distribution.upperCumulativeProbability(z);
        assertEquals(expected, actual, 0.0001);
    }
}