package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImpl() {
        final double mean = 5.0;
        final double standardDeviation = 2.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(standardDeviation, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double, double)}.
     */
    @Test
    public void testNormalDistributionImplWithAccuracy() {
        final double mean = 5.0;
        final double standardDeviation = 2.0;
        final double inverseCumAccuracy = 1e-6;
        final NormalDistribution dist = new NormalDistributionImpl(mean, standardDeviation, inverseCumAccuracy);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(standardDeviation, dist.getStandardDeviation(), 0.0);
        assertEquals(inverseCumAccuracy, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl()}.
     */
    @Test
    public void testNormalDistributionImplWithDefaults() {
        final NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final double x = 1.5;
        double expected = 0.242004593; // calculated using MathCAD
        assertEquals(expected, dist.density(x), Precision.SAFE_EARPSILON);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double x, p, expected;

        x = 0.0;
        p = 0.5;
        expected = 0.5;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EARPSILON);

        x = 1.0;
        p = 0.841345; // calculated using MathCAD
        expected = 0.842700796;
        assertEquals(expected, dist.cumulativeProbability(x), Precision.SAFE_EARPSILON);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbabilityWithDefaults() {
        final NormalDistribution dist = new NormalDistributionImpl();
        double p, x;

        p = 0.0;
        x = Double.NEGATIVE_INFINITY;
        assertEquals(x, dist.inverseCumulativeProbability(p));

        p = 1.0;
        x = Double.POSITIVE_INFINITY;
        assertEquals(x, dist.inverseCumulativeProbability(p));
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p, x;

        p = 0.25;
        x = -0.802852093; // calculated using MathCAD
        assertEquals(x, dist.inverseCumulativeProbability(p), Precision.SAFE_EARPSILON);

        p = 0.75;
        x = 0.674489746; // calculated using MathCAD
        assertEquals(x, dist.inverseCumulativeProbability(p), Precision.SAFE_EARPSILON);

        p = 0.5;
        x = 0.0;
        assertEquals(x, dist.inverseCumulativeProbability(p), Precision.SAFE_EARPSILON);
    }

    /**
     * Test method for {@link NormalDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final RandomDataGenerator rng = new RandomDataGenerator();
        double s, m;

        s = new StandardDeviation();
        int n = 100000;
        double[] samples = new double[n];
        for (int i = 0; i < n; i++) {
            samples[i] = dist.sample();
        }
        m = s.evaluate(samples);
        assertEquals(1.0, m, 0.03);
    }

    /**
     * Test method for {@link NormalDistributionImpl#getSolverAbsoluteAccuracy()}.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 0.0);
    }
}