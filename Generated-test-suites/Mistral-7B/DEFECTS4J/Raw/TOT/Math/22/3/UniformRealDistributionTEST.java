package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private RandomGenerator rng;
    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution uniform = new UniformRealDistribution();
        Assert.assertEquals(0, uniform.getSupportLowerBound(), 0);
        Assert.assertEquals(1, uniform.getSupportUpperBound(), 0);
        Assert.assertEquals(0.5, uniform.getNumericalMean(), 0);
        Assert.assertEquals(0.25, uniform.getNumericalVariance(), 0);
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution uniform = new UniformRealDistribution(rng, 2, 5);
        Assert.assertEquals(2, uniform.getSupportLowerBound(), 0);
        Assert.assertEquals(5, uniform.getSupportUpperBound(), 0);
        Assert.assertEquals(3.5, uniform.getNumericalMean(), 0);
        Assert.assertEquals(2.25, uniform.getNumericalVariance(), 0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        for (double x = 0; x <= 1; x += 0.01) {
            double density = distribution.density(x);
            Assert.assertEquals(1.0 / (1 - 0), density, 1e-5);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        for (double x = 0; x <= 1; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            double expected = x;
            if (x < 0 || x > 1) {
                expected = Double.NEGATIVE_INFINITY;
            }
            Assert.assertEquals(expected, cumProb, 1e-5);
        }
    }

    /**
     * Test getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0 && sample < 1);
        }
    }

    /**
     * Test sample method with negative lower bound.
     *
     * @expected Exception: java.lang.NumberIsTooLargeException
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleWithNegativeLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(rng, -1, 1);
        distribution.sample();
    }

    /**
     * Test sample method with upper bound equal to lower bound.
     *
     * @expected Exception: java.lang.NumberIsTooLargeException
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleWithEqualBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 1, 1);
        distribution.sample();
    }
}