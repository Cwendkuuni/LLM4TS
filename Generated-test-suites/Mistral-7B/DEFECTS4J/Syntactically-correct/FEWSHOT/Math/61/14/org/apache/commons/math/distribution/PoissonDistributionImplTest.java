package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testNewInstancePositiveMean() {
        double mean = 2.5;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertNotNull(distribution.normal);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNewInstanceNegativeMean() {
        double mean = -2.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityZero() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(FastMath.exp(-mean), distribution.probability(0), 0.0);
    }

    @Test
    public void testProbabilityPositive() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int x = 2;
        double probability = distribution.probability(x);
        assertEquals(FastMath.exp(-mean) * FastMath.pow(mean, x) / Math.pow(FastMath.E, x), probability, 0.01);
    }

    @Test
    public void testProbabilityMaxInteger() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int x = Integer.MAX_VALUE;
        double probability = distribution.probability(x);
        assertEquals(0.0, probability, 0.0);
    }

    @Test
    public void testProbabilityNegative() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int x = -1;
        double probability = distribution.probability(x);
        assertEquals(0.0, probability, 0.0);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double cdf = distribution.cumulativeProbability(0);
        assertEquals(FastMath.exp(-mean), cdf, 0.01);
    }

    @Test
    public void testCumulativeProbabilityMaxInteger() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double cdf = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cdf, 0.01);
    }

    @Test
    public void testCumulativeProbabilityBetweenZeroAndMaxInteger() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int x = 5;
        double cdf = distribution.cumulativeProbability(x);
        assertTrue(cdf > FastMath.exp(-mean) && cdf < 1.0);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double cdf = distribution.normalApproximateProbability(0);
        assertEquals(FastMath.exp(-mean), cdf, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityMaxInteger() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double cdf = distribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cdf, 0.01);
    }

    @Test
    public void testNormalApproximateProbabilityBetweenZeroAndMaxInteger() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int x = 5;
        double cdf = distribution.normalApproximateProbability(x);
        assertTrue(Precision.isClose(cdf, distribution.cumulativeProbability(x), 0.01));
    }

    @Test
    public void testSample() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        assertEquals(Precision.isClose(mean, (double) sample / 1000.0, 0.01), true);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = distribution.cumulativeProbability(0);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(0, x);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = distribution.cumulativeProbability(Integer.MAX_VALUE);
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE, x);
    }

    @Test
    public void testInverseCumulativeProbabilityBetweenZeroAndOne() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = distribution.cumulativeProbability(5);
        int x = distribution.inverseCumulativeProbability(p);
        int expected = 5;
        assertEquals(expected, x);
    }

    @Test
    public void testInverseCumulativeProbabilityGreaterThanOne() throws MathException {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double p = distribution.cumulativeProbability(Integer.MAX_VALUE) + 0.1;
        int x = distribution.inverseCumulativeProbability(p);
        assertFalse(x >= 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(0, distribution.getDomainLowerBound(0.0));
    }

    @Test
    public void testGetDomainUpperBound() {
        double mean = 1.0;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0));
    }

    @Test
    public void testCumulativeProbabilityWithCustomEpsilonAndMaxIterations() throws MathException {
        double mean = 1.0;
        double epsilon = 1E-15;
        int maxIterations = 100000;
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean, epsilon, maxIterations);
        int x = 5;
        double cdf = distribution.cumulativeProbability(x);
        assertTrue(Precision.isClose(cdf, Gamma.regularizedGammaQ((double) x + 1, mean, epsilon, maxIterations), 0.01));
    }
}