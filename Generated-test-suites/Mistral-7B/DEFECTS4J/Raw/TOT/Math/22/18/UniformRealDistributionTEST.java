package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    /**
     * Test standard uniform distribution.
     */
    @Test
    public void testStandardUniformDistribution() {
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0 && sample < 1);
        }
    }

    /**
     * Test lower bound is inclusive.
     */
    @Test
    public void testLowerBoundIsInclusive() {
        distribution = new UniformRealDistribution(0.1, 0.5, 0.01);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0.1);
        }
    }

    /**
     * Test upper bound is exclusive.
     */
    @Test
    public void testUpperBoundIsExclusive() {
        distribution = new UniformRealDistribution(0.1, 0.5, 0.01);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample < 0.5);
        }
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        double lowerBound = 0.2;
        double upperBound = 0.6;
        double inverseProbability = 0.3;
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0, 1e-10);
        distribution = new UniformRealDistribution(distribution.getRandomGenerator(), lowerBound, upperBound);
        double result = distribution.inverseCumulativeProbability(inverseProbability);
        double expected = (lowerBound + (upperBound - lowerBound) * inverseProbability);
        Assert.assertEquals(expected, result, 1e-12);
    }

    /**
     * Test lower bound is greater than upper bound.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testLowerBoundGreaterThanUpperBound() {
        distribution = new UniformRealDistribution(-1.0, 0.5, 0.01);
    }
}