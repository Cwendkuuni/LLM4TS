package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {
    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructorValid() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        Assert.assertEquals(10, dist.getPopulationSize());
        Assert.assertEquals(5, dist.getNumberOfSuccesses());
        Assert.assertEquals(5, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid parameters (populationSize <= 0).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    /**
     * Test constructor with invalid parameters (numberOfSuccesses < 0).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    /**
     * Test constructor with invalid parameters (sampleSize < 0).
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    /**
     * Test constructor with invalid parameters (numberOfSuccesses > populationSize).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidNumberOfSuccessesPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    /**
     * Test constructor with invalid parameters (sampleSize > populationSize).
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorInvalidSampleSizePopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.cumulativeProbability(x);
            double expected = innerCumulativeProbability(domain[0], x, 1);
            Assert.assertEquals(expected, prob, 1e-12);
        }
    }

    /**
     * Inner helper method for testing cumulativeProbability method.
     *
     * @param x0 Inclusive lower bound.
     * @param x1 Inclusive upper bound.
     * @param dx Direction of summation (1 indicates summing from x0 to x1, and
     * 0 indicates summing from x1 to x0).
     * @return {@code P(x0 <= X <= x1)}.
     */
    private double innerCumulativeProbability(int x0, int x1, int dx) {
        double ret = 0.0;
        for (int x = x0; x <= x1; x += dx) {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
            ret += dist.probability(x);
        }
        return ret;
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        for (int x = 0; x <= 10; x++) {
            double prob = dist.probability(x);
            double expected = innerProbability(x);
            Assert.assertEquals(expected, prob, 1e-12);
        }
    }

    /**
     * Inner helper method for testing probability method.
     *
     * @param x Value at which the probability is evaluated.
     * @return {@code P(X = x)}.
     */
    private double innerProbability(int x) {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        double p = (double) dist.getSampleSize() / (double) dist.getPopulationSize();
        double q = (double) (dist.getPopulationSize() - dist.getSampleSize()) / (double) dist.getPopulationSize();
        double numerator = SaddlePointExpansion.logBinomialProbability(x, dist.getNumberOfSuccesses(), p, q);
        double denominator = SaddlePointExpansion.logBinomialProbability(dist.getSampleSize() - x,
                dist.getPopulationSize() - dist.getNumberOfSuccesses(), p, q);
        return Math.exp(numerator + denominator - SaddlePointExpansion.logBinomialProbability(dist.getSampleSize(),
                dist.getPopulationSize(), p, q));
    }

    /**
     * Test upperCumulativeProbability method.
     */
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.upperCumulativeProbability(x);
            double expected = 1.0 - dist.cumulativeProbability(x);
            Assert.assertEquals(expected, prob, 1e-12);
        }
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double mean = dist.getNumericalMean();
        Assert.assertEquals(5.0, mean, 1e-12);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double variance = dist.getNumericalVariance();
        Assert.assertEquals(1.96875, variance, 1e-12);
    }
}