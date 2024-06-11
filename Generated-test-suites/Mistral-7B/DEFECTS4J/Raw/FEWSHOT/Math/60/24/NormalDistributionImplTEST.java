package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.special.Erf;

import java.util.Random;

public class NormalDistributionImplTest {
    private static final double DELTA = 1e-6;

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double density = dist.density(x);
            double expected = FastMath.exp(-0.5 * x * x) / (1.0 * FastMath.SQRT_2_PI);
            assertEquals(expected, density, DELTA);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            double erfResult = 0.5 * (1.0 + Erf.erf(x / FastMath.sqrt(2.0)));
            assertEquals(erfResult, cdf, DELTA);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseCdf = dist.inverseCumulativeProbability(p);
            double actualCdf = dist.cumulativeProbability(inverseCdf);
            assertEquals(p, actualCdf, DELTA);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testSample() throws Exception {
        Random random = new Random();
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(Double.isFinite(sample));
            assertEquals(0.0, dist.density(sample), DELTA);
            assertEquals(dist.cumulativeProbability(sample), sample, DELTA);
        }
    }
}