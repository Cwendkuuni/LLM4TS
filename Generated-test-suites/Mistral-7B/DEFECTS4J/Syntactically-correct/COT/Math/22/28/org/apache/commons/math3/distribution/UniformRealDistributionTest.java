package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import org.junit.Test;

/**
 * Test class for UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with defaults.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        // other tests for default constructor can be added here
    }

    /**
     * Test constructor with given bounds.
     * @throws NumberIsTooLargeException if lower >= upper
     */
    @Test
    public void testConstructorWithBounds() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(2.5, 4.5);
        assertEquals(2.5, dist.getLowerBound(), 0.0);
        assertEquals(4.5, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with given bounds and inverseCumulativeProbabilityAccuracy.
     * @throws NumberIsTooLargeException if lower >= upper
     */
    @Test
    public void testConstructorWithBoundsAndSolverAccuracy()
        throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(2.5, 4.5, 1.0e-5);
        assertEquals(2.5, dist.getLowerBound(), 0.0);
        assertEquals(4.5, dist.getUpperBound(), 0.0);
        assertEquals(1.0e-5, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with random number generator, bounds and inverseCumulativeProbabilityAccuracy.
     * @throws NumberIsTooLargeException if lower >= upper
     */
    @Test
    public void testConstructorWithRngBoundsAndSolverAccuracy()
        throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(RandomUtils.createGaussianRandom(), 2.5, 4.5, 1.0e-5);
        assertEquals(2.5, dist.getLowerBound(), 0.0);
        assertEquals(4.5, dist.getUpperBound(), 0.0);
        assertEquals(1.0e-5, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method density.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.1) {
            double density = dist.density(x);
            if (x == dist.getLowerBound() || x == dist.getUpperBound()) {
                assertEquals(0.0, density, 1e-5);
            } else {
                assertEquals(1 / (dist.getUpperBound() - dist.getLowerBound()), density, 1e-5);
            }
        }
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            if (x <= dist.getLowerBound()) {
                assertEquals(0.0, cdf, 1e-5);
            } else if (x >= dist.getUpperBound()) {
                assertEquals(1.0, cdf, 1e-5);
            } else {
                assertEquals(x - dist.getLowerBound() / (dist.getUpperBound() - dist.getLowerBound()), cdf, 1e-5);
            }
        }
    }

    /**
     * Test method getSolverAbsoluteAccuracy.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 1e-5);
        UniformRealDistribution dist1 = new UniformRealDistribution(1.0, 2.0, 0.1);
        assertEquals(0.1, dist1.getSolverAbsoluteAccuracy(), 1e-5);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5 * (dist.getLowerBound() + dist.getUpperBound()), dist.getNumericalMean(), 1e-5);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double ul = dist.getUpperBound() - dist.getLowerBound();
        assertEquals(ul * ul / 12, dist.getNumericalVariance(), 1e-5);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getLowerBound(), dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(dist.getUpperBound(), dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method isSupportLowerBoundInclusive.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method sample.
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
}