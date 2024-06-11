package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Tests for the {@link PoissonDistributionImpl} class.
 */
public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)}
     * with positive mean.
     */
    @Test
    public void testPoissonDistributionImplPositiveMean() {
        final double mean = 3.0;
        PoissonDistribution dist = new PoissonDistributionImpl(mean);
        // Test probability mass function
        for (int x = 0; x <= 10; x++) {
            double p = dist.probability(x);
            double np = Gamma.regularizedGammaP(x + 1, mean);
            assertEquals(np, p, 1e-12);
        }
        // Test cumulative probability function
        for (int x = 0; x <= 10; x++) {
            double cp = dist.cumulativeProbability(x);
            double np = Gamma.regularizedGammaQ(x + 1, mean);
            assertEquals(np, cp, 1e-12);
        }
        // Test normal approximation
        for (int x = 0; x <= 10; x++) {
            double np = dist.normalApproximateProbability(x);
            double err = Math.abs(np - dist.probability(x));
            assertTrue(err <= 1e-5);
        }
        // Test sampling
        for (int i = 0; i < 1000; i++) {
            int x = dist.sample();
            assertEquals(mean, dist.getMean(), 1e-6);
            assertTrue(x >= 0 && x <= 1000 * mean);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double)}
     * with positive mean and custom convergence criterion and maximum number of iterations.
     */
    @Test
    public void testPoissonDistributionImplCustomSettings() {
        final double mean = 5.0;
        final double epsilon = 1e-8;
        final int maxIterations = 20000;
        PoissonDistribution dist = new PoissonDistributionImpl(mean, epsilon, maxIterations);
        // Test probability mass function
        for (int x = 0; x <= 10; x++) {
            double p = dist.probability(x);
            double np = Gamma.regularizedGammaP(x + 1, mean);
            assertEquals(np, p, Precision.safeEpsilon);
        }
        // Test cumulative probability function
        for (int x = 0; x <= 10; x++) {
            double cp = dist.cumulativeProbability(x);
            double np = Gamma.regularizedGammaQ(x + 1, mean);
            assertEquals(np, cp, Precision.safeEpsilon);
        }
        // Test normal approximation
        for (int x = 0; x <= 10; x++) {
            double np = dist.normalApproximateProbability(x);
            double err = Math.abs(np - dist.probability(x));
            assertTrue(err <= Precision.SAFE_EPSILON);
        }
        // Test sampling
        for (int i = 0; i < 1000; i++) {
            int x = dist.sample();
            assertEquals(mean, dist.getMean(), 1e-6);
            assertTrue(x >= 0 && x <= 1000 * mean);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)}
     * with negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}
     * with negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMeanCustomSettings() {
        new PoissonDistributionImpl(-1.0, 1e-8, 10000000);
    }
}