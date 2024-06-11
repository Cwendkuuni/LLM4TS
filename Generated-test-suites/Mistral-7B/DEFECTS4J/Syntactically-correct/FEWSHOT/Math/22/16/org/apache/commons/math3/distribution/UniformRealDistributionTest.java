package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test(expected = NumberIsTooLargeException.class)
    public void testNegativeLowerBound() {
        new UniformRealDistribution(-1, 1, 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUpperLowerEqual() {
        new UniformRealDistribution(1, 1, 0.001);
    }

    @Test
    public void testStandardUniform() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 100000; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x <= 1);
        }
    }

    @Test
    public void testSampleBelowLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x <= 1);
        }
    }

    @Test
    public void testSampleAboveUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x < 1);
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x < 1.1; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1 / (1 - 0), density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1.1; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            if (x <= 0) {
                assertEquals(0, cdf, 0.001);
            } else if (x >= 1) {
                assertEquals(1, cdf, 0.001);
            } else {
                assertEquals(x, cdf, 0.001);
            }
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double p = 0; p <= 1.01; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            assertEquals(p, dist.cumulativeProbability(x), 0.001);
        }
    }

    @Test
    public void testMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.00025, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 10000; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x < 1);
        }
    }
}