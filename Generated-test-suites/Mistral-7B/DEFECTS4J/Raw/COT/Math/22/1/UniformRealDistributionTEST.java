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
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with lower and upper bounds.
     */
    @Test
    public void testConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.4, dist.getUpperBound(), 0.0);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidBoundsConstructor() throws NumberIsTooLargeException {
        new UniformRealDistribution(1.0, 0.5);
    }

    /**
     * Test constructor with rng, lower and upper bounds.
     */
    @Test
    public void testConstructorWithRng() throws NumberIsTooLargeException {
        Well19937c rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.1, 0.4);
        assertEquals(0.1, dist.getLowerBound(), 0.0);
        assertEquals(0.4, dist.getUpperBound(), 0.0);
        assertEquals(rng, dist.getRandomGenerator());
    }

    /**
     * Test method density.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertNotNull(dist);
        for (double x = 0.1; x <= 0.4; x += 0.01) {
            double density = dist.density(x);
            assertEquals(1.0 / (0.4 - 0.1), density, 0.001);
        }
        for (double x = 0.0; x < 0.1; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 0.001);
        }
        for (double x = 0.4; x < Double.MAX_VALUE; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 0.001);
        }
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertNotNull(dist);
        for (double x = 0.1; x <= 0.4; x += 0.01) {
            double cp = dist.cumulativeProbability(x);
            assertEquals(x - 0.1, cp, 0.001);
        }
        for (double x = 0.0; x < 0.1; x += 0.01) {
            double cp = dist.cumulativeProbability(x);
            assertEquals(0.0, cp, 0.001);
        }
        for (double x = 0.4; x < Double.MAX_VALUE; x += 0.01) {
            double cp = dist.cumulativeProbability(x);
            assertEquals(1.0, cp, 0.001);
        }
    }

    /**
     * Test method getSolverAbsoluteAccuracy.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.0, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.25, dist.getNumericalMean(), 0.001);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.0004166666666666667, dist.getNumericalVariance(), 0.001);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.4, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method isSupportLowerBoundInclusive.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test method sample.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.4);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.1 && sample <= 0.4);
        }
    }
}