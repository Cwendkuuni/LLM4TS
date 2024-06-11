package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {
    /**
     * Test constructor with valid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testConstructorValidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid population size.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid number of successes.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sample size.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid input (number of successes > population size).
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid input (sample size > population size).
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test method cumulativeProbability with valid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        double expected = 0.11764705882353057;
        double actual = dist.cumulativeProbability(1);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method cumulativeProbability with lower bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int lowerBound = dist.getSupportLowerBound();
        double expected = 0.0;
        double actual = dist.cumulativeProbability(lowerBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method cumulativeProbability with upper bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testCumulativeProbabilityUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int upperBound = dist.getSupportUpperBound();
        double expected = 0.3555685297367692;
        double actual = dist.cumulativeProbability(upperBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method cumulativeProbability with invalid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException, IllegalArgumentException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int input = 11;
        double actual = dist.cumulativeProbability(input);
    }

    /**
     * Test method probability with valid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityValidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int x = 2;
        double expected = 0.12596000233631666;
        double actual = dist.probability(x);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method probability with lower bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int lowerBound = dist.getSupportLowerBound();
        double expected = 0.0;
        double actual = dist.probability(lowerBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method probability with upper bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testProbabilityUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int upperBound = dist.getSupportUpperBound();
        double expected = 0.1731780887767273;
        double actual = dist.probability(upperBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method probability with invalid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityInvalidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException, IllegalArgumentException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int input = 11;
        double actual = dist.probability(input);
    }

    /**
     * Test method upperCumulativeProbability with valid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testUpperCumulativeProbabilityValidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        double expected = 0.8823529411764694;
        double actual = dist.upperCumulativeProbability(1);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method upperCumulativeProbability with lower bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testUpperCumulativeProbabilityLowerBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int lowerBound = dist.getSupportLowerBound();
        double expected = 1.0;
        double actual = dist.upperCumulativeProbability(lowerBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method upperCumulativeProbability with upper bound.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test
    public void testUpperCumulativeProbabilityUpperBound() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int upperBound = dist.getSupportUpperBound();
        double expected = 0.6444319111133307;
        double actual = dist.upperCumulativeProbability(upperBound);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method upperCumulativeProbability with invalid input.
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpperCumulativeProbabilityInvalidInput() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException, IllegalArgumentException {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(rng, 10, 3, 5);
        int input = 11;
        double actual = dist.upperCumulativeProbability(input);
    }
}