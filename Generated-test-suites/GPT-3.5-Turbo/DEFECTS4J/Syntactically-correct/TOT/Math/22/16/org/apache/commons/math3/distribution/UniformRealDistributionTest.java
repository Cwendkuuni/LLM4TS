package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(1.0, distribution.density(0.5), 0.00001);

        // edge cases
        Assert.assertEquals(0.0, distribution.density(-0.5), 0.00001);
        Assert.assertEquals(0.0, distribution.density(1.5), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), 0.00001);

        // edge cases
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-0.5), 0.00001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(1.5), 0.00001);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(0.5, distribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(0.083333, distribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.00001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.00001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // typical case
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample < 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidLowerBound() {
        new UniformRealDistribution(2, 1);
    }
}