package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Tests for the {@link HypergeometricDistribution} class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with negative number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNonPositivePopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNonPositiveSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = -5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes larger than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size larger than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test getNumberOfSuccesses method.
     */
    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
    }

    /**
     * Test getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
    }

    /**
     * Test getSampleSize method.
     */
    @Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 1;
        double expectedValue = dist.cumulativeProbability(x);
        double actualValue = dist.cumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test cumulativeProbability method with lower domain value.
     */
    @Test
    public void testCumulativeProbabilityLowerDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 0;
        double expectedValue = 0.0;
        double actualValue = dist.cumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test cumulativeProbability method with upper domain value.
     */
    @Test
    public
    void testCumulativeProbabilityUpperDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 7;
        double expectedValue = 0.6878626875;
        double actualValue = dist.cumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 1;
        double expectedValue = dist.probability(x);
        double actualValue = dist.probability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test probability method with lower domain value.
     */
    @Test
    public void testProbabilityLowerDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 0;
        double expectedValue = 0.0;
        double actualValue = dist.probability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test probability method with upper domain value.
     */
    @Test
    public void testProbabilityUpperDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 7;
        double expectedValue = 0.312711025;
        double actualValue = dist.probability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test upperCumulativeProbability method with valid arguments.
     */
    @Test
    public void testUpperCumulativeProbabilityValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 1;
        double expectedValue = 1.0 - dist.cumulativeProbability(x - 1);
        double actualValue = dist.upperCumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test upperCumulativeProbability method with lower domain value.
     */
    @Test
    public void testUpperCumulativeProbabilityLowerDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 0;
        double expectedValue = 1.0;
        double actualValue = dist.upperCumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test upperCumulativeProbability method with upper domain value.
     */
    @Test
    public void testUpperCumulativeProbabilityUpperDomainValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int x = 7;
        double expectedValue = 0.0;
        double actualValue = dist.upperCumulativeProbability(x);
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedValue = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        double actualValue = dist.getNumericalMean();
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expectedValue = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        double actualValue = dist.getNumericalVariance();
        assertEquals(expectedValue, actualValue, 0.0001);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int expectedValue = FastMath.max(0, sampleSize + numberOfSuccesses - populationSize);
        int actualValue = dist.getSupportLowerBound();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int expectedValue = FastMath.min(numberOfSuccesses, sampleSize);
        int actualValue = dist.getSupportUpperBound();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test generateRandomValue method with valid arguments.
     */
    @Test
    public void testGenerateRandomValueValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
        int expectedMin = FastMath.max(0, numberOfSuccesses - (populationSize - sampleSize));
        int expectedMax = FastMath.min(numberOfSuccesses, sampleSize);
        int x = dist.generateRandomValue();
        assertTrue(x >= expectedMin && x <= expectedMax);
    }
}