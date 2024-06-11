package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructWithValidArguments() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, distribution.getPopulationSize());
        assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
        assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructWithNegativeNumberOfSuccesses() throws NotPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = -3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithZeroOrNegativePopulationSize() throws NotStrictlyPositiveException {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithZeroPopulationSize() throws NotStrictlyPositiveException {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithZeroNumberOfSuccesses() throws NotStrictlyPositiveException {
        int populationSize = 10;
        int numberOfSuccesses = 0;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 11;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalMean = distribution.getNumericalMean();
        assertEquals(5.0 * 3.0 / 10.0, numericalMean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.getNumericalVariance();
        double expectedVariance = ((double) 5.0 * 3.0 * (10.0 - 5.0) * (10.0 - 3.0)) / (100.0 * 10.0 * (10.0 - 1.0));
        assertEquals(expectedVariance, numericalVariance, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double p = 0.5;
        int x = 2;

        double cumulativeProbability = distribution.cumulativeProbability(x);

        double innerCumulativeProbability = innerCumulativeProbability(populationSize, numberOfSuccesses, p, x);
        double expectedCumulativeProbability = FastMath.exp(innerCumulativeProbability);

        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double p = 0.5;
        int x = 2;

        double upperCumulativeProbability = distribution.upperCumulativeProbability(x);

        double innerUpperCumulativeProbability = innerCumulativeProbability(populationSize, numberOfSuccesses, p, x + 1);
        double expectedUpperCumulativeProbability = 1.0 - FastMath.exp(innerUpperCumulativeProbability);

        assertEquals(expectedUpperCumulativeProbability, upperCumulativeProbability, 0.001);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double p = 0.5;
        int x = 2;

        double probability = distribution.probability(x);

        double logBinomialProbabilityP = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, 1.0 - p);
        double logBinomialProbabilitySampleSizeMinusX = SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - numberOfSuccesses, p, 1.0 - p);
        double logBinomialProbabilitySampleSize = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, 1.0 - p);

        double expectedProbability = FastMath.exp(logBinomialProbabilityP + logBinomialProbabilitySampleSizeMinusX - logBinomialProbabilitySampleSize);

        assertEquals(expectedProbability, probability, 0.001);
    }

    private double innerCumulativeProbability(int populationSize, int numberOfSuccesses, double p, int x) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be non-negative");
        }

        double logBinomialProbability = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses, p, 1.0 - p);
        return logBinomialProbability;
    }
}