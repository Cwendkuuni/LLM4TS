package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for the HypergeometricDistribution class.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     * @throws Exception if construction fails
     */
    @Test
    public void testValidConstruction() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with negative number of successes.
     * @throws Exception if construction fails
     */
    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive population size.
     * @throws Exception if construction fails
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonPositivePopulationSize() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive number of successes in the population.
     * @throws Exception if construction fails
     */
    @Test(expected = NotPositiveException.class)
    public void testNonPositiveNumberOfSuccessesInPopulation() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 0;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with non-positive sample size.
     * @throws Exception if construction fails
     */
    @Test(expected = NotPositiveException.class)
    public void testNonPositiveSampleSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with sample size greater than population size.
     * @throws Exception if construction fails
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 11;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with number of successes greater than population size.
     * @throws Exception if construction fails
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 11;
        final int sampleSize = 3;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test basic functionality of cumulativeProbability method.
     * @throws Exception if probabilities are not correct
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.cumulativeProbability(x);
            double expected = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses,
                    sampleSize / (double) populationSize, (populationSize - sampleSize) / (double) populationSize);
            double error = Math.abs(prob - expected);
            assertTrue("Error for x = " + x + ": " + error, error < 1e-10);
        }
    }

    /**
     * Test basic functionality of probability method.
     * @throws Exception if probabilities are not correct
     */
    @Test
    public void testProbability() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double prob;
        for (int x = 0; x <= populationSize; x++) {
            if ((x >= numberOfSuccesses || x >= sampleSize) && x != populationSize) {
                prob = dist.probability(x);
                assertEquals(0.0, prob, 1e-12);
            } else {
                prob = dist.probability(x);
                double expected = SaddlePointExpansion.logBinomialProbability(x, numberOfSuccesses,
                        sampleSize / (double) populationSize, (populationSize - sampleSize) / (double) populationSize);
                double error = Math.abs(prob - Math.exp(expected));
                assertTrue("Error for x = " + x + ": " + error, error < 1e-10);
            }
        }
    }

    /**
     * Test basic functionality of probability method with negative sample size.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotPositiveException.class)
    public void testProbabilityNegativeSampleSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = -1;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(0);
    }

    /**
     * Test basic functionality of probability method with negative number of successes.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotPositiveException.class)
    public void testProbabilityNegativeNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = -1;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(0);
    }

    /**
     * Test basic functionality of probability method with population size equal to zero.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testProbabilityZeroPopulationSize() throws Exception {
        final int populationSize = 0;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(0);
    }

    /**
     * Test basic functionality of probability method with number of successes equal to zero.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotPositiveException.class)
    public void testProbabilityZeroNumberOfSuccesses() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 0;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(0);
    }

    /**
     * Test basic functionality of probability method with number of successes equal to the population size.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotPositiveException.class)
    public void testProbabilityNumberOfSuccessesEqualPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = populationSize;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(populationSize);
    }

    /**
     * Test basic functionality of probability method with sample size equal to the population size.
     * @throws Exception if probabilities are not correct
     */
    @Test(expected = NotPositiveException.class)
    public void testProbabilitySampleSizeEqualPopulationSize() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = populationSize;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double prob = dist.probability(populationSize);
    }

    /**
     * Test basic functionality of getNumericalMean method.
     * @throws Exception if mean is not correct
     */
    @Test
    public void testGetNumericalMean() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double mean = dist.getNumericalMean();
        double expected = sampleSize * numberOfSuccesses / (double) populationSize;
        assertEquals(expected, mean, 1e-12);
    }

    /**
     * Test basic functionality of getNumericalVariance method.
     * @throws Exception if variance is not correct
     */
    @Test
    public void testGetNumericalVariance() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double variance = dist.getNumericalVariance();
        double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses))
                / (populationSize * populationSize * (populationSize - 1));
        assertEquals(expected, variance, 1e-12);
    }

    /**
     * Test basic functionality of getSupportLowerBound method.
     * @throws Exception if lower bound is not correct
     */
    @Test
    public void testGetSupportLowerBound() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int lowerBound = dist.getSupportLowerBound();
        int expected = FastMath.max(0, numberOfSuccesses + sampleSize - populationSize);
        assertEquals(expected, lowerBound);
    }

    /**
     * Test basic functionality of getSupportUpperBound method.
     * @throws Exception if upper bound is not correct
     */
    @Test
    public void testGetSupportUpperBound() throws Exception {
        final int populationSize = 10;
        final int numberOfSuccesses = 5;
        final int sampleSize = 3;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int upperBound = dist.getSupportUpperBound();
        int expected = FastMath.min(numberOfSuccesses, sampleSize);
        assertEquals(expected, upperBound);
    }
}