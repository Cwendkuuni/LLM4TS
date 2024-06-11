package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefault() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / 12, dist.getNumericalVariance(), 0.001);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionCustomBounds() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(2.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 && sample <= 2.0);
        }
    }

    @Test
    public void testUniformRealDistributionCustomBoundsInverseCumulativeAccuracy()
            throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0, 0.00001);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(2.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.00001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.00001);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.inverseCumulativeProbability(RandomUtils.nextDouble(0.0, 1.0));
            assertTrue(sample >= 1.0 && sample <= 2.0);
        }
    }

    @Test
    public void testUniformRealDistributionCustomGenerator()
            throws NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1.0, 2.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(2.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 && sample <= 2.0);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds()
            throws NumberIsTooLargeException {
        new UniformRealDistribution(2.0, 1.0);
    }
}