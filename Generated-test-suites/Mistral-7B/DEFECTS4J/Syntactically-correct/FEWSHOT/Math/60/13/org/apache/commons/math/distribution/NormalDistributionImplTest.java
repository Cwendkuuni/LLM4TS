package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);

        for (double x = -10; x <= 10; x++) {
            double density = dist.density(x);
            double expectedDensity = FastMath.exp(-0.5 * Math.pow(x, 2)) / (Math.sqrt(2 * Math.PI));
            assertEquals(expectedDensity, density, 1e-5);
        }

        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        double expectedX = FastMath.erfinv(2 * p - 1) * Math.sqrt(2) + dist.getMean();
        assertEquals(expectedX, x, 1e-5);

        double q = dist.cumulativeProbability(x);
        assertEquals(p, q, 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityBelowZero() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityAboveOne() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(2);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityBelowZero() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityAboveOne() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(2);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= -3 && sample <= 3);
        }
    }
}