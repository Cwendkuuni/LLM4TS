package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test the constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
        assertEquals(true, distribution.isSupportConnected());
    }

    /**
     * Test the constructor with specified bounds.
     * @throws NumberIsTooLargeException if the lower bound is greater than or equal to the upper bound.
     */
    @Test
    public void testSpecifiedConstructors() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(3.0, distribution.getSupportUpperBound(), 0.0);
        assertEquals(2.0, distribution.getNumericalMean(), 0.0);
        assertEquals(1.33333333333333333, distribution.getNumericalVariance(), 0.0);
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
        assertEquals(true, distribution.isSupportConnected());
    }

    /**
     * Test the density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x < 1.0; x += 0.01) {
            double density = distribution.density(x);
            assertTrue(density > 0.0 && density < 2.0);
        }
    }

    /**
     * Test the cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double probability = distribution.cumulativeProbability(x);
            assertTrue(probability >= x && probability <= x + 0.01);
        }
    }

    /**
     * Test the inverseCumulativeProbability method.
     */
    @Test
    public void testInverseCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double inverseProbability = distribution.inverseCumulativeProbability(x);
            assertEquals(x, inverseProbability, distribution.getSolverAbsoluteAccuracy());
        }
    }

    /**
     * Test the sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getSupportLowerBound() && sample < distribution.getSupportUpperBound());
        }
    }
}