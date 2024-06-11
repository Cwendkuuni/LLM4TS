package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {
    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertTrue(dist.isSupportUpperBoundExclusive());
        assertFalse(dist.isSupportConnected());
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3);
        assertEquals(1, dist.getSupportLowerBound(), 0);
        assertEquals(3, dist.getSupportUpperBound(), 0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertFalse(dist.isSupportConnected());
    }

    /**
     * Test constructor with custom parameters and inverseCumulativeProbabilityAccuracy.
     */
    @Test
    public void testCustomConstructorWithAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3, 0.001);
        assertEquals(1, dist.getSupportLowerBound(), 0);
        assertEquals(3, dist.getSupportUpperBound(), 0);
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertFalse(dist.isSupportConnected());
    }

    /**
     * Test method {@link UniformRealDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double density = dist.density(x);
            double expected = 1 / (1 - 0);
            assertEquals(expected, density, 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double probs = dist.cumulativeProbability(x);
            double expected = x;
            assertEquals(expected, probs, 1e-6);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#sample()}.
     */
    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0);
            assertTrue(sample < 1);
        }
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double mean = dist.getNumericalMean();
        double expected = 0.5;
        assertEquals(expected, mean, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double variance = dist.getNumericalVariance();
        double expected = 0.125;
        assertEquals(expected, variance, 1e-6);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double lowerBound = dist.getSupportLowerBound();
        double expected = 0;
        assertEquals(expected, lowerBound, 0);
    }

    /**
     * Test method {@link UniformRealDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double upperBound = dist.getSupportUpperBound();
        double expected = 1;
        assertEquals(expected, upperBound, 0);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportLowerBoundInclusive()}.
     */
    @Test
    public void testSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean inclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(inclusive);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportUpperBoundInclusive()}.
     */
    @Test
    public void testSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean inclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(inclusive);
    }

    /**
     * Test method {@link UniformRealDistribution#isSupportConnected()}.
     */
    @Test
    public void testSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        boolean connected = dist.isSupportConnected();
        assertTrue(connected);
    }
}