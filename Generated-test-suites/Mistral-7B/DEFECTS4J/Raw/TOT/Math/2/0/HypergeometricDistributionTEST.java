package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test construction with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalidPopulationSize() {
        final int populationSize = 0;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidNumberOfSuccesses() {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructionInvalidSampleSize() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters due to number of successes larger
     * than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters() {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test construction with invalid parameters due to sample size larger than
     * population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionInvalidParameters2() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test {@link #cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int x = 0; x <= sampleSize; x++) {
            double p = dist.cumulativeProbability(x);
            int expected = getExpectedCumulativeProbability(populationSize, numberOfSuccesses, sampleSize, x);
            assertEquals(expected, p, 1e-14);
        }
    }

    /**
     * Test {@link #probability(int)}.
     */
    @Test
    public void testProbability() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int x = 0; x <= sampleSize; x++) {
            double p = dist.probability(x);
            double expected = getExpectedProbability(populationSize, numberOfSuccesses, sampleSize, x);
            assertEquals(expected, p, 1e-14);
        }
    }

    /**
     * Test {@link #getSupportLowerBound()}.
     */
    @Test
    public void testSupportLowerBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = dist.getSupportLowerBound();
        int expected = getExpectedSupportLowerBound(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expected, lowerBound);
    }

    /**
     * Test {@link #getSupportUpperBound()}.
     */
    @Test
    public void testSupportUpperBound() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = dist.getSupportUpperBound();
        int expected = getExpectedSupportUpperBound(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expected, upperBound);
    }

    /**
     * Test {@link #getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalMean = dist.getNumericalMean();
        double expected = getExpectedNumericalMean(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expected, numericalMean, 1e-14);
    }

    /**
     * Test {@link #getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double numericalVariance = dist.getNumericalVariance();
        double expected = getExpectedNumericalVariance(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expected, numericalVariance, 1e-14);
    }

    /**
     * Test {@link #getNumericalMean()} and {@link #getNumericalVariance()} with
     * different parameters.
     */
    @Test
    public void testNumericalMeanAndVariance() {
        final int populationSize1 = 10;
        final int numberOfSuccesses1 = 3;
        final int sampleSize1 = 5;
        final int populationSize2 = 20;
        final int numberOfSuccesses2 = 8;
        final int sampleSize2 = 10;
        HypergeometricDistribution dist1 = new HypergeometricDistribution(populationSize1, numberOfSuccesses1, sampleSize1);
        HypergeometricDistribution dist2 = new HypergeometricDistribution(populationSize2, numberOfSuccesses2, sampleSize2);
        double numericalMean1 = dist1.getNumericalMean();
        double numericalMean2 = dist2.getNumericalMean();
        double expectedMean1 = getExpectedNumericalMean(populationSize1, numberOfSuccesses1, sampleSize1);
        double expectedMean2 = getExpectedNumericalMean(populationSize2, numberOfSuccesses2, sampleSize2);
        assertEquals(expectedMean1, numericalMean1, 1e-14);
        assertEquals(expectedMean2, numericalMean2, 1e-14);
        double numericalVariance1 = dist1.getNumericalVariance();
        double numericalVariance2 = dist2.getNumericalVariance();
        double expectedVariance1 = getExpectedNumericalVariance(populationSize1, numberOfSuccesses1, sampleSize1);
        double expectedVariance2 = getExpectedNumericalVariance(populationSize2, numberOfSuccesses2, sampleSize2);
        assertEquals(expectedVariance1, numericalVariance1, 1e-14);
        assertEquals(expectedVariance2, numericalVariance2, 1e-14);
    }

    /**
     * Test {@link #isSupportConnected()}.
     */
    @Test
    public void testIsSupportConnected() {
        final int populationSize = 10;
        final int numberOfSuccesses = 3;
        final int sampleSize = 5;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Helper method to compute the expected cumulative probability for a given
     * hypergeometric distribution and a given value.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @param x Value.
     * @return the expected cumulative probability for the given value.
     */
    private static int getExpectedCumulativeProbability(int populationSize, int numberOfSuccesses, int sampleSize,
                                                        int x) {
        return SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, (double) sampleSize / (double) populationSize,
                                                           (double) (populationSize - sampleSize) / (double) populationSize);
    }

    /**
     * Helper method to compute the expected probability for a given hypergeometric
     * distribution and a given value.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @param x Value.
     * @return the expected probability for the given value.
     */
    private static double getExpectedProbability(int populationSize, int numberOfSuccesses, int sampleSize, int x) {
        double p = (double) sampleSize / (double) populationSize;
        double q = (double) (populationSize - sampleSize) / (double) populationSize;
        return SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q)
               + SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q)
               + SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
    }

    /**
     * Helper method to compute the expected lower bound for the support of a given
     * hypergeometric distribution.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @return the expected lower bound for the support.
     */
    private static int getExpectedSupportLowerBound(int populationSize, int numberOfSuccesses, int sampleSize) {
        return FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
    }

    /**
     * Helper method to compute the expected upper bound for the support of a given
     * hypergeometric distribution.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @return the expected upper bound for the support.
     */
    private static int getExpectedSupportUpperBound(int populationSize, int numberOfSuccesses, int sampleSize) {
        return FastMath.min(numberOfSuccesses, sampleSize);
    }

    /**
     * Helper method to compute the expected numerical mean for a given
     * hypergeometric distribution.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @return the expected numerical mean.
     */
    private static double getExpectedNumericalMean(int populationSize, int numberOfSuccesses, int sampleSize) {
        return (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
    }

    /**
     * Helper method to compute the expected numerical variance for a given
     * hypergeometric distribution.
     *
     * @param populationSize Population size.
     * @param numberOfSuccesses Number of successes in the population.
     * @param sampleSize Sample size.
     * @return the expected numerical variance.
     */
    private static double getExpectedNumericalVariance(int populationSize, int numberOfSuccesses, int sampleSize) {
        double N = (double) populationSize;
        double m = (double) numberOfSuccesses;
        double n = (double) sampleSize;
        return (n * m * (N - n) * (N - m)) / (N * N * (N - 1));
    }
}