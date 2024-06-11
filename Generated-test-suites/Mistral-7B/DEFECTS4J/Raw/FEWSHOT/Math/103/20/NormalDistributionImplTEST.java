package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.01);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double prob = distribution.cumulativeProbability(x);
            double expected = 0.5 * (1 + Erf.erf((x - 0.0) / (1.0 * Math.sqrt(2.0))));
            assertEquals(expected, prob, 0.01);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double expected = 0.0 + p * 2.0 * Math.sqrt(2.0 / Math.PI);
            assertEquals(expected, x, 0.01);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }
}