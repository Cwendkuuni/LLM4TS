package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    /**
     * Test construction with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertFalse(dist.isSupportConnected());
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0.0);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test construction with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1.0, 3.0);
        Assert.assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        Assert.assertEquals(3.0, dist.getSupportUpperBound(), 0.0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertFalse(dist.isSupportConnected());
        Assert.assertEquals(2.0, dist.getNumericalMean(), 0.0);
        Assert.assertEquals(0.34, dist.getNumericalVariance(), 0.01);
    }

    /**
     * Test construction with invalid parameters.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 3.0, 1.0);
    }

    /**
     * Test density function.
     */
    @Test
    public void testDensityFunction() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            Assert.assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
        }
    }

    /**
     * Test cumulative probability function.
     */
    @Test
    public void testCumulativeProbabilityFunction() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            Assert.assertEquals(x, cdf, 0.001);
        }
    }

    /**
     * Test inverse cumulative probability function.
     */
    @Test
    public void testInverseCumulativeProbabilityFunction() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);
        double epsilon = 1e-9;

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double inverseCdf = dist.inverseCumulativeProbability(x);
            Assert.assertEquals(x, inverseCdf, epsilon);
        }
    }

    /**
     * Test sampling.
     */
    @Test
    public void testSampling() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.0, 1.0);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            Assert.assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }
}