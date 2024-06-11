package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistribution_ValidConstruction() throws Exception {
        // Test with valid parameters
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hypergeometricDistribution.getPopulationSize());
        assertEquals(numberOfSuccesses, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(sampleSize, hypergeometricDistribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistribution_InvalidNumberOfSuccesses() {
        // Test with invalid number of successes (negative value)
        int populationSize = 10;
        int numberOfSuccesses = -3;
        HypergeometricDistribution hypergeometricDistribution = null;
        try {
            hypergeometricDistribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, 5);
        } catch (NotPositiveException e) {
            hypergeometricDistribution = e.getLocalizedMessage();
        }
        assertEquals("NumberOfSuccesses must be non-negative: " + numberOfSuccesses, hypergeometricDistribution);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistribution_InvalidPopulationSize() {
        // Test with invalid population size (zero or negative value)
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("PopulationSize must be strictly positive: " + populationSize, e.getLocalizedMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_InvalidNumberOfSuccessesGreaterThanPopulationSize() {
        // Test with number of successes greater than population size
        int populationSize = 10;
        int numberOfSuccesses = 12;
        int sampleSize = 5;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("NumberOfSuccesses cannot be greater than PopulationSize: " + numberOfSuccesses +
                    " > " + populationSize, e.getLocalizedMessage());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_InvalidSampleSizeGreaterThanPopulationSize() {
        // Test with sample size greater than population size
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 12;
        try {
            new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        } catch (NumberIsTooLargeException e) {
            assertEquals("SampleSize cannot be greater than PopulationSize: " + sampleSize +
                    " > " + populationSize, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        // Test cumulative probability with valid parameters
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; ++x) {
            assertEquals(hypergeometricDistribution.cumulativeProbability(x),
                    innerCumulativeProbability(domain[0], x, 1), 1e-9);
        }
    }

    @Test
    public void testProbability() {
        // Test probability with valid parameters
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; ++x) {
            assertEquals(hypergeometricDistribution.probability(x),
                    SaddlePointExpansion.logBinomialProbability(x, 3, 5.0 / 10.0, 5.0 / 10.0), 1e-9);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test upper cumulative probability with valid parameters
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        int[] domain = hypergeometricDistribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; ++x) {
            assertEquals(1.0 - hypergeometricDistribution.cumulativeProbability(x),
                    hypergeometricDistribution.upperCumulativeProbability(x), 1e-9);
        }
    }

    @Test
    public void testNumericMean() {
        // Test numerical mean with valid parameters
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        double numericalMean = hypergeometricDistribution.getNumericalMean();
        assertEquals(5.0 * 3.0 / 10.0, numericalMean, 1e-9);
    }

    @Test
    public void testNumericalVariance() {
        // Test numerical variance with valid parameters
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        double numericalVariance = hypergeometricDistribution.getNumericalVariance();
        double expectedVariance = ((5.0 * 3.0 / 10.0) * (8.0 * 6.0 / 10.0) * (5.0 * 6.0 / 10.0)) / ((10.0 * 10.0 * (10.0 - 1.0)));
        assertEquals(expectedVariance, numericalVariance, 1e-9);
    }

    private double innerCumulativeProbability(int x0, int x1, int dx) {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        double ret = hypergeometricDistribution.probability(x0);
        int x = x0;
        while (x != x1) {
            x += dx;
            ret += hypergeometricDistribution.probability(x);
        }
        return ret;
    }
}