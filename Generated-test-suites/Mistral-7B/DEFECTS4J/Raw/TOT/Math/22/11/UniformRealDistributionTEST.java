package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Tests default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.0);
    }

    /**
     * Tests constructor with lower bound 0 and upper bound 1.
     */
    @Test
    public void testConstructorLowerBoundZeroUpperBoundOne() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 - 0.0), dist.density(0.5), 0.0);
    }

    /**
     * Tests constructor with lower bound 1 and upper bound 2.
     */
    @Test
    public void testConstructorLowerBoundOneUpperBoundTwo() {
        UniformRealDistribution dist = new UniformRealDistribution(1, 2);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(2.0, dist.getUpperBound(), 0.0);
        assertEquals(1.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (2.0 - 1.0), dist.density(1.5), 0.0);
    }

    /**
     * Tests constructor with lower bound -1 and upper bound 1.
     */
    @Test
    public void testConstructorLowerBoundNegativeOneUpperBoundOne() {
        UniformRealDistribution dist = new UniformRealDistribution(-1, 1);
        assertEquals(-1.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.125, dist.getNumericalVariance(), 0.0);
        assertEquals(1.0 / (1.0 + 1.0), dist.density(0.0), 0.0);
    }

    /**
     * Tests constructor with wrong lower bound and upper bound.
     *
     * @throws NumberIsTooLargeException expected exception
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorLowerBoundGreaterOrEqualUpperBound() throws NumberIsTooLargeException {
        new UniformRealDistribution(3.0, 2.0);
    }

    /**
     * Tests density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double[] x = {0.1, 0.5, 0.9};
        for (double value : x) {
            assertEquals(1.0 / (1.0 - 0.0), dist.density(value), 0.0);
        }
    }

    /**
     * Tests cumulativeProbability method with equal bounds.
     */
    @Test
    public void testCumulativeProbabilityEqualBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 0);
        assertEquals(1.0, dist.cumulativeProbability(0.0), 0.0);
    }

    /**
     * Tests cumulativeProbability method with lower bound less than upper bound.
     */
    @Test
    public void testCumulativeProbabilityLowerBoundLessThanUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double[] x = {0.1, 0.5, 0.9};
        for (double value : x) {
            double expected = value;
            if (value <= 0.0) {
                expected = 0.0;
            } else if (value >= 1.0) {
                expected = 1.0;
            }
            assertEquals(expected, dist.cumulativeProbability(value), 0.0);
        }
    }

    /**
     * Tests inverseCumulativeProbability method with equal bounds.
     */
    @Test
    public void testInverseCumulativeProbabilityEqualBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 0);
        assertEquals(0.0, dist.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Tests inverseCumulativeProbability method with lower bound less than upper bound.
     */
    @Test
    public void testInverseCumulativeProbabilityLowerBoundLessThanUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        double[] x = {0.1, 0.5, 0.9};
        for (double p : x) {
            double expected = p;
            assertEquals(expected, dist.inverseCumulativeProbability(p), 0.0);
        }
    }

    /**
     * Tests inverseCumulativeProbability method with negative probability.
     *
     * @throws AssertionError expected exception
     */
    @Test(expected = AssertionError.class)
    public void testInverseCumulativeProbabilityNegativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        dist.inverseCumulativeProbability(-1.0);
    }

    /**
     * Tests inverseCumulativeProbability method with probability greater than 1.
     *
     * @throws AssertionError expected exception
     */
    @Test(expected = AssertionError.class)
    public void testInverseCumulativeProbabilityProbabilityGreaterThanOne() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        dist.inverseCumulativeProbability(2.0);
    }

    /**
     * Tests getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    /**
     * Tests getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    /**
     * Tests getSupportLowerBoundInclusive method.
     */
    @Test
    public void testGetSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    /**
     * Tests getSupportUpperBoundInclusive method.
     */
    @Test
    public void testGetSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    /**
     * Tests isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(0, 1);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Tests sample method with default RandomGenerator.
     */
    @Test
    public void testSampleWithDefaultRandomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double x = dist.sample();
        assertTrue(x >= dist.getLowerBound() && x < dist.getUpperBound());
    }

    /**
     * Tests sample method with specified RandomGenerator.
     */
    @Test
    public void testSampleWithSpecifiedRandomGenerator() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c());
        double x = dist.sample();
        assertTrue(x >= dist.getLowerBound() && x < dist.getUpperBound());
    }
}