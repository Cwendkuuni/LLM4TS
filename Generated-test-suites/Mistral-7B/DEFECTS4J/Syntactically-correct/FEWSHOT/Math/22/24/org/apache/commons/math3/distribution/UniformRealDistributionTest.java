package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1, dist.getSupportUpperBound(), 0.0);
        assertEquals(1.0 / (1 - 0), dist.density(0.5), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() {
        new UniformRealDistribution(1, 1);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.5, 3.5);
        assertEquals(1.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(3.5, dist.getSupportUpperBound(), 0.0);
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distSampled = new UniformRealDistribution(rng, 1.5, 3.5, 0.001);
        assertEquals(1.5, distSampled.sample(), 0.01);
        assertEquals(3.5, distSampled.sample(), 0.01);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(1.5, 3.5, 0.001);
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distSampled = new UniformRealDistribution(rng, 1.5, 3.5, 0.001);
        double p = 0.25;
        double x = distSampled.inverseCumulativeProbability(p);
        assertEquals(2.25, x, 0.01);
        assertEquals(p, dist.cumulativeProbability(x), 0.01);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.getNumericalMean(), 0.01);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0625, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());
    }
}