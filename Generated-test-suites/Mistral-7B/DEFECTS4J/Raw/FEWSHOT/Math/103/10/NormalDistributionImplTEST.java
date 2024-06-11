package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);

        NormalDistribution distributionWithMeanAndStd = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, distributionWithMeanAndStd.getMean(), 0.001);
        assertEquals(2.5, distributionWithMeanAndStd.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            double expectedCdf = 0.5 * (1.0 + Erf.erf((x - 0.0) / (1.0 * Math.sqrt(2.0))));
            assertEquals(expectedCdf, cdf, 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCdf = distribution.inverseCumulativeProbability(p);
            double expectedInverseCdf = 0.0;
            if (p > 0.5) {
                expectedInverseCdf = Math.sqrt(2.0) * Erf.erfinv(2.0 * p - 1.0) + 0.0;
            } else {
                expectedInverseCdf = Math.sqrt(2.0) * Erf.erfinv(1.0 - 2.0 * p) + 0.0;
            }
            assertEquals(expectedInverseCdf, inverseCdf, 0.01);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }
}