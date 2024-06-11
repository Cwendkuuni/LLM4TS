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

    /**
     * Test constructor with valid arguments.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testHypergeometricDistributionValidArgs() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test constructor with invalid populationSize.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionInvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 5;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid numberOfSuccesses.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionInvalidNumberOfSuccesses() {
        int populationSize = 5;
        int numberOfSuccesses = -2;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sampleSize.
     */
    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionInvalidSampleSize() {
        int populationSize = 5;
        int numberOfSuccesses = 5;
        int sampleSize = -2;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sampleSize > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionInvalidSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 2;
        int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid numberOfSuccesses > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionInvalidNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityValidArgs() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 3;
        double expected = 0.3514;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test cumulativeProbability method with invalid argument.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityInvalidArgument() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 11;
        double expected = 1.0;
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test probability method with valid arguments.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityValidArgs() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 3;
        double expected = 0.3514;
        double actual = distribution.probability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test probability method with invalid argument.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityInvalidArgument() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 11;
        double expected = 0.0;
        double actual = distribution.probability(x);
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test numericalMean method.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testNumericalMean() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 3.0;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.001);
    }

    /**
     * Test numericalVariance method.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testNumericalVariance() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 2;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = 1.356;
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.001);
    }
}