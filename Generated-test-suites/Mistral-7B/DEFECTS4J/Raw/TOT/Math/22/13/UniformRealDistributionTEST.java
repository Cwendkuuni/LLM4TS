package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {
    private RandomGenerator rng;
    private UniformRealDistribution dist;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            dist = new UniformRealDistribution(rng, 0, 1);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDensity() {
        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.01) {
            double density = dist.density(x);
            double expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            Assert.assertEquals("Unexpected density for x = " + x, expected, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.01) {
            double prob = dist.cumulativeProbability(x);
            double expected = x / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            Assert.assertEquals("Unexpected cumulative probability for x = " + x, expected, prob, 0.001);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLowerBound() {
        new UniformRealDistribution(rng, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpperBoundLowerThanLowerBound() {
        new UniformRealDistribution(rng, 3, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUpperBoundEqualsLowerBound() {
        try {
            new UniformRealDistribution(rng, 1, 1);
        } catch (NumberIsTooLargeException e) {
            throw new RuntimeException("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals("Unexpected lower bound", 0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals("Unexpected upper bound", 1, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue("Unexpected support lower bound is not inclusive", dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse("Unexpected support upper bound is inclusive", dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue("Unexpected support is not connected", dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            Assert.assertTrue("Unexpected sample: " + sample, sample >= dist.getSupportLowerBound() && sample < dist.getSupportUpperBound());
        }
    }
}