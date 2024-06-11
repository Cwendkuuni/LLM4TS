package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import java.util.Random;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double density = dist.density(0.0);
        assertEquals(0.39894228040143271, density, 0.0001);

        density = dist.density(1.0);
        assertEquals(0.39894228040143271, density, 0.0001);

        density = dist.density(2.0);
        assertEquals(0.1592625736612502, density, 0.0001);

        density = dist.density(-1.0);
        assertEquals(0.1592625736612502, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double p = 0.0;
        double cdf = dist.cumulativeProbability(p);
        assertEquals(0.0, cdf, 0.0);

        p = 0.5;
        cdf = dist.cumulativeProbability(p);
        assertEquals(0.5, cdf, 0.0);

        p = 1.0;
        cdf = dist.cumulativeProbability(p);
        assertEquals(1.0, cdf, 0.0);

        p = -1.0;
        try {
            cdf = dist.cumulativeProbability(p);
            fail();
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double p = 0.0;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, x);

        p = 0.5;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);

        p = 1.0;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);

        p = 0.1;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(x > 0.0);

        p = 0.9;
        x = dist.inverseCumulativeProbability(p);
        assertTrue(x < 0.0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            double x = random.nextGaussian(0, 1);
            assertEquals(x, sample, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testSampleException() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        dist.sample();
    }
}