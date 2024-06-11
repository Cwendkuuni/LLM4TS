package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        new UniformRealDistribution(1, 0);
    }

    @Test
    public void testUniformRealDistributionWithCustomParameters() {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3, 0.0001);
        assertEquals(1, dist.getSupportLowerBound(), 0.001);
        assertEquals(3, dist.getSupportUpperBound(), 0.001);
        assertEquals(2, dist.getNumericalMean(), 0.001);
        assertEquals(0.41666666666666664, dist.getNumericalVariance(), 0.0001);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);

        for (double i = 0; i <= 1; i += 0.01) {
            assertEquals(1, dist.density(i), 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);

        for (double i = 0; i <= 1; i += 0.01) {
            assertEquals(i, dist.cumulativeProbability(i), 0.01);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 100000; i++) {
            assertEquals(true, dist.isSupportConnected());
            assertEquals(0, dist.sample(), 0.001);
            assertEquals(1, dist.sample(), 0.001);
        }
    }

    @Test
    public void testSampleWithCustomParameters() {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3, new Well19937c());
        for (int i = 0; i < 100000; i++) {
            assertEquals(true, dist.isSupportConnected());
            assertTrue(dist.sample() >= 1);
            assertTrue(dist.sample() <= 3);
        }
    }

    @Test
    public void testSamplingWithSavedSeed() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double savedValue = dist.sample();
        RandomGenerator rng = dist.getRandomGenerator();
        rng.setSeed(rng.nextLong());
        assertEquals(savedValue, dist.sample(), 0.001);
    }
}