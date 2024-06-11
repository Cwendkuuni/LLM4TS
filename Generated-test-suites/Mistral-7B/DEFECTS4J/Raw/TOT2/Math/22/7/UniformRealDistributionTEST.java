package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getLowerBound(), 0.001);
        assertEquals(1, dist.getUpperBound(), 0.001);
        // Test basic functionality
        for (int i = 0; i < 10; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x < 1);
        }
    }

    /**
     * Test constructor with specified bounds.
     */
    @Test
    public void testConstructorWithSpecifiedBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3);
        assertEquals(1, dist.getLowerBound(), 0.001);
        assertEquals(3, dist.getUpperBound(), 0.001);
        // Test basic functionality
        for (int i = 0; i < 10; i++) {
            double x = dist.sample();
            assertTrue(x >= 1 && x < 3);
        }
    }

    /**
     * Test constructor with specified bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testConstructorWithSpecifiedBoundsAndAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution dist = new UniformRealDistribution(1, 3, 0.001);
        assertEquals(1, dist.getLowerBound(), 0.001);
        assertEquals(3, dist.getUpperBound(), 0.001);
        // Test basic functionality
        for (int i = 0; i < 10; i++) {
            double x = dist.sample();
            assertTrue(x >= 1 && x < 3);
        }
        // Test inverse cumulative probability
        double p = 0.2;
        double x = dist.inverseCumulativeProbability(p);
        assertTrue(x >= 1.2 && x < 1.4);
    }

    /**
     * Test constructor with invalid bounds.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() throws NumberIsTooLargeException {
        new UniformRealDistribution(5, 4);
    }

    /**
     * Test {@link #density(double)} method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double density = dist.density(x);
            if (x >= 0 && x < 1) {
                assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
            } else {
                assertEquals(0.0, density, 0.001);
            }
        }
    }

    /**
     * Test {@link #cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        for (double x = 0; x <= 1; x += 0.01) {
            double prob = dist.cumulativeProbability(x);
            if (x >= 0 && x < 1) {
                assertEquals(x, prob, 0.001);
            } else if (x < 0) {
                assertEquals(0.0, prob, 0.001);
            } else {
                assertEquals(1.0, prob, 0.001);
            }
        }
    }

    /**
     * Test {@link #getSolverAbsoluteAccuracy()} method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.001);
    }

    /**
     * Test {@link #getNumericalMean()} method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double mean = dist.getNumericalMean();
        assertEquals(0.5, mean, 0.001);
    }

    /**
     * Test {@link #getNumericalVariance()} method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double variance = dist.getNumericalVariance();
        assertEquals(0.0025, variance, 0.00001);
    }

    /**
     * Test {@link #getSupportLowerBound()} method.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double lowerBound = dist.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.001);
    }

    /**
     * Test {@link #getSupportUpperBound()} method.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double upperBound = dist.getSupportUpperBound();
        assertEquals(1.0, upperBound, 0.001);
    }

    /**
     * Test {@link #isSupportLowerBoundInclusive()} method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Test {@link #isSupportUpperBoundInclusive()} method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Test {@link #isSupportConnected()} method.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test {@link #sample()} method using default random number generator.
     */
    @Test
    public void testSampleUsingDefaultGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x < 1);
        }
    }

    /**
     * Test {@link #sample()} method using specified random number generator.
     */
    @Test
    public void testSampleUsingSpecifiedGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 0, 1);
        RandomGenerator rng = dist.getRandomGenerator();
        for (int i = 0; i < 10; i++) {
            double x = dist.sample();
            assertTrue(x >= 0 && x < 1);
            assertEquals(rng.nextDouble(), x, 0.001);
        }
    }
}