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
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(2.5, 6.5);
        assertEquals(2.5, distribution.getLowerBound(), 0.0);
        assertEquals(6.5, distribution.getUpperBound(), 0.0);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with custom parameters and solver accuracy.
     */
    @Test
    public void testCustomConstructorWithCustomAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(2.5, 6.5, 1e-5);
        assertEquals(2.5, distribution.getLowerBound(), 0.0);
        assertEquals(6.5, distribution.getUpperBound(), 0.0);
        assertEquals(1e-5, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test constructor with custom random number generator.
     */
    @Test
    public void testCustomConstructorWithRandomGenerator() throws NumberIsTooLargeException {
        Well19937c rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 2.5, 6.5);
        assertEquals(2.5, distribution.getLowerBound(), 0.0);
        assertEquals(6.5, distribution.getUpperBound(), 0.0);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);
        assertEquals(rng, distribution.getRandomGenerator());
    }

    /**
     * Test method density.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 1e-7);
        }

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        for (double x = 1.5; x <= 4.5; x += 0.01) {
            double density = distribution2.density(x);
            assertEquals(1.0 / (4.5 - 1.5), density, 1e-7);
        }

        for (double x = 0.0; x < 1.5; x += 0.01) {
            double density = distribution2.density(x);
            assertEquals(0.0, density, 1e-7);
        }

        for (double x = 4.5; x > 1.5; x += 0.01) {
            double density = distribution2.density(x);
            assertEquals(0.0, density, 1e-7);
        }
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            assertEquals(x, cumProb, 1e-7);
        }

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        for (double x = 1.5; x <= 4.5; x += 0.01) {
            double cumProb = distribution2.cumulativeProbability(x);
            assertEquals((x - 1.5) / (4.5 - 1.5), cumProb, 1e-7);
        }

        for (double x = 0.0; x < 1.5; x += 0.01) {
            double cumProb = distribution2.cumulativeProbability(x);
            assertEquals(0.0, cumProb, 1e-7);
        }

        for (double x = 4.5; x > 1.5; x += 0.01) {
            double cumProb = distribution2.cumulativeProbability(x);
            assertEquals(1.0, cumProb, 1e-7);
        }
    }

    /**
     * Test method getSolverAbsoluteAccuracy.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5, 1e-5);
        assertEquals(1e-5, distribution2.getSolverAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.getNumericalMean(), 1e-7);

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertEquals(3.0, distribution2.getNumericalMean(), 1e-7);
    }

    /**
     * Test method getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getNumericalVariance(), 1e-7);

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertEquals(0.25, distribution2.getNumericalVariance(), 1e-7);
    }

    /**
     * Test method getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertEquals(1.5, distribution2.getSupportLowerBound(), 0.0);
    }

    /**
     * Test method getSupportUpperBound.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertEquals(4.5, distribution2.getSupportUpperBound(), 0.0);
    }

    /**
     * Test method isSupportLowerBoundInclusive.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportLowerBoundInclusive());

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertTrue(distribution2.isSupportLowerBoundInclusive());
    }

    /**
     * Test method isSupportUpperBoundInclusive.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertFalse(distribution.isSupportUpperBoundInclusive());

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertFalse(distribution2.isSupportUpperBoundInclusive());
    }

    /**
     * Test method isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportConnected());

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        assertTrue(distribution2.isSupportConnected());
    }

    /**
     * Test method sample.
     */
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getSupportLowerBound());
            assertTrue(sample < distribution.getSupportUpperBound());
        }

        UniformRealDistribution distribution2 = new UniformRealDistribution(1.5, 4.5);
        for (int i = 0; i < 10000; i++) {
            double sample = distribution2.sample();
            assertTrue(sample >= distribution2.getSupportLowerBound());
            assertTrue(sample < distribution2.getSupportUpperBound());
        }
    }
}