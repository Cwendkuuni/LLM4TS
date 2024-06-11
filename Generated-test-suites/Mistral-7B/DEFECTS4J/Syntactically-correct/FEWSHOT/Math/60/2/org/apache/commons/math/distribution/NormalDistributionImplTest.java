package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
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
        assertEquals(0.39894228040143271, density, 0.00000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894621366197, p, 0.00000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.25;
        double z = dist.inverseCumulativeProbability(p);
        assertEquals(0.674489750166064, z, 0.00000001);

        p = 0.75;
        z = dist.inverseCumulativeProbability(p);
        assertEquals(-0.674489750166064, z, 0.00000001);

        p = 0.0;
        try {
            z = dist.inverseCumulativeProbability(p);
            fail();
        } catch (MathException e) {
            // expected
        }

        p = 1.0;
        try {
            z = dist.inverseCumulativeProbability(p);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals(0.0, sample, 0.02);
    }
}