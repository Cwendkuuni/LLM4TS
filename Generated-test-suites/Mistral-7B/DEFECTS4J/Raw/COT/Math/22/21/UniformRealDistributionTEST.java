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
     * Test constructor UniformRealDistribution().
     */
    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertNotNull(dist);
    }

    /**
     * Test constructor UniformRealDistribution(double, double) with valid bounds.
     */
    @Test
    public void testUniformRealDistributionValidBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.5, dist.getUpperBound(), 0.0);
        assertNotNull(dist);
    }

    /**
     * Test constructor UniformRealDistribution(double, double) with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds() {
        new UniformRealDistribution(1.0, 0.5);
    }

    /**
     * Test constructor UniformRealDistribution(double, double, double) with valid bounds and accuracy.
     */
    @Test
    public void testUniformRealDistributionValidBoundsAndAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.5, 0.001);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.5, dist.getUpperBound(), 0.0);
        assertEquals(0.001, dist.getSolverAbsoluteAccuracy(), 0.0);
        assertNotNull(dist);
    }

    /**
     * Test constructor UniformRealDistribution(RandomGenerator, double, double, double).
     */
    @Test
    public void testUniformRealDistributionRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.5);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.5, dist.getUpperBound(), 0.0);
        assertNotNull(dist);
    }

    /**
     * Test method density.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.3, 0.6, 0.001);
        for (double x = 0.3; x <= 0.6; x += 0.01) {
            assertEquals(1.0 / (0.6 - 0.3), dist.density(x), 0.001);
        }
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.3, 0.6, 0.001);
        for (double x = 0.3; x <= 0.6; x += 0.01) {
            assertEquals(x - 0.3, dist.cumulativeProbability(x), 0.001);
        }
        assertEquals(1.0, dist.cumulativeProbability(0.6), 0.001);
        assertEquals(0.0, dist.cumulativeProbability(0.3 - 0.01), 0.001);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertEquals(0.5, dist2.getNumericalMean(), 0.0);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getNumericalVariance(), 0.0);
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertEquals(0.00392857142857143, dist2.getNumericalVariance(), 0.0001);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertEquals(0.2, dist2.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertEquals(0.8, dist2.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method isSupportLowerBoundInclusive.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertTrue(dist2.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertFalse(dist2.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
        UniformRealDistribution dist2 = new UniformRealDistribution(0.2, 0.8);
        assertTrue(dist2.isSupportConnected());
    }

    /**
     * Test method sample.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            double s = dist.sample();
            assertTrue(s >= dist.getSupportLowerBound());
            assertTrue(s < dist.getSupportUpperBound());
        }
    }
}