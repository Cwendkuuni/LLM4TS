package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 10; i++) {
            double prob = dist.probability(i);
            double expected = Gamma.regularizedGammaQ(i + 1, 2.5);
            assertEquals(expected, prob, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 11; i++) {
            double cdf = dist.cumulativeProbability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals(Precision.round(expected, 6), cdf, 1e-6);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 11; i++) {
            double prob = dist.normalApproximateProbability(i);
            double cdf = dist.cumulativeProbability(i);
            double error = Precision.abs(cdf - prob);
            assertTrue(error < 0.01 * Math.max(cdf, prob));
        }
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(2.5);
        int samples[] = new int[1000];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = dist.sample();
        }
        double mean = 0;
        double variance = 0;
        for (int i = 0; i < samples.length; i++) {
            mean += samples[i];
            variance += Math.pow(samples[i] - mean, 2);
        }
        mean /= samples.length;
        variance /= samples.length;
        double expectedMean = 2.5;
        double expectedVariance = 2.5;
        assertEquals(expectedMean, mean, 0.01 * expectedMean);
        assertEquals(expectedVariance, variance, 0.01 * expectedVariance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }
}