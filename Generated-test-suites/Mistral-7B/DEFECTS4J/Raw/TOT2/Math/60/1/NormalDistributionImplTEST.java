package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_stdDev1() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertNotNull(dist);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertEquals(0.5, dist.density(0.0), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(1.0, dist.density(1.0), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.0, dist.density(-1.0), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(-1.0), 0.001);

        // test inverseCumulativeProbability with valid probabilities
        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-0.6745, x, 0.001);
        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.6745, x, 0.001);

        // test sample() method
        double sample = dist.sample();
        assertNotNull(sample);

        // test getSolverAbsoluteAccuracy()
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_meanNegativeStdDevNegative() {
        new NormalDistributionImpl(-1.0, -1.0);
    }

    @Test
    public void testNormalDistributionImpl_meanNegativeStdDevPositive() {
        NormalDistribution dist = new NormalDistributionImpl(-1.0, 1.0);
        assertEquals(-1.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertTrue(dist.density(0.0) < 0);
        assertTrue(dist.cumulativeProbability(0.0) < 0.5);

        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        assertTrue(x < 0);
        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(x > 0);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_meanNegativeStdDevZero() {
        new NormalDistributionImpl(-1.0, 0.0);
    }

    @Test
    public void testNormalDistributionImpl_mean0_stdDev0() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 0.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(0.0, dist.getStandardDeviation(), 0.0);
        assertEquals(1.0 / (SQRT2PI * 2), dist.density(0.0), 0.01);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.01);

        // test inverseCumulativeProbability with edge cases
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);
        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
    }

    @Test
    public void testNormalDistributionImpl_meanPositive_stdDevNegative() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, -1.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(-1.0, dist.getStandardDeviation(), 0.0);
        assertTrue(dist.density(0.0) > 0);
        assertTrue(dist.cumulativeProbability(0.0) > 0.5);

        // test inverseCumulativeProbability with edge cases
        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertTrue(x > 0);
        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(x < 0);
    }

    @Test
    public void testNormalDistributionImpl_meanPositive_stdDev1() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 1.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertEquals(0.3989, dist.density(0.0), 0.01);
        assertEquals(0.8413, dist.cumulativeProbability(0.0), 0.01);

        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.075, x, 0.01);
        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(1.25, x, 0.01);

        // test sample() method with edge cases
        double sample = dist.sample();
        assertTrue(sample > -3 && sample < 3);
    }

    @Test
    public void testNormalDistributionImpl_meanNegative_stdDev1() {
        NormalDistribution dist = new NormalDistributionImpl(-1.0, 1.0);
        assertEquals(-1.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
        assertEquals(-0.3989, dist.density(0.0), 0.01);
        assertEquals(0.1587, dist.cumulativeProbability(0.0), 0.01);

        double p = 0.25;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-1.25, x, 0.01);
        p = 0.75;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(-0.75, x, 0.01);

        // test sample() method with edge cases
        double sample = dist.sample();
        assertTrue(sample < -3 || sample > 3);
    }
}