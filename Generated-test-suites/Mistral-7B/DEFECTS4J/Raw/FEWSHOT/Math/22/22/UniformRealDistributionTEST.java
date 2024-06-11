package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import org.junit.Test;

public class UniformRealDistributionTest {
    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.density(0), 1e-9);
        assertEquals(0, dist.density(1), 1e-9);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 1e-9);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5 + 1e-12), 1e-9);
        assertEquals(0, dist.density(1.0), 1e-9);

        UniformRealDistribution dist1 = new UniformRealDistribution(1.0, 2.0);
        assertEquals(0, dist1.density(1.0), 1e-9);
        assertEquals(0, dist1.density(2.0), 1e-9);
        assertEquals(1.0 / (2.0 - 1.0), dist1.density(1.5), 1e-9);

        UniformRealDistribution dist2 = new UniformRealDistribution(-1.0, 1.0);
        assertEquals(0, dist2.density(-1.0), 1e-9);
        assertEquals(0, dist2.density(1.0), 1e-9);
        assertEquals(1.0 / (1.0 + 1.0), dist2.density(-0.5), 1e-9);

        UniformRealDistribution dist3 = new UniformRealDistribution(0.0, 0.0);
        assertEquals(0, dist3.density(0.0), 1e-9);
        assertEquals(Double.NaN, dist3.density(-1.0), 0);
        assertEquals(Double.NaN, dist3.density(1.0), 0);

        UniformRealDistribution dist4 = new UniformRealDistribution(1.0, 1.0);
        assertEquals(1.0 / (1.0 - 1.0), dist4.density(0.0), 1e-9);
        assertEquals(1.0 / (1.0 - 1.0), dist4.density(1.0 - 1e-12), 1e-9);
        assertEquals(Double.NaN, dist4.density(1.0), 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.cumulativeProbability(0), 1e-9);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 1e-9);
        assertEquals(1, dist.cumulativeProbability(1), 1e-9);

        UniformRealDistribution dist1 = new UniformRealDistribution(1.0, 2.0);
        assertEquals(0, dist1.cumulativeProbability(1.0), 1e-9);
        assertEquals(0.5, dist1.cumulativeProbability(1.5), 1e-9);
        assertEquals(1, dist1.cumulativeProbability(2.0), 1e-9);

        UniformRealDistribution dist2 = new UniformRealDistribution(-1.0, 1.0);
        assertEquals(0.5, dist2.cumulativeProbability(-0.5), 1e-9);
        assertEquals(1, dist2.cumulativeProbability(0.5), 1e-9);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound() && sample <= dist.getSupportUpperBound());
        }

        UniformRealDistribution dist1 = new UniformRealDistribution(1.0, 2.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist1.sample();
            assertTrue(sample >= dist1.getSupportLowerBound() && sample <= dist1.getSupportUpperBound());
        }

        UniformRealDistribution dist2 = new UniformRealDistribution(-1.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist2.sample();
            assertTrue(sample >= dist2.getSupportLowerBound() && sample <= dist2.getSupportUpperBound());
        }

        UniformRealDistribution dist3 = new UniformRealDistribution(0.0, 0.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist3.sample();
            assertEquals(0.0, sample, 1e-9);
        }

        UniformRealDistribution dist4 = new UniformRealDistribution(1.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist4.sample();
            assertEquals(0.5, sample, 1e-9);
        }

        UniformRealDistribution dist5 = new UniformRealDistribution(RandomUtils.nextDouble(0.1), RandomUtils.nextDouble(0.9));
        for (int i = 0; i < 1000; i++) {
            double sample = dist5.sample();
            assertTrue(sample >= dist5.getSupportLowerBound() && sample <= dist5.getSupportUpperBound());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        new UniformRealDistribution(1.0, 0.0);
    }
}