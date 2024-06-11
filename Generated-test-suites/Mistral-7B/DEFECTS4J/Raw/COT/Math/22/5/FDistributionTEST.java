package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test cases for FDistribution class.
 */
public class FDistributionTest {

    private FDistribution dist;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        final double numeratorDegreesOfFreedom = 3.0;
        final double denominatorDegreesOfFreedom = 4.0;
        dist = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        FDistribution dist = new FDistribution(1.5, 2.0);
        Assert.assertEquals(1.5, dist.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double density = dist.density(x);
        Assert.assertNotNull(density);
        x = 1.0;
        density = dist.density(x);
        Assert.assertTrue(density > 0);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.0;
        double p = dist.cumulativeProbability(x);
        Assert.assertEquals(0.0, p, 1e-11);
        x = 1.0;
        p = dist.cumulativeProbability(x);
        Assert.assertEquals(1.0, p, 1e-11);
        x = 0.5;
        p = dist.cumulativeProbability(x);
        Assert.assertTrue(Precision.isClose(p, 0.9, 1e-11));
    }

    @Test
    public void testNumericalMean() {
        double numericalMean = dist.getNumericalMean();
        Assert.assertEquals(1.5 / (1.5 - 2), numericalMean, 1e-11);
    }

    @Test
    public void testNumericalVariance() {
        double numericalVariance = dist.getNumericalVariance();
        if (Double.isNaN(numericalVariance)) {
            Assert.fail("Numerical variance is not defined for given degrees of freedom.");
        }
        double expectedVariance = (2 * 4 * (3.0 + 4.0) / (3.0 * (4.0 - 2) * (4.0 - 4)));
        Assert.assertEquals(expectedVariance, numericalVariance, 1e-11);
    }

    @Test
    public void testSupportBounds() {
        double lowerBound = dist.getSupportLowerBound();
        Assert.assertEquals(0, lowerBound, 0);
        double upperBound = dist.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 0);
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testRandomValues() {
        final int numberOfSamples = 100000;
        double[] samples = new double[numberOfSamples];
        for (int i = 0; i < numberOfSamples; i++) {
            samples[i] = dist.inverseCumulativeProbability(RandomUtils.nextDouble());
        }
        double mean = 0.0;
        for (int i = 0; i < numberOfSamples; i++) {
            mean += samples[i];
        }
        mean /= numberOfSamples;
        double numericalMean = dist.getNumericalMean();
        Assert.assertTrue(Precision.isClose(mean, numericalMean, 1e-5));

        double variance = 0.0;
        for (int i = 0; i < numberOfSamples; i++) {
            variance += Math.pow(samples[i] - mean, 2);
        }
        variance /= numberOfSamples;
        double numericalVariance = dist.getNumericalVariance();
        Assert.assertTrue(Precision.isClose(variance, numericalVariance, 1e-5));
    }

    
}