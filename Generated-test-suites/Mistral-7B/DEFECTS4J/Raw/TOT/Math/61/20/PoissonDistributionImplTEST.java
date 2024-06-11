package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 1.0E-10);
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; ++i) {
            double probability = distribution.probability(i);
            double approximation = Gamma.regularizedGammaP(i + 1, mean);
            assertTrue(Precision.isClose(probability, approximation, 1.0E-8));
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; ++i) {
            double cumulativeProbability = distribution.cumulativeProbability(i);
            double approximation = Gamma.regularizedGammaQ(i + 1, mean);
            assertTrue(Precision.isClose(cumulativeProbability, approximation, 1.0E-8));
        }
    }

    /**
     * Test normalApproximateProbability method.
     */
    @Test
    public void testNormalApproximateProbability() throws Exception {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        final NormalDistribution normal = new NormalDistributionImpl(mean, Math.sqrt(mean));
        for (int i = 0; i <= 10; ++i) {
            double probability = distribution.normalApproximateProbability(i);
            double approximation = normal.cumulativeProbability(i + 0.5);
            assertTrue(Precision.isClose(probability, approximation, 1.0E-6));
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws Exception {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        int sample;
        for (int i = 0; i < 10; ++i) {
            sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= 11); // sample is inclusive
            assertEquals(mean, distribution.getMean(), 1.0E-10);
        }
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i <= 10; ++i) {
            double probability = distribution.cumulativeProbability(i);
            int inverseCumulativeProbability = distribution.inverseCumulativeProbability(probability);
            assertEquals(i, inverseCumulativeProbability);
        }
    }

    /**
     * Test negative mean constructor.
     */
    @Test(expected = MathRuntimeException.class)
    public void testNegativeMeanConstructor() {
        final double mean = -3.14;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test maximum number of iterations.
     */
    @Test(expected = MathException.class)
    public void testMaxIterations() throws Exception {
        final double mean = 3.14;
        final PoissonDistribution distribution = new PoissonDistributionImpl(mean, 1);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }
}