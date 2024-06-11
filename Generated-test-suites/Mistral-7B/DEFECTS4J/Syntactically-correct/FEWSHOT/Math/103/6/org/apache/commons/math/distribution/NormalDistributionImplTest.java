package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.01);
        assertEquals(1.0, dist.getStandardDeviation(), 0.01);
    }

    @Test
    public void testNormalDistributionImplWithCustomParams() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, dist.getMean(), 0.01);
        assertEquals(2.5, dist.getStandardDeviation(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double expectedCumulativeProb = 0.5 * (1.0 + Erf.erf((x - dist.getMean()) / (dist.getStandardDeviation() * Math.sqrt(2.0))));
            double actualCumulativeProb = dist.cumulativeProbability(x);
            assertEquals(expectedCumulativeProb, actualCumulativeProb, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithLargeAbsoluteValue() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double x = 100 * dist.getStandardDeviation();
        dist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double expectedX = dist.getMean() + dist.getStandardDeviation() * Erf.erfinv(2 * p - 1);
            double actualX = dist.inverseCumulativeProbability(p);
            assertEquals(expectedX, actualX, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(-1.0);
    }
}