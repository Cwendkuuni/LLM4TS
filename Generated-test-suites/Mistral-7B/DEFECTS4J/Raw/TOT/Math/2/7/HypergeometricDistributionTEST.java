package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
@Category(org.apache.commons.math3.distribution.HypergeometricDistributionTest.class)
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
        assertArrayEquals(new int[] { 3, 5 }, dist.getDomain());
    }

    /**
     * Test constructor with IllegalArgumentException for negative arguments.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeArguments() {
        int populationSize = -1;
        int numberOfSuccesses = -2;
        int sampleSize = -3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with IllegalArgumentException for sampleSize > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 2;
        int sampleSize = 6;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with IllegalArgumentException for numberOfSuccesses > populationSize.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 5;
        int numberOfSuccesses = 6;
        int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test getNumericalMean with valid arguments.
     */
    @Test
    public void testGetNumericalMeanValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test getNumericalVariance with valid arguments.
     */
    @Test
    public void testGetNumericalVarianceValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) /
                (populationSize * populationSize * (populationSize - 1));
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test probability method with valid arguments.
     */
    @Test
    public void testProbabilityValidArguments() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = SaddlePointExpansion.logBinomialProbability(2,
                3, dist.getSampleSize() / (double) populationSize,
                (populationSize - dist.getSampleSize()) / (double) populationSize);
        double actual = dist.probability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test probability method with upper domain bound.
     */
    @Test
    public void testProbabilityUpperDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = dist.probability(6);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test probability method with lower domain bound.
     */
    @Test
    public void testProbabilityLowerDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 1.0;
        double actual = dist.probability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test cumulativeProbability method with valid argument.
     */
    @Test
    public void testCumulativeProbabilityValidArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = SaddlePointExpansion.logBinomialProbability(2,
                3, dist.getSampleSize() / (double) populationSize,
                (populationSize - dist.getSampleSize()) / (double) populationSize);
        double actual = dist.cumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test cumulativeProbability method with upper domain bound.
     */
    @Test
    public void testCumulativeProbabilityUpperDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 1.0;
        double actual = dist.cumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test cumulativeProbability method with lower domain bound.
     */
    @Test
    public void testCumulativeProbabilityLowerDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = dist.cumulativeProbability(1);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test upperCumulativeProbability method with valid argument.
     */
    @Test
    public void testUpperCumulativeProbabilityValidArgument() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 1.0 - SaddlePointExpansion.logBinomialProbability(2,
                3, dist.getSampleSize() / (double) populationSize,
                (populationSize - dist.getSampleSize()) / (double) populationSize);
        double actual = dist.upperCumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test upperCumulativeProbability method with upper domain bound.
     */
    @Test
    public void testUpperCumulativeProbabilityUpperDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 1.0;
        double actual = dist.upperCumulativeProbability(2);
        assertEquals(expected, actual, 1e-12);
    }

    /**
     * Test upperCumulativeProbability method with lower domain bound.
     */
    @Test
    public void testUpperCumulativeProbabilityLowerDomainBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize,
                numberOfSuccesses, sampleSize);
        double expected = 0.0;
        double actual = dist.upperCumulativeProbability(1);
        assertEquals(expected, actual, 1e-12);
    }
}