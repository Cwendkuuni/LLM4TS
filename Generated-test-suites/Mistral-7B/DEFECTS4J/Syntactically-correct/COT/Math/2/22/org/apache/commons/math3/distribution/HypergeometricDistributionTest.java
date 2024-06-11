package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for HypergeometricDistribution.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals("Incorrect number of successes.", 5, dist.getNumberOfSuccesses());
        assertEquals("Incorrect population size.", 10, dist.getPopulationSize());
        assertEquals("Incorrect sample size.", 5, dist.getSampleSize());
    }

    /**
     * Test constructor with negative population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    /**
     * Test constructor with negative number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 5);
    }

    /**
     * Test constructor with negative sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -5);
    }

    /**
     * Test constructor with number of successes greater than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    /**
     * Test constructor with sample size greater than population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    /**
     * Test method getSampleSize.
     */
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals("Incorrect sample size.", 5, dist.getSampleSize());
    }

    /**
     * Test method getNumberOfSuccesses.
     */
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals("Incorrect number of successes.", 5, dist.getNumberOfSuccesses());
    }

    /**
     * Test method getPopulationSize.
     */
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals("Incorrect population size.", 10, dist.getPopulationSize());
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.cumulativeProbability(x);
            double expectedProb = 0.0;
            if (x == domain[0]) {
                expectedProb = 0.0;
            } else if (x == domain[1]) {
                expectedProb = 1.0;
            } else {
                int m = dist.getNumberOfSuccesses();
                int n = dist.getSampleSize();
                int N = dist.getPopulationSize();
                int k = x;
                expectedProb = SaddlePointExpansion.logBinomialProbability(k,
                        m, (double) n / (double) N, 1.0 - (double) n / (double) N);
            }
            assertEquals("Incorrect cumulative probability for x = " + x,
                    expectedProb, prob, 1e-14);
        }
    }

    /**
     * Test method probability.
     */
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        for (int x = 0; x <= dist.getPopulationSize(); x++) {
            double prob = dist.probability(x);
            double expectedProb = 0.0;
            if (x < dist.getLowerDomain()) {
                expectedProb = 0.0;
            } else if (x >= dist.getUpperDomain()) {
                expectedProb = 0.0;
            } else {
                int m = dist.getNumberOfSuccesses();
                int n = dist.getSampleSize();
                int N = dist.getPopulationSize();
                expectedProb = SaddlePointExpansion.logBinomialProbability(x,
                        m, (double) n / (double) N, 1.0 - (double) n / (double) N);
                expectedProb = Math.exp(expectedProb);
            }
            assertEquals("Incorrect probability for x = " + x,
                    expectedProb, prob, 1e-14);
        }
    }

    /**
     * Test method upperCumulativeProbability.
     */
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

        int[] domain = dist.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double prob = dist.upperCumulativeProbability(x);
            double expectedProb = 1.0;
            if (x == domain[1]) {
                expectedProb = 1.0;
            } else {
                int m = dist.getNumberOfSuccesses();
                int n = dist.getSampleSize();
                int N = dist.getPopulationSize();
                int k = x;
                expectedProb = SaddlePointExpansion.logBinomialProbability(k,
                        m, (double) n / (double) N, 1.0 - (double) n / (double) N);
                expectedProb = Math.exp(expectedProb);
            }
            assertEquals("Incorrect upper cumulative probability for x = " + x,
                    expectedProb, prob, 1e-14);
        }
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double mean = dist.getNumericalMean();
        double expectedMean = 5.0;
        assertEquals("Incorrect numerical mean.", expectedMean, mean, 1e-14);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double variance = dist.getNumericalVariance();
        double expectedVariance = 0.0028346456692913382;
        assertEquals("Incorrect numerical variance.", expectedVariance, variance, 1e-14);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int lowerBound = dist.getSupportLowerBound();
        int expectedLowerBound = 5;
        assertEquals("Incorrect lower bound of support.", expectedLowerBound, lowerBound);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int upperBound = dist.getSupportUpperBound();
        int expectedUpperBound = 5;
        assertEquals("Incorrect upper bound of support.", expectedUpperBound, upperBound);
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        boolean isConnected = dist.isSupportConnected();
        assertTrue("Support is not connected.", isConnected);
    }

}