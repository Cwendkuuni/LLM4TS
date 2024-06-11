package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);

        NormalDistribution distZeroMeanOneStdDev = new NormalDistributionImpl();
        assertEquals(0.0, distZeroMeanOneStdDev.getMean(), 0.001);
        assertEquals(1.0, distZeroMeanOneStdDev.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMeanSetter() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(2.5);
        assertEquals(2.5, dist.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMeanNegative() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(-1.5);
    }

    @Test
    public void testStandardDeviationSetter() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(3.0);
        assertEquals(3.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardDeviationZero() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(2.0), 0.001);

        NormalDistribution distMean2 = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(0.15865525, distMean2.cumulativeProbability(-1.0), 0.001);
        assertEquals(0.5, distMean2.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, distMean2.cumulativeProbability(1.0), 0.001);
        assertEquals(1.0, distMean2.cumulativeProbability(2.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityLargeValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeValue() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.288667184, dist.inverseCumulativeProbability(0.841344746), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.2));

        NormalDistribution distMean2 = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(-1.0, distMean2.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(-0.288667184, distMean2.inverseCumulativeProbability(0.15865525), 0.001);
        assertEquals(1.288667184, distMean2.inverseCumulativeProbability(0.841344746), 0.001);
        assertEquals(2.2, distMean2.inverseCumulativeProbability(0.95), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfBounds() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testInverseCumulativeProbabilityZeroAndOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.001);
    }
}