package org.apache.commons.math3.distribution;

import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0001);

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(10.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.6429, dist.getNumericalVariance(), 0.0001);

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(3.8788, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0476, dist.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.3095, dist.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.8095, dist.cumulativeProbability(3), 0.0001);

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.0311, dist.cumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.1979, dist.cumulativeProbability(10), 0.0001);
        Assert.assertEquals(0.7164, dist.cumulativeProbability(15), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.4762, dist.probability(0), 0.0001);
        Assert.assertEquals(0.1905, dist.probability(1), 0.0001);
        Assert.assertEquals(0.2857, dist.probability(2), 0.0001);

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.0657, dist.probability(5), 0.0001);
        Assert.assertEquals(0.2500, dist.probability(10), 0.0001);
        Assert.assertEquals(0.1272, dist.probability(15), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.9524, dist.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.5238, dist.upperCumulativeProbability(2), 0.0001);

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0.9688, dist.upperCumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.8021, dist.upperCumulativeProbability(10), 0.0001);
        Assert.assertEquals(0.2529, dist.upperCumulativeProbability(15), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, dist.getSupportLowerBound());

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(5, dist.getSupportUpperBound());

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertEquals(20, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(dist.isSupportConnected());

        dist = new HypergeometricDistribution(100, 50, 20);
        Assert.assertTrue(dist.isSupportConnected());
    }
}