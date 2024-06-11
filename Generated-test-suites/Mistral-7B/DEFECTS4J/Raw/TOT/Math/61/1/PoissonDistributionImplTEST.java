package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * Test cases for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double)}.
     */
    @Test
    public void testPoissonDistributionImpl() {
        PoissonDistribution dist = new PoissonDistributionImpl(0.5);
        assertEquals(0.5, dist.getMean(), 0.01);
        verifyDistribution(dist, 100000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double)}.
     */
    @Test
    public void testPoissonDistributionImplWithEpsilon() {
        PoissonDistribution dist = new PoissonDistributionImpl(0.5, 1e-6);
        assertEquals(0.5, dist.getMean(), 0.01);
        verifyDistribution(dist, 100000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     */
    @Test
    public void testPoissonDistributionImplWithMaxIterations() {
        PoissonDistribution dist = new PoissonDistributionImpl(0.5, 1e-6, 10000);
        assertEquals(0.5, dist.getMean(), 0.01);
        verifyDistribution(dist, 100000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     */
    @Test
    public void testPoissonDistributionImplWithMaxIterationsAndEpsilon() {
        PoissonDistribution dist = new PoissonDistributionImpl(0.5, 1e-8, 1000000);
        assertEquals(0.5, dist.getMean(), 0.01);
        verifyDistribution(dist, 100000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#probability(int)}.
     */
    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 6; i++) {
            double expected = FastMath.exp(-2.5) * Gamma.gamma(i + 1) / FastMath.sqrt(FastMath.TWO_PI * (i + 1));
            assertEquals(expected, dist.probability(i), 0.01);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 6; i++) {
            double expected = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals(expected, dist.cumulativeProbability(i), 0.01);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#normalApproximateProbability(int)}.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 6; i++) {
            double expected = new NormalDistributionImpl(2.5, Math.sqrt(2.5)).cumulativeProbability(i + 0.5);
            assertEquals(expected, dist.normalApproximateProbability(i), 0.01);
        }
    }

    /**
     * Test method for {@link PoissonDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertEquals(sample, dist.cumulativeProbability(sample), 0.01);
        }
    }

    /**
     * Test method for constructor with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, int, int)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalMaxIterations() {
        new PoissonDistributionImpl(2.5, 0, 1);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalEpsilon() {
        new PoissonDistributionImpl(2.5, 0, 1000000);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#PoissonDistributionImpl(double, double, int)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalNegativeEpsilon() {
        new PoissonDistributionImpl(2.5, -1e-6, 1000000);
    }

    /**
     * Verify distribution properties.
     *
     * @param dist distribution instance
     * @param n number of samples
     */
    private void verifyDistribution(PoissonDistribution dist, int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += dist.sample();
        }
        double mean = dist.getMean();
        double variance = mean;
        for (int i = 0; i < n; i++) {
            variance += Math.pow(dist.sample() - mean, 2);
        }
        variance /= n;
        assertEquals(mean, variance, 0.01);
        assertEquals(mean, sum / n, 0.01);
    }
}