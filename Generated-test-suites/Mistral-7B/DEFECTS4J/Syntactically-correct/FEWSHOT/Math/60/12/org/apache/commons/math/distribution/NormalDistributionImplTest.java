package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_Mean0StdDev1() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0, distribution.getMean(), 0.001);
        assertEquals(1, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanPositiveStdDevPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, distribution.getMean(), 0.001);
        assertEquals(2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanNegativeStdDevPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(-1, 2);
        assertEquals(-1, distribution.getMean(), 0.001);
        assertEquals(2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanPositiveStdDevNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(1, -2);
        assertEquals(1, distribution.getMean(), 0.001);
        assertEquals(-2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_MeanNegativeStdDevNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(-1, -2);
        assertEquals(-1, distribution.getMean(), 0.001);
        assertEquals(-2, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_Sample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        double mean = distribution.getMean();
        double stdDev = distribution.getStandardDeviation();
        double expected = mean + stdDev * Math.random();
        assertTrue(Math.abs(sample - expected) < 0.05);
    }

    @Test
    public void testNormalDistributionImpl_CumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double expected = 0.5;
        double actual = distribution.cumulativeProbability(0);
        assertEquals(expected, actual, 0.001);

        p = 0.75;
        expected = 0.75;
        actual = distribution.cumulativeProbability(0.6745);
        assertEquals(expected, actual, 0.001);

        p = 0.25;
        expected = 0.25;
        actual = distribution.cumulativeProbability(-0.6745);
        assertEquals(expected, actual, 0.001);

        p = 1 - 1e-9;
        expected = 1;
        actual = distribution.cumulativeProbability(50);
        assertEquals(expected, actual, 0.001);

        p = 0;
        expected = 0;
        actual = distribution.cumulativeProbability(-50);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_CumulativeProbability_OutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.cumulativeProbability(100);
    }

    @Test
    public void testNormalDistributionImpl_InverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double expected = 0;
        double actual = distribution.inverseCumulativeProbability(expected);
        assertEquals(p, actual, 0.001);

        p = 0.75;
        expected = 0.6745;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 0.25;
        expected = -0.6745;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 1 - 1e-9;
        expected = 50;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 0;
        expected = -50;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.001);

        p = 2;
        expected = Double.POSITIVE_INFINITY;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual);

        p = -2;
        expected = Double.NEGATIVE_INFINITY;
        actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_InverseCumulativeProbability_OutOfRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }
}