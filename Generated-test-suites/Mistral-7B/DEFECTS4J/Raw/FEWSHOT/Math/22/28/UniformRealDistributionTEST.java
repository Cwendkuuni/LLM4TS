package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;

import org.junit.Test;

public class UniformRealDistributionTest {

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(5.0, 4.0);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(1, distribution.getSupportLowerBound(), 0.001);
        assertEquals(3, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithCustomBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3, 0.0001);
        assertEquals(1, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(3, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testConstructorWithCustomRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithCustomRandomGeneratorAndAccuracy() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0, 1, 0.0001);
        assertEquals(0, distribution.getSupportLowerBound(), 0.0001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        for (double x = 1; x < 3; x += 0.1) {
            assertEquals(1.0 / (3 - 1), distribution.density(x), 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        for (double x = 1; x < 3; x += 0.1) {
            assertEquals(x / (3 - 1), distribution.cumulativeProbability(x), 0.01);
        }
        assertEquals(0, distribution.cumulativeProbability(0), 0.01);
        assertEquals(1, distribution.cumulativeProbability(3), 0.01);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        assertEquals(0.5625, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 3);
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 1 && sample < 3);
        }
    }

    @Test
    public void testSampleWithCustomRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 1, 3);
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 1 && sample < 3);
        }
    }
}