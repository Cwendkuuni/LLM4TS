package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class UniformRealDistributionTest {
    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
       assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.0001);
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample(rng);
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() throws NumberIsTooLargeException {
        new UniformRealDistribution(1.0, 0.0);
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomBoundsAndAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3, 1e-5);
        assertEquals(0.1, dist.getSupportLowerBound(), 1e-5);
        assertEquals(0.3, dist.getSupportUpperBound(), 1e-5);
        assertEquals(3.0 / (0.3 - 0.1), dist.density(0.2), 0.0001);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertEquals(0.0, dist.cumulativeProbability(0.0), 1e-9);
        assertEquals(0.25, dist.cumulativeProbability(0.25), 1e-9);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 1e-9);
        assertEquals(1.0, dist.cumulativeProbability(1.0), 1e-9);
    }

    @Test
    public void testUniformRealDistributionMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertEquals(0.3, dist.getNumericalMean(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertEquals(0.0014285714285714286, dist.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        RandomGenerator rng = new Well19937c();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample(rng);
            assertTrue(sample >= 0.1 && sample <= 0.5);
        }
    }

    @Test
    public void testUniformRealDistributionSampleUsingRandomUtils() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        for (int i = 0; i < 1000; i++) {
            double sample = RandomUtils.nextDouble(0.1, 0.5);
            assertTrue(sample >= 0.1 && sample <= 0.5);
        }
    }
}