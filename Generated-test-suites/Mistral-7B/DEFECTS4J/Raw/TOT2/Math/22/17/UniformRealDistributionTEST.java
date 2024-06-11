package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for UniformRealDistribution.
 */
public class UniformRealDistributionTest {
    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
        assertEquals(false, dist.isSupportUpperBoundInclusive());
        assertEquals(true, dist.isSupportConnected());
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.2, 0.6, 0.0001);
        assertEquals(0.2, dist.getLowerBound(), 0.0001);
        assertEquals(0.6, dist.getUpperBound(), 0.0001);
        assertEquals(0.4, dist.getNumericalMean(), 0.0001);
        assertEquals(0.02222222222222222, dist.getNumericalVariance(), 0.00001);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
        assertEquals(false, dist.isSupportUpperBoundInclusive());
        assertEquals(true, dist.isSupportConnected());
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.2 && sample < 0.6);
        }
    }

    /**
     * Test constructor with incorrect parameters.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testIncorrectConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 0.5);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            double expected = 1.0 / (dist.getUpperBound() - dist.getLowerBound());
            assertEquals(expected, density, 0.0001);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.01) {
            double prob = dist.cumulativeProbability(x);
            double expected = x - dist.getLowerBound();
            assertEquals(expected, prob, 0.0001);
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound() && sample < dist.getUpperBound());
        }
    }
}