package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)}.
     */
    @Test
    public void testPoissonDistributionImpl() {
        final double mean = 2.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        assertEquals(mean, dist.getMean(), 1e-10);
        assertEquals(mean, dist.expectedValue(), 1e-10);
        assertEquals(mean, dist.getProbabilityMassFunction().expectedValue(), 1e-10);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double)}.
     */
    @Test
    public void testPoissonDistributionImplWithCustomParameters() {
        final double mean = 5.0;
        final double epsilon = 1e-8;
        final int maxIterations = 1000000;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean, epsilon, maxIterations);
        assertEquals(mean, dist.getMean(), 1e-10);
        assertEquals(mean, dist.expectedValue(), 1e-10);
        assertEquals(mean, dist.getProbabilityMassFunction().expectedValue(), 1e-10);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#probability(int)}.
     */
    @Test
    public void testProbability() {
        final double mean = 2.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; i++) {
            double prob = dist.probability(i);
            double expectedProb = MathUtils.exp(-mean) * MathUtils.factorial(i) / Math.pow(Math.E, i * mean);
            assertEquals(expectedProb, prob, 1e-7);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        final double mean = 2.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; i++) {
            double cdf = dist.cumulativeProbability(i);
            double expectedCdf = Gamma.regularizedGammaQ((double) i + 1, mean);
            assertEquals(expectedCdf, cdf, 1e-6);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#normalApproximateProbability(int)}.
     */
    @Test
    public void testNormalApproximateProbability() throws Exception {
        final double mean = 2.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; i++) {
            double probability = dist.normalApproximateProbability(i);
            double error = Math.abs(probability - dist.probability(i));
            assertEquals(0.0, error, 1e-7);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws Exception {
        final int maxIterations = 10000;
        final double mean = 2.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        for (int i = 0; i < maxIterations; i++) {
            int sample = dist.sample();
            assertEquals(Math.round(mean), sample, 1);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     * With negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplWithNegativeMean() {
        new PoissonDistributionImpl(-1.0, 0.0, 1000000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     * With non-positive mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplWithNonPositiveMean() {
        new PoissonDistributionImpl(0.0, 0.0, 1000000);
    }
}