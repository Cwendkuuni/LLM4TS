package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.density(0.5), 0.001);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertTrue(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        double lower = 1.0;
        double upper = 2.0;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        assertEquals(lower, dist.getLowerBound(), 0.0);
        assertEquals(upper, dist.getUpperBound(), 0.0);
        assertEquals(1.0 / (upper - lower), dist.density(1.5), 0.001);
        assertFalse(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test constructor with custom bounds and solver accuracy.
     */
    @Test
    public void testCustomConstructorWithSolverAccuracy() {
        double lower = -1.0;
        double upper = 1.0;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper, 0.0001);
        assertEquals(lower, dist.getLowerBound(), 0.0);
        assertEquals(upper, dist.getUpperBound(), 0.0);
        // Check that inverse cumulative probability is accurate
        double p = 0.2;
        double x = dist.inverseCumulativeProbability(p);
        double expected = (lower + upper) / 2.0 + p * (upper - lower) / 2.0;
        assertEquals(expected, x, 0.0001);
        // Check that density is correct at the inverse cumulative probability
        assertEquals(1.0 / (upper - lower), dist.density(x), 0.001);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(1.0, 1.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.1) {
            assertEquals(1.0 / (dist.getUpperBound() - dist.getLowerBound()), dist.density(x), 0.01);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.1) {
            double p = dist.cumulativeProbability(x);
            assertEquals(x, dist.inverseCumulativeProbability(p), 0.01);
        }
    }

    /**
     * Test inverseCumulativeProbability method with different precisions.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double p = 0.25;
        double x1 = dist.inverseCumulativeProbability(p, 0.01);
        double x2 = dist.inverseCumulativeProbability(p, 0.001);
        double x3 = dist.inverseCumulativeProbability(p, 0.0001);
        assertEquals(x1, x2, 0.001);
        assertEquals(x2, x3, 0.0001);
        // Check that inverse cumulative probability is accurate
        assertEquals(0.5, x3, 0.0001);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }

    /**
     * Test sample method with custom random number generator.
     */
    @Test
    public void testSampleWithCustomRng() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }
}