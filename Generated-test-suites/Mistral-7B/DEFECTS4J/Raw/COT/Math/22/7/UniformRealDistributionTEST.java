package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

/**
 * Test cases for UniformRealDistribution class.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(2.0, distribution.getUpperBound(), 0.0);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with custom bounds and accuracy.
     */
    @Test
    public void testCustomConstructorWithAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 2.0, 1e-6);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(2.0, distribution.getUpperBound(), 0.0);
        assertEquals(1.5, distribution.getNumericalMean(), 1e-6);
        assertEquals(0.25, distribution.getNumericalVariance(), 1e-6);
    }

    /**
     * Test exception when lower bound is greater than upper bound.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testExceptionLowerBoundGreaterThanUpperBound() {
        new UniformRealDistribution(3.0, 2.0, 0.01);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x++) {
            double density = distribution.density(x);
            if (x >= 0.5) {
                assertEquals(2.0 * x - 1.0, density, 1e-5);
            } else {
                assertEquals(1.0 / (1.0 - 0.0), density, 1e-5);
            }
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            if (x <= 0.5) {
                assertEquals(x, cdf, 1e-5);
            } else {
                assertEquals(x - 0.5, cdf, 1e-5);
            }
        }
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 1e-7);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, distribution.getNumericalMean(), 1e-5);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 1e-5);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertTrue(distribution.isSupportConnected());
    }
}