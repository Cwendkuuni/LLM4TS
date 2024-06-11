package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(0.10769910550632691, dist.density(2), EPSILON);
        Assert.assertEquals(0.022108275772161914, dist.density(3), EPSILON);
        Assert.assertEquals(0.07291231010464484, dist.density(4), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(0, dist.cumulativeProbability(0), EPSILON);
        Assert.assertEquals(0.6999999999999998, dist.cumulativeProbability(1), EPSILON);
        Assert.assertEquals(0.8999999999999999, dist.cumulativeProbability(2), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(1.5, dist.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(6, dist.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(0, dist.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(2, 3);
        Assert.assertTrue(dist.isSupportConnected());
    }
}