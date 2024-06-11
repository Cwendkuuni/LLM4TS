package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.RandomDataGenerator;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertTrue(dist instanceof NormalDistributionImpl);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989423, dist.density(0), 0.001);
        assertEquals(0.15430803, dist.density(1), 0.001);
        assertEquals(0.0, dist.density(-3), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.841345, dist.cumulativeProbability(1), 0.001);
        assertEquals(0, dist.cumulativeProbability(-3), 0.001);
        assertEquals(1, dist.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(-1.2821, dist.inverseCumulativeProbability(0.1587), 0.01);
        assertEquals(0.5, dist.inverseCumulativeProbability(0.5), 0.01);
        assertEquals(1.2821, dist.inverseCumulativeProbability(0.8413), 0.01);
        assertNaN(dist.inverseCumulativeProbability(1.2));
        assertNaN(dist.inverseCumulativeProbability(-1));
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        RandomDataGenerator rdg = new RandomDataGenerator();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertEquals(0, rdg.nextInt(2), sample < 0 ? -1 : 1);
            assertEquals(0, rdg.nextInt((int) (2 * Math.pow(10, 6))),
                    Math.abs(sample - dist.mean) % 1000000, 0);
        }
    }

    private void assertNaN(Double d) {
        if (d == null || Double.isNaN(d)) {
            return;
        }
        fail("Expected NaN, but got " + d);
    }
}