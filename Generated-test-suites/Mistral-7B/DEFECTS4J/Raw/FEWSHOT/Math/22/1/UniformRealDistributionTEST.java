package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

/**
 * Test cases for class UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    /**
     * Test constuctor UniformRealDistribution().
     */
    @Test
    public void testUniformRealDistribution() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0, urd.getSupportLowerBound(), 0.0);
        assertEquals(1, urd.getSupportUpperBound(), 0.0);
        // test density
        for (double d = -0.01; d <= 1.01; d += 0.01) {
            double density = urd.density(d);
            double eps = 1e-7;
            if (d < 0 || d >= 1) {
                assertTrue(density == 0.0);
            } else {
                assertTrue(Math.abs(density - 1.0 / (1.0 - 0.0)) <= eps);
            }
        }
    }

    /**
     * Test constructor UniformRealDistribution(double, double).
     */
    @Test
    public void testUniformRealDistributionWithBounds() throws NumberIsTooLargeException {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertEquals(0.1, urd.getSupportLowerBound(), 0.0);
        assertEquals(0.4, urd.getSupportUpperBound(), 0.0);
        // test density
        for (double d = -0.01; d <= 0.3; d += 0.01) {
            double density = urd.density(d);
            double eps = 1e-7;
            assertTrue(Math.abs(density - 1.0 / (0.4 - 0.1)) <= eps);
        }
        for (double d = 0.4; d <= 1.01; d += 0.01) {
            assertTrue(density == 0.0);
        }
    }

    /**
     * Test constructor UniformRealDistribution(double, double, double).
     */
    @Test
    public void testUniformRealDistributionWithAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4, 1e-8);
        assertEquals(0.1, urd.getSupportLowerBound(), 0.0);
        assertEquals(0.4, urd.getSupportUpperBound(), 0.0);
        // test density
        for (double d = -0.01; d <= 0.3; d += 0.01) {
            double density = urd.density(d);
            double eps = 1e-8;
            assertTrue(Math.abs(density - 1.0 / (0.4 - 0.1)) <= eps);
        }
        for (double d = 0.4; d <= 1.01; d += 0.01) {
            assertTrue(density == 0.0);
        }
    }

    /**
     * Test method sample().
     */
    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        for (int i = 0; i < 1000; i++) {
            double s = urd.sample();
            assertTrue(s >= 0.1 && s < 0.4);
        }
    }

    /**
     * Test method density().
     */
    @Test
    public void testDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        double eps = 1e-7;
        for (double d = 0.11; d <= 0.39; d += 0.01) {
            double density = urd.density(d);
            assertTrue(Math.abs(density - 1.0 / (0.39 - 0.11)) <= eps);
        }
        for (double d = 0.41; d <= 1; d += 0.01) {
            assertTrue(density == 0.0);
        }
    }

    /**
     * Test method cumulativeProbability().
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        for (double d = 0.1; d <= 0.4; d += 0.01) {
            double cumProb = urd.cumulativeProbability(d);
            double eps = 1e-7;
            assertTrue(Math.abs(cumProb - d / (0.3)) <= eps);
        }
        for (double d = 0.1; d <= 0.1; d += 0.01) {
            double cumProb = urd.cumulativeProbability(d);
            double eps = 1e-7;
            assertTrue(Math.abs(cumProb - d / (0.3)) <= eps);
        }
        for (double d = 0.41; d <= 1; d += 0.01) {
            assertTrue(cumProb == 1.0);
        }
    }

    /**
     * Test method getNumericalMean().
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        double mean = urd.getNumericalMean();
        double eps = 1e-7;
        assertEquals(0.254, mean, eps);
    }

    /**
     * Test method getNumericalVariance().
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        double variance = urd.getNumericalVariance();
        double eps = 1e-7;
        assertEquals(0.001_023_122_580_25, variance, eps);
    }

    /**
     * Test method isSupportLowerBoundInclusive().
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertTrue(urd.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive().
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertFalse(urd.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected().
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4);
        assertTrue(urd.isSupportConnected());
    }

    /**
     * Test method getSolverAbsoluteAccuracy().
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution urd = new UniformRealDistribution(0.1, 0.4, 1e-8);
        double solverAccuracy = urd.getSolverAbsoluteAccuracy();
        assertEquals(1e-8, solverAccuracy, 0.0);
    }
}