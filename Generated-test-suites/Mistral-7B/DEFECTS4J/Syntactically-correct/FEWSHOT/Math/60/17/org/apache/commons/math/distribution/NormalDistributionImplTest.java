package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import static org.apache.commons.math3.Math.PI;
import static org.apache.commons.math3.special.Erf.erf;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.RandomDataGenerator;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 1e-6);
        assertEquals(1, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(1, 1);
        assertEquals(1, dist.getMean(), 1e-6);
        assertEquals(1, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(-1, 1);
        assertEquals(-1, dist.getMean(), 1e-6);
        assertEquals(1, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(0, 2);
        assertEquals(0, dist.getMean(), 1e-6);
        assertEquals(2, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(3, 0.5);
        assertEquals(3, dist.getMean(), 1e-6);
        assertEquals(0.5, dist.getStandardDeviation(), 1e-6);

        dist = new NormalDistributionImpl(Math.PI, 1);
        assertNaN("Mean of NormalDistributionImpl with PI as mean should be NaN", dist.getMean());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double density;

        density = dist.density(0);
        assertEquals(0.398942280401013, density, 1e-6);

        density = dist.density(1);
        assertEquals(0.398942280401013, density, 1e-6);

        density = dist.density(-1);
        assertEquals(0.398942280401013, density, 1e-6);

        density = dist.density(2);
        assertEquals(0.120405743927371, density, 1e-6);

        density = dist.density(-2);
        assertEquals(0.120405743927371, density, 1e-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        assertEquals(0.5, dist.cumulativeProbability(0), 1e-6);
        assertEquals(0.841344746, dist.cumulativeProbability(1), 1e-6);
        assertEquals(0.158655254, dist.cumulativeProbability(-1), 1e-6);
        assertEquals(0, dist.cumulativeProbability(-3), 1e-6);
        assertEquals(1, dist.cumulativeProbability(3), 1e-6);

        dist = new NormalDistributionImpl(1, 1);

        assertEquals(0.158655254, dist.cumulativeProbability(0), 1e-6);
        assertEquals(0.841344746, dist.cumulativeProbability(1), 1e-6);
        assertEquals(1, dist.cumulativeProbability(2), 1e-6);
        assertEquals(0, dist.cumulativeProbability(-2), 1e-6);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        assertEquals(0, dist.inverseCumulativeProbability(0), 1e-6);
        assertEquals(-0.535691428, dist.inverseCumulativeProbability(0.158655254), 1e-6);
        assertEquals(0.535691428, dist.inverseCumulativeProbability(0.841344746), 1e-6);
        assertEquals(1.5, dist.inverseCumulativeProbability(0.95), 1e-6);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(1), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(2), 1e-6);

        dist = new NormalDistributionImpl(1, 1);

        assertEquals(1, dist.inverseCumulativeProbability(0), 1e-6);
        assertEquals(0.535691428, dist.inverseCumulativeProbability(0.158655254), 1e-6);
        assertEquals(-0.535691428, dist.inverseCumulativeProbability(0.841344746), 1e-6);
        assertEquals(-1.5, dist.inverseCumulativeProbability(0.05), 1e-6);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(2), 1e-6);

        dist = new NormalDistributionImpl(0, 2);

        assertEquals(-1, dist.inverseCumulativeProbability(0), 1e-6);
        assertEquals(-0.535691428, dist.inverseCumulativeProbability(0.158655254), 1e-6);
        assertEquals(0.535691428, dist.inverseCumulativeProbability(0.841344746), 1e-6);
        assertEquals(3, dist.inverseCumulativeProbability(0.95), 1e-6);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(1), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(4), 1e-6);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityConvergenceError() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(0.6);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        RandomDataGenerator rdg = dist.getRandomGenerator();

        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue("Sample is not within range", sample >= -3 && sample <= 3);
        }
    }
}