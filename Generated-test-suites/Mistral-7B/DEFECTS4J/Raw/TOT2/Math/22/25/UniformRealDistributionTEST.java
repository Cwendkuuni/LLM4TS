package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert. assertThrows;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.4, 0.01);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
        assertEquals(0.255, dist.getNumericalMean(), 0.001);
        assertEquals(0.00222222222, dist.getNumericalVariance(), 0.0001);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBounds() {
        new UniformRealDistribution(0.5, 0.5);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 1e-9);
        }

        for (double x = 0.0; x < 0.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 1e-9);
        }

        for (double x = 1.0; x > 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 1e-9);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            double expected = x;
            assertEquals(expected, cumProb, 1e-9);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertEquals(true, dist.isInSupport(sample));
            assertEquals(true, dist.density(sample) > 0.0, 0.0);
        }
    }

    /**
     * Test isInSupport method.
     */
    @Test
    public void testIsInSupport() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            boolean inSupport = dist.isInSupport(x);
            assertEquals(true, inSupport);
        }

        for (double x = 0.0; x < 0.0; x += 0.01) {
            boolean inSupport = dist.isInSupport(x);
            assertEquals(false, inSupport);
        }

        for (double x = 1.0; x > 1.0; x += 0.01) {
            boolean inSupport = dist.isInSupport(x);
            assertEquals(false, inSupport);
        }
    }

    /**
     * Test inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double invCumProb = dist.inverseCumulativeProbability(x);
            assertEquals(x, invCumProb, 1e-9);
        }
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }
}