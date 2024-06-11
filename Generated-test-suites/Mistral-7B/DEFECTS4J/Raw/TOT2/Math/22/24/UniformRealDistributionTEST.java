package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Test class for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    /**
     * Test standard uniform real distribution.
     */
    @Test
    public void testStandardUniformRealDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // Basic functionality test
        assertEquals(0.5, distribution.density(0.5), 1e-9);
        assertEquals(0, distribution.density(1.5), 1e-9);
        assertEquals(1, distribution.density(0.4), 1e-9);

        // Check limits
        assertEquals(0, distribution.cumulativeProbability(0), 1e-9);
        assertEquals(1, distribution.cumulativeProbability(1), 1e-9);

        // Check mean and variance
        assertEquals(0.5, distribution.getNumericalMean(), 1e-9);
        assertEquals(0.0625, distribution.getNumericalVariance(), 1e-9);

        // Check bounds
        assertEquals(0, distribution.getSupportLowerBound(), 1e-9);
        assertEquals(1, distribution.getSupportUpperBound(), 1e-9);

        // Check inclusiveness
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());

        // Check support is connected
        assertTrue(distribution.isSupportConnected());

        // Check sample
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue((sample >= 0) && (sample <= 1));
        }
    }

    /**
     * Test uniform real distribution with custom bounds.
     */
    @Test
    public void testUniformRealDistributionCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 5);

        // Basic functionality test
        assertEquals(1 / (5 - 1), distribution.density(3), 1e-9);
        assertEquals(0, distribution.density(0), 1e-9);
        assertEquals(1, distribution.density(4), 1e-9);

        // Check limits
        assertEquals(1, distribution.cumulativeProbability(1), 1e-9);
        assertEquals(1, distribution.cumulativeProbability(5), 1e-9);

        // Check mean and variance
        assertEquals(3, distribution.getNumericalMean(), 1e-9);
        double variance = (Math.pow(5, 2) + Math.pow(1, 2)) / 12;
        assertEquals(variance, distribution.getNumericalVariance(), 1e-9);

        // Check bounds
        assertEquals(1, distribution.getSupportLowerBound(), 1e-9);
        assertEquals(5, distribution.getSupportUpperBound(), 1e-9);

        // Check inclusiveness
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());

        // Check support is connected
        assertTrue(distribution.isSupportConnected());

        // Check sample
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue((sample >= 1) && (sample <= 5));
        }
    }

    /**
     * Test uniform real distribution with custom bounds and error handling.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionCustomBoundsErrorCase() {
        // Test with lower bound >= upper bound
        new UniformRealDistribution(5, 5);
    }
}