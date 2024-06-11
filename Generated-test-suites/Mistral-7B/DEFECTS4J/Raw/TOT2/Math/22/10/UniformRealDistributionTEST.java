package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;

/**
 * Tests for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default bounds.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(1, distribution.getSupportUpperBound(), 0.001);
    }

    /**
     * Test constructor with custom bounds.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 5);
        assertEquals(1, distribution.getSupportLowerBound(), 0.001);
        assertEquals(5, distribution.getSupportUpperBound(), 0.001);
    }

    /**
     * Test constructor with custom bounds and inverse cumulative probability accuracy.
     */
    @Test
    public void testCustomConstructorWithAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 5, 1e-11);
        assertEquals(1, distribution.getSupportLowerBound(), 0.001);
        assertEquals(5, distribution.getSupportUpperBound(), 0.001);
    }

    /**
     * Test constructor with custom bounds and random number generator.
     */
    @Test
    public void testCustomConstructorWithRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 1, 5);
        assertEquals(1, distribution.getSupportLowerBound(), 0.001);
        assertEquals(5, distribution.getSupportUpperBound(), 0.001);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            assertEquals(1 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()), distribution.density(x), 0.001);
        }
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            assertEquals(x / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound()), distribution.cumulativeProbability(x), 0.001);
        }
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.001);
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(distribution.getSupportLowerBound() + distribution.getSupportUpperBound() / 2, distribution.getNumericalMean(), 0.001);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        double ul = distribution.getSupportUpperBound() - distribution.getSupportLowerBound();
        assertEquals(ul * ul / 12, distribution.getNumericalVariance(), 0.001);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            assertEquals(true, distribution.isSupportLowerBoundInclusive());
            assertEquals(false, distribution.isSupportUpperBoundInclusive());
            assertEquals(true, distribution.isSupportConnected());
            double sample = distribution.sample();
            assertEquals(true, distribution.isInSupport(sample));
        }
    }

    /**
     * Test isInSupport method with lower bound inclusive.
     */
    @Test
    public void testIsInSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            assertEquals(true, distribution.isInSupport(x));
        }
    }

    /**
     * Test isInSupport method with lower bound exclusive.
     */
    @Test
    public void testIsInSupportLowerBoundExclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x < distribution.getSupportUpperBound(); x++) {
            assertEquals(false, distribution.isInSupport(x));
        }
        assertEquals(true, distribution.isInSupport(distribution.getSupportLowerBound()));
    }

    /**
     * Test isInSupport method with upper bound inclusive.
     */
    @Test
    public void testIsInSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x <= distribution.getSupportUpperBound(); x++) {
            assertEquals(true, distribution.isInSupport(x));
        }
        assertEquals(false, distribution.isInSupport(distribution.getSupportUpperBound()));
    }

    /**
     * Test isInSupport method with upper bound exclusive.
     */
    @Test
    public void testIsInSupportUpperBoundExclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getSupportLowerBound(); x < distribution.getSupportUpperBound(); x++) {
            assertEquals(true, distribution.isInSupport(x));
        }
        assertEquals(false, distribution.isInSupport(distribution.getSupportUpperBound()));
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }
}