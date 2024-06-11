package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link UniformRealDistribution}.
 */
public class UniformRealDistributionTest {

    @Test
    public void testStandardUniformRealDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.5, distribution.density(0.5), 1e-9);
        Assert.assertEquals(0, distribution.density(0), 1e-9);
        Assert.assertEquals(0, distribution.density(1), 1e-9);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 1e-9);
        Assert.assertEquals(0, distribution.cumulativeProbability(0), 1e-9);
        Assert.assertEquals(1, distribution.cumulativeProbability(1), 1e-9);
    }

    @Test
    public void testUniformRealDistributionWithCustomBounds() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(-1, 1);
        Assert.assertEquals(1.0 / (1 - (-1)), distribution.density(0), 1e-9);
        Assert.assertEquals(0, distribution.density(-2), 1e-9);
        Assert.assertEquals(0, distribution.density(2), 1e-9);
        Assert.assertEquals(0, distribution.cumulativeProbability(-1), 1e-9);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0), 1e-9);
        Assert.assertEquals(1, distribution.cumulativeProbability(1), 1e-9);
    }

    @Test
    public void testUniformRealDistributionWithCustomBoundsAndCustomAccuracy() throws NumberIsTooLargeException {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), -1, 1, 1e-5);
        Assert.assertEquals(1.0 / (1 - (-1)), distribution.density(0), 1e-5);
        Assert.assertEquals(0, distribution.density(-2), 1e-5);
        Assert.assertEquals(0, distribution.density(2), 1e-5);
        Assert.assertEquals(0, distribution.cumulativeProbability(-1), 1e-5);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0), 1e-5);
        Assert.assertEquals(1, distribution.cumulativeProbability(1), 1e-5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidUniformRealDistribution() throws NumberIsTooLargeException {
        new UniformRealDistribution(new Well19937c(), 1, 1);
    }

    @Test
    public void testSampleFromUniformRealDistribution() {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), 0, 1);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0);
            Assert.assertTrue(sample < 1);
        }
    }

    @Test
    public void testNumericalMeanAndVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), 0, 1);
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 1e-9);
        Assert.assertEquals(0.25 / 12, distribution.getNumericalVariance(), 1e-9);
    }
}