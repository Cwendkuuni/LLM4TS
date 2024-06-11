package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link UniformRealDistribution}.
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
        assertSame(Well19937c.getDefault(), dist.getRandomGenerator());
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(2.0, dist.getUpperBound(), 0.0);
        assertNotSame(Well19937c.getDefault(), dist.getRandomGenerator());
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(2.0, 1.0);
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            assertEquals(1.0 / (1.0 - 0.0), dist.density(x), 1e-6);
        }
        for (double x = 1.0; x > 0.0; x -= 0.01) {
            assertEquals(0.0, dist.density(x), 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            assertEquals(x, dist.cumulativeProbability(x), 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 100; i++) {
            assertNotNull(dist.sample());
            assertTrue(dist.sample() >= 0.0 && dist.sample() <= 1.0);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, dist.getNumericalMean(), 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0002449489742783178, dist.getNumericalVariance(), 1e-8);
    }
}