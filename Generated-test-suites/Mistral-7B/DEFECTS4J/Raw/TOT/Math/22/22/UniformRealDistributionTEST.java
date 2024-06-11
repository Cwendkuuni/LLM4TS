package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Unit tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {
    /**
     * Test standard uniform distribution.
     */
    @Test
    public void testStandardUniformDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        RandomGenerator rng = new Well19937c();

        // Test density
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            double density = distribution.density(x);
            double expected = 1 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound());
            assertEquals("Unexpected density at x = " + x, expected, density, 0.0001);
        }

        // Test cumulative probability
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            double cdf = distribution.cumulativeProbability(x);
            double expected = x / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound());
            assertEquals("Unexpected CDF at x = " + x, expected, cdf, 0.0001);
        }

        // Test inverse cumulative probability
        for (double p = 0; p <= 1; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double expected = p * (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()) + distribution.getSupportLowerBound();
            assertEquals("Unexpected inverse CDF at p = " + p, expected, x, distribution.getSolverAbsoluteAccuracy());
        }

        // Test mean
        double mean = 0;
        for (int i = 0; i < 100_000; i++) {
            mean += distribution.sample();
        }
        mean /= 100_000;
        double expected = 0.5 * (distribution.getSupportLowerBound() + distribution.getSupportUpperBound());
        assertEquals("Unexpected mean", expected, mean, 0.0001);

        // Test variance
        double variance = 0;
        for (int i = 0; i < 100_000; i++) {
            double sample = distribution.sample();
            variance += Math.pow(sample - mean, 2);
        }
        variance /= 100_000;
        double expectedVariance = Math.pow(distribution.getSupportUpperBound() - distribution.getSupportLowerBound(), 2) / 12;
        assertEquals("Unexpected variance", expectedVariance, variance, 0.0001);

        // Test sample
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue("Unexpected sample value: " + sample, sample >= distribution.getSupportLowerBound() && sample < distribution.getSupportUpperBound());
        }
    }

    /**
     * Test uniform distribution with custom bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testUniformDistributionWithCustomBoundsAndAccuracy() {
        double lower = 1.0;
        double upper = 3.0;
        double inverseCumulativeProbabilityAccuracy = 1e-7;
        UniformRealDistribution distribution = new UniformRealDistribution(lower, upper, inverseCumulativeProbabilityAccuracy);
        RandomGenerator rng = new Well19937c();

        // Test density
        for (double x = lower; x <= upper; x++) {
            double density = distribution.density(x);
            double expected = 1 / (upper - lower);
            assertEquals("Unexpected density at x = " + x, expected, density, 0.0001);
        }

        // Test cumulative probability
        for (double x = lower; x <= upper; x++) {
            double cdf = distribution.cumulativeProbability(x);
            double expected = (x - lower) / (upper - lower);
            assertEquals("Unexpected CDF at x = " + x, expected, cdf, 0.0001);
        }

        // Test inverse cumulative probability
        for (double p = 0; p <= 1; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double expected = p * (upper - lower) + lower;
            assertEquals("Unexpected inverse CDF at p = " + p, expected, x, distribution.getSolverAbsoluteAccuracy());
        }

        // Test mean
        double mean = 0;
        for (int i = 0; i < 100_000; i++) {
            mean += distribution.sample();
        }
        mean /= 100_000;
        double expected = 2.0;
        assertEquals("Unexpected mean", expected, mean, 0.0001);

        // Test variance
        double variance = 0;
        for (int i = 0; i < 100_000; i++) {
            double sample = distribution.sample();
            variance += Math.pow(sample - mean, 2);
        }
        variance /= 100_000;
        double expectedVariance = Math.pow(upper - lower, 2) / 12;
        assertEquals("Unexpected variance", expectedVariance, variance, 0.0001);

        // Test sample
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue("Unexpected sample value: " + sample, sample >= lower && sample < upper);
        }

        // Test exception when bounds are not valid
        try {
            new UniformRealDistribution(upper, lower);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }
}