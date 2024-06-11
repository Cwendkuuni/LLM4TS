package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {
    /**
     * Tests creation of Poisson distribution with valid parameters.
     */
    @Test
    public void testCreation() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        assertNotNull(dist);
    }

    /**
     * Tests creation of Poisson distribution with negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreationNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Tests probability mass function for valid input.
     */
    @Test
    public void testProbabilityMassFunction() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double prob = dist.probability(i);
            double expected = Math.exp(-1.0) * Math.pow(1.0, i) / Math.sqrt(2 * Math.PI);
            assertEquals("Probability for i = " + i, expected, prob, 0.001);
        }
    }

    /**
     * Tests probability mass function for invalid input.
     */
    @Test
    public void testProbabilityMassFunctionInvalidInput() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 10; i++) {
            double prob = dist.probability(i);
            assertEquals("Probability for i < 0 should be 0", 0, prob, 0.001);
        }
        PoissonDistribution dist2 = new PoissonDistributionImpl(1.0);
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 10; i--) {
            double prob = dist2.probability(i);
            assertEquals("Probability for i > max int should be 0", 0, prob, 0.001);
        }
    }

    /**
     * Tests cumulative probability function for valid input.
     */
    @Test
    public void testCumulativeProbabilityFunction() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double cdf = dist.cumulativeProbability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 1.0);
            assertEquals("CDF for i = " + i, expected, cdf, 0.001);
        }
    }

    /**
     * Tests cumulative probability function for invalid input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityFunctionInvalidInput() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        dist.cumulativeProbability(-1);
    }

    /**
     * Tests normalApproximateProbability function for valid input.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double approxProb = dist.normalApproximateProbability(i);
            double trueProb = dist.probability(i);
            assertEquals("Approximate probability for i = " + i + " should be close to true probability",
                    trueProb, approxProb, 0.01);
        }
    }

    /**
     * Tests normalApproximateProbability function for invalid input.
     */
    @Test
    public void testNormalApproximateProbabilityInvalidInput() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = Integer.MIN_VALUE; i < Integer.MIN_VALUE + 10; i++) {
            try {
                dist.normalApproximateProbability(i);
                fail("Normal approximation probability for i < 0 should not be computable");
            } catch (MathException e) {
                // expected
            }
        }
        PoissonDistribution dist2 = new PoissonDistributionImpl(1.0);
        for (int i = Integer.MAX_VALUE; i > Integer.MAX_VALUE - 10; i--) {
            try {
                dist2.normalApproximateProbability(i);
                fail("Normal approximation probability for i > max int should not be computable");
            } catch (MathException e) {
                // expected
            }
        }
    }

    /**
     * Tests sample function for valid input.
     */
    @Test
    public void testSampleFunction() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        RandomGenerator random = new MersenneTwister();
        for (int i = 0; i < 1000; i++) {
            int sample = dist.sample(random);
            assertTrue("Sample should be non-negative integer", sample >= 0);
            assertEquals("Sample should be close to mean", 1.0, dist.cumulativeProbability(sample), 0.001);
        }
    }

    /**
     * Tests sample function with large mean.
     */
    @Test
    public void testSampleFunctionLargeMean() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(10000.0);
        RandomGenerator random = new MersenneTwister();
        for (int i = 0; i < 1000; i++) {
            int sample = dist.sample(random);
            assertTrue("Sample should be non-negative integer", sample >= 0);
            assertEquals("Sample should be close to mean", 10000.0, dist.cumulativeProbability(sample), 0.001);
        }
    }

    /**
     * Tests sample function with negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleFunctionNegativeMean() {
        PoissonDistribution dist = new PoissonDistributionImpl(-1.0);
        dist.sample(new MersenneTwister());
    }

    /**
     * Tests inverseCumulativeProbability function for valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityFunction() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double p = dist.cumulativeProbability(i);
            int inverseCdf = dist.inverseCumulativeProbability(p);
            assertEquals("Inverse CDF for p = " + p + " should be i = " + i, i, inverseCdf, 0);
        }
    }

    /**
     * Tests inverseCumulativeProbability function for invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityFunctionInvalidInput() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        dist.inverseCumulativeProbability(1.1);
    }
}