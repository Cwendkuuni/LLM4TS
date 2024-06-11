package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        try {
            UniformRealDistribution dist = new UniformRealDistribution();
            assertEquals(0.5, dist.density(0.5), 0.0001);
            assertEquals(0.0, dist.density(1.0), 0.0001);
            assertEquals(0.0, dist.density(0.0), 0.0001);
            assertTrue(dist.isSupportConnected());
            assertEquals(0.5, dist.getNumericalMean(), 0.0001);
            assertEquals(0.25, dist.getNumericalVariance(), 0.0001);
            assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0001);
            assertEquals(1.0, dist.cumulativeProbability(1.0), 0.0001);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected NumberIsTooLargeException", e);
        }
    }

    @Test
    public void testUniformRealDistributionWithCustomBounds() {
        try {
            UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
            assertEquals(0.0, dist.density(1.0), 0.0001);
            assertEquals(1 / (2.0 - 1.0), dist.density(1.5), 0.0001);
            assertTrue(dist.isSupportConnected());
            assertEquals(1.5, dist.getNumericalMean(), 0.0001);
            assertEquals(0.0625, dist.getNumericalVariance(), 0.0001);
            assertEquals(0.0, dist.cumulativeProbability(1.0), 0.0001);
            assertEquals(1.0, dist.cumulativeProbability(1.5), 0.0001);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected NumberIsTooLargeException", e);
        }
    }

    @Test
    public void testUniformRealDistributionWithCustomBoundsAndGenerator() {
        try {
            RandomGenerator rng = new Well19937c();
            UniformRealDistribution dist = new UniformRealDistribution(rng, 1.0, 2.0);
            assertEquals(0.0, dist.density(1.0), 0.0001, rng);
            assertEquals(1 / (2.0 - 1.0), dist.density(1.5), 0.0001, rng);
            assertTrue(dist.isSupportConnected());
            assertEquals(1.5, dist.getNumericalMean(), 0.0001, rng);
            assertEquals(0.0625, dist.getNumericalVariance(), 0.0001, rng);
            assertEquals(0.0, dist.cumulativeProbability(1.0), 0.0001, rng);
            assertEquals(1.0, dist.cumulativeProbability(1.5), 0.0001, rng);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected NumberIsTooLargeException", e);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        new UniformRealDistribution(1.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10000; ++i) {
            double sample = dist.sample();
            assertEquals(true, dist.isSupportConnected());
            assertEquals(true, dist.isSupportLowerBoundInclusive());
            assertEquals(false, dist.isSupportUpperBoundInclusive());
            assertEquals(dist.getSupportLowerBound(), sample, 0.0001);
            assertEquals(dist.getSupportUpperBound(), 1 + sample, 0.0001);
        }
    }

    @Test
    public void testUniformRealDistributionSampleWithCustomBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (int i = 0; i < 10000; ++i) {
            double sample = dist.sample();
            assertEquals(true, dist.isSupportConnected());
            assertEquals(true, dist.isSupportLowerBoundInclusive());
            assertEquals(false, dist.isSupportUpperBoundInclusive());
            assertEquals(dist.getSupportLowerBound(), sample, 0.0001);
            assertEquals(dist.getSupportUpperBound(), sample + 0.001, 0.001);
        }
    }
}