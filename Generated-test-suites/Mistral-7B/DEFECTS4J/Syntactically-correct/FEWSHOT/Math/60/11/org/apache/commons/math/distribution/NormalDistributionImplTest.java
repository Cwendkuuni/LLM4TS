package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl nd = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, nd.getMean(), 0.001);
        assertEquals(1.0, nd.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl nd = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -10; x <= 10; x += 0.1) {
            double density = nd.density(x);
            double expected = 0.3989422804; // Approximate value of density for normal distribution
            assertEquals(expected, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl nd = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5; x <= 5; x += 0.1) {
            double cdf = nd.cumulativeProbability(x);
            double expected = 0.15865525; // Approximate value of cumulative distribution for normal distribution
            assertEquals(expected, cdf, 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl nd = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.05) {
            double x = nd.inverseCumulativeProbability(p);
            double expected = p * 2.0 * 0.316227766; // Approximate value of inverse cumulative distribution for normal distribution
            assertEquals(expected, x, 0.1);
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl nd = new NormalDistributionImpl(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = nd.sample();
            assertEquals(0.0, nd.mean, sample);
            assertEquals(1.0, nd.standardDeviation, Math.abs(sample - nd.mean));
        }
    }
}