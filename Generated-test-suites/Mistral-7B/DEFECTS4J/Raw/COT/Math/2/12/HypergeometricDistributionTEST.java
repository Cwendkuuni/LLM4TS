package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test class for HypergeometricDistribution
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     *
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test
    public void testConstructorValid()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses,
                sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test constructor with invalid population size.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        new HypergeometricDistribution(-1, 1, 1);
    }

    /**
     * Test constructor with invalid number of successes.
     */
    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(1, -1, 1);
    }

    /**
     * Test constructor with invalid sample size.
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSize() {
        new HypergeometricDistribution(1, 1, 11);
    }

    /**
     * Test constructor with random generator.
     *
     * @throws NotPositiveException
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test
    public void testConstructorRandomGenerator()
            throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize,
                numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    /**
     * Test getLowerDomain method.
     */
    @org.junit.Test
    public void testGetLowerDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize)
                .getDomain();
        int lowerDomain = domain[0];
        int expectedLowerDomain = FastMath.max(0, numberOfSuccesses - (populationSize - sampleSize));
        assertEquals(expectedLowerDomain, lowerDomain);
    }

    /**
     * Test getUpperDomain method.
     */
    @org.junit.Test
    public void testGetUpperDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        int[] domain = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize)
                .getDomain();
        int upperDomain = domain[1];
        int expectedUpperDomain = FastMath.min(sampleSize, numberOfSuccesses);
        assertEquals(expectedUpperDomain, upperDomain);
    }

    /**
     * Test probability method with valid input.
     */
    @org.junit.Test
    public void testProbabilityValidInput() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        for (int i = 0; i <= sampleSize; i++) {
            double probability = distribution.probability(i);
            assertEquals(probability, distribution.cumulativeProbability(i) - distribution
                    .cumulativeProbability(i - 1), 1e-10);
        }
    }

    /**
     * Test probability method with invalid input.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testProbabilityInvalidInput() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 1, 1);
        distribution.probability(-1);
    }

    /**
     * Test cumulativeProbability method with valid inputs.
     */
    @org.junit.Test
    public void testCumulativeProbabilityValidInputs() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double cumulativeProbability = distribution.cumulativeProbability(0);
        assertTrue(cumulativeProbability >= 0 && cumulativeProbability <= 1);
        cumulativeProbability = distribution.cumulativeProbability(sampleSize);
        assertTrue(cumulativeProbability >= 0 && cumulativeProbability <= 1);
    }

    /**
     * Test cumulativeProbability method with invalid inputs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityInvalidInputs() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 1, 1);
        distribution.cumulativeProbability(-1);
    }

    /**
     * Test upperCumulativeProbability method with valid inputs.
     */
    @org.junit.Test
    public void testUpperCumulativeProbabilityValidInputs() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double upperCumulativeProbability = distribution.upperCumulativeProbability(0);
        assertTrue(upperCumulativeProbability >= 0 && upperCumulativeProbability <= 1);
        upperCumulativeProbability = distribution.upperCumulativeProbability(sampleSize);
        assertTrue(upperCumulativeProbability >= 0 && upperCumulativeProbability <= 1);
    }

    /**
     * Test upperCumulativeProbability method with invalid inputs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testUpperCumulativeProbabilityInvalidInputs() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 1, 1);
        distribution.upperCumulativeProbability(-1);
    }

    /**
     * Test innerCumulativeProbability method with valid inputs.
     */
    @org.junit.Test
    public void testInnerCumulativeProbabilityValidInputs() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double innerCumulativeProbability = distribution.innerCumulativeProbability(0, 0, 1);
        assertEquals(innerCumulativeProbability, 0, 1e-10);
        innerCumulativeProbability = distribution.innerCumulativeProbability(sampleSize, sampleSize, 1);
        assertEquals(innerCumulativeProbability, 1, 1e-10);
        innerCumulativeProbability = distribution.innerCumulativeProbability(sampleSize / 2, sampleSize / 2, 1);
        double expectedInnerCumulativeProbability = distribution.probability(sampleSize / 2);
        assertEquals(expectedInnerCumulativeProbability, innerCumulativeProbability, 1e-10);
    }

    /**
     * Test innerCumulativeProbability method with invalid inputs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInnerCumulativeProbabilityInvalidInputs() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 1, 1);
        distribution.innerCumulativeProbability(-1, 0, 1);
    }

    /**
     * Test getNumericalMean method.
     */
    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(numericalMean, (double) (sampleSize * numberOfSuccesses) / populationSize, 1e-12);
    }

    /**
     * Test getNumericalVariance method.
     */
    @org.junit.Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / (populationSize * populationSize * (populationSize - 1));
        assertEquals(numericalVariance, expectedNumericalVariance, 1e-12);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @org.junit.Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int supportLowerBound = distribution.getSupportLowerBound();
        int expectedSupportLowerBound = FastMath.max(0, sampleSize + numberOfSuccesses - populationSize);
        assertEquals(expectedSupportLowerBound, supportLowerBound);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @org.junit.Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int supportUpperBound = distribution.getSupportUpperBound();
        int expectedSupportUpperBound = FastMath.min(sampleSize, numberOfSuccesses);
        assertEquals(expectedSupportUpperBound, supportUpperBound);
    }

    /**
     * Test isSupportConnected method.
     */
    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 4;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(distribution.isSupportConnected());
    }

}