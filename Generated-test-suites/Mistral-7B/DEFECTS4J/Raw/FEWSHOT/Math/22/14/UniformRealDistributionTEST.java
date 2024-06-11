package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Test;

public class UniformRealDistributionTest {
    @Test(expected = NumberIsTooLargeException.class)
    public void testInitializationWithInvalidBounds() {
        new UniformRealDistribution(1, 1);
    }

    @Test
    public void testGetters() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4, 1e-7);
        assertEquals(0.1, urd.getLowerBound(), 1e-8);
        assertEquals(0.4, urd.getUpperBound(), 1e-8);
        assertEquals(1e-7, urd.getSolverAbsoluteAccuracy(), 1e-8);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        for (double d = 0.1; d < 0.4; d += 0.001) {
            assertEquals(1.0 / (0.4 - 0.1), urd.density(d), 1e-7);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        for (double d = 0.1; d <= 0.4; d += 0.001) {
            assertEquals(d - 0.1, urd.cumulativeProbability(d), 1e-7);
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        double eps = 1e-7;
        for (int i = 0; i < 100; i++) {
            double u = RandomUtils.nextDouble();
            double x = urd.inverseCumulativeProbability(u);
            assertTrue(x >= 0.1 && x <= 0.4);
            assertEquals(u, urd.cumulativeProbability(x), eps);
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.25, urd.getNumericalMean(), 1e-7);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.00042857142857142853, urd.getNumericalVariance(), 1e-7);
    }

    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        for (int i = 0; i < 1000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= 0.1 && sample <= 0.4);
        }
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertTrue(urd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertFalse(urd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testSampleUsingGivenRandomNumberGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(rng, 0.1, 0.4, 1e-7);
        for (int i = 0; i < 1000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= 0.1 && sample <= 0.4);
        }
    }

}