package org.apache.commons.math3.distribution;

import static org.apache.commons.math3.util.Precision.isClose;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testConstructWithValidArguments() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructWithNegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithPopulationSizeZero() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @org.junit.Test
    public void testGetDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] domain = distribution.getDomain();
        assertEquals(domain[0], 2);
        assertEquals(domain[1], 5);
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int x = 0; x <= 5; x++) {
            double prob = distribution.cumulativeProbability(x);
            int[] domain = distribution.getDomain();
            int lowerDomain = domain[0];
            int upperDomain = domain[1];
            if (x < lowerDomain) {
                assertEquals(0.0, prob, 1e-12);
            } else if (x >= upperDomain) {
                assertEquals(1.0, prob, 1e-12);
            } else {
                double expected = innerCumulativeProbability(lowerDomain, x, distribution);
                assertEquals(expected, prob, 1e-12);
            }
        }
    }

    private double innerCumulativeProbability(int lowerDomain, int x, HypergeometricDistribution distribution) {
        double ret = distribution.probability(lowerDomain);
        for (int i = lowerDomain + 1; i <= x; i++) {
            ret += distribution.probability(i);
        }
        return ret;
    }

    @org.junit.Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int x = 0; x <= 5; x++) {
            double prob = distribution.probability(x);
            double expected = calculateProbability(populationSize, numberOfSuccesses, sampleSize, x);
            assertEquals(expected, prob, 1e-12);
        }
    }

    private double calculateProbability(int populationSize, int numberOfSuccesses, int sampleSize, int x) {
        double p = (double) sampleSize / (double) populationSize;
        double q = (double) (populationSize - sampleSize) / (double) populationSize;
        double p1 = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, q);
        double p2 = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, q);
        double p3 = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
        double sum = p1 + p2 - p3;
        return FastMath.exp(sum);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        for (int x = 0; x <= 5; x++) {
            double prob = distribution.upperCumulativeProbability(x);
            int[] domain = distribution.getDomain();
            int lowerDomain = domain[0];
            int upperDomain = domain[1];
            if (x <= lowerDomain) {
                assertEquals(1.0, prob, 1e-12);
            } else if (x > upperDomain) {
                assertEquals(0.0, prob, 1e-12);
            } else {
                double expected = innerCumulativeProbability(x, lowerDomain, distribution, -1);
                assertEquals(expected, prob, 1e-12);
            }
        }
    }

    private double innerCumulativeProbability(int x, int lowerDomain, HypergeometricDistribution distribution, int dx) {
        double ret = distribution.probability(lowerDomain);
        while (lowerDomain != x) {
            lowerDomain++;
            ret += distribution.probability(lowerDomain);
        }
        return ret;
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = distribution.getNumericalMean();
        assertEquals(mean, (double) (sampleSize * numberOfSuccesses / populationSize), 1e-12);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = distribution.getNumericalVariance();
        double expectedVariance = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expectedVariance, variance, 1e-12);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = distribution.getSupportLowerBound();
        assertEquals(lowerBound, 2);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = distribution.getSupportUpperBound();
        assertEquals(upperBound, 5);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertTrue(distribution.isSupportConnected());
    }

    @org.junit.Test
    public void testGenerateRandomValue() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 100000;
        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int[] values = new int[sampleSize];
        Well19937c rng = new Well19937c();
        for (int i = 0; i < sampleSize; i++) {
            values[i] = distribution.sample(rng);
        }
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        double mean = (double) sum / sampleSize;
        assertEquals(mean, (double) (sampleSize * numberOfSuccesses / populationSize), 1e-6);
    }
}