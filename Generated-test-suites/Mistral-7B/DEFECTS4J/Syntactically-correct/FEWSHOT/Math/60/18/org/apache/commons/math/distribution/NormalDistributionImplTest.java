package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);

        NormalDistribution dist2 = new NormalDistributionImpl(1, 2);
        assertEquals(1, dist2.getMean(), 0.001);
        assertEquals(2, dist2.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double x = -10; x <= 10; x += 0.1) {
            double density = dist.density(x);
            if (x == 0) {
                assertEquals(0.39894228040143272, density, 0.0001);
            } else {
                assertTrue(density > 0);
            }
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double x = -5; x <= 5; x += 0.1) {
            double p = dist.cumulativeProbability(x);
            if (x == 0) {
                assertEquals(0.5, p, 0.0001);
            } else if (x > 0) {
                assertTrue(p > 0);
            } else {
                assertTrue(p < 0);
            }
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (double p = 0; p <= 1; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            if (p == 0) {
                assertEquals(-1.28155200381445, x, 0.01);
            } else if (p == 1) {
                assertEquals(1.28155200381445, x, 0.01);
            } else {
                assertTrue(x > -5);
                assertTrue(x < 5);
            }
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        for (int i = 0; i < 100; i++) {
            double x = dist.sample();
            assertTrue(x >= -3);
            assertTrue(x <= 3);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testInvalidProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-1);
    }
}