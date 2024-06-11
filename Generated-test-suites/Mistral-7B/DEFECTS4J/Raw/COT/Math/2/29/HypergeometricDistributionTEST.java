package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for HypergeometricDistribution class
 */
public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 1;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 1;
        int sampleSize = 0;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 12;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 12;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testHypergeometricDistributionBasicFunctionality()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int expectedValue = 2;
        int value = hypergeometricDistribution.nextInt();
        assertEquals(expectedValue, value);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int value = 2;
        double expectedProbability = hypergeometricDistribution.cumulativeProbability(value);
        double actualProbability = hypergeometricDistribution.cumulativeProbability(value);
        assertEquals(expectedProbability, actualProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int value = 2;
        double expectedProbability = hypergeometricDistribution.probability(value);
        double actualProbability = hypergeometricDistribution.probability(value);
        assertEquals(expectedProbability, actualProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int value = 5;
        double expectedProbability = hypergeometricDistribution.upperCumulativeProbability(value);
        double actualProbability = hypergeometricDistribution.upperCumulativeProbability(value);
        assertEquals(expectedProbability, actualProbability, 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalMean()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expectedMean = 3.0;
        double actualMean = hypergeometricDistribution.getNumericalMean();
        assertEquals(expectedMean, actualMean, 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expectedVariance = 2.166666666666667;
        double actualVariance = hypergeometricDistribution.getNumericalVariance();
        assertEquals(expectedVariance, actualVariance, 0.001);
    }

    @Test
    public void testHypergeometricDistributionGetDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        int[] domain = hypergeometricDistribution.getDomain();
        int expectedLowerBound = 2;
        int expectedUpperBound = 5;
        int actualLowerBound = domain[0];
        int actualUpperBound = domain[1];
        assertEquals(expectedLowerBound, actualLowerBound);
        assertEquals(expectedUpperBound, actualUpperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        boolean expected = true;
        boolean actual = hypergeometricDistribution.isSupportConnected();
        assertEquals(expected, actual);
    }

}