package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.0);
        assertEquals(0.0, dist.density(0.4), 0.0);
        assertEquals(0.0, dist.density(1.1), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(0.1), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(0.9), 0.0);
        assertEquals(0.5, dist.sample(), 0.0);
        assertEquals(0.5, dist.sample(), 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstruction() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1.5, 2.5);
        assertEquals(1.5, dist.getLowerBound(), 0.0);
        assertEquals(2.5, dist.getUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.20833333333333332, dist.getNumericalVariance(), 0.0);
        assertEquals(0.015277777777777778, dist.density(1.7), 0.0);
        assertEquals(0.0, dist.density(1.3), 0.0);
        assertEquals(0.0, dist.density(3.0), 0.0);
        assertEquals(0.3333333333333333, dist.cumulativeProbability(2.0), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(1.4), 0.0);
        assertEquals(0.6666666666666666, dist.cumulativeProbability(2.6), 0.0);
        assertEquals(1.9583333333333333, dist.sample(), 0.0);
        assertEquals(1.9583333333333333, dist.sample(), 0.0);
    }

    @Test
    public void testUniformRealDistributionRandomSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        RandomGenerator rng = new Well19937c();
        dist.setSeed(rng.nextLong());
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }
}