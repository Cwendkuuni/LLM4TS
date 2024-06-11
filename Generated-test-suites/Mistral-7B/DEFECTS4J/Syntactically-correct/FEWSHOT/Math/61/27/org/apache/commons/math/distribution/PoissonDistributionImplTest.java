package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstructorWithValidMean() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean());
    }

    @Test
    public void testPoissonDistributionImplConstructorWithValidMeanAndEpsilon() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5, 1E-12);
        assertEquals(2.5, poissonDistribution.getMean());
        assertEquals(1E-12, poissonDistribution.epsilon, 0.0);
    }

    @Test
    public void testPoissonDistributionImplConstructorWithValidMeanAndMaxIterations() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5, 1000000);
        assertEquals(2.5, poissonDistribution.getMean());
        assertEquals(1000000, poissonDistribution.maxIterations, 0);
    }

    @Test
    public void testPoissonDistributionImplConstructorWithValidMeanAndEpsilonAndMaxIterations() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5, 1E-12, 1000000);
        assertEquals(2.5, poissonDistribution.getMean());
        assertEquals(1E-12, poissonDistribution.epsilon, 0.0);
        assertEquals(1000000, poissonDistribution.maxIterations, 0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProbabilityNegativeValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(-1);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(0);
        assertEquals(FastMath.exp(-2.5), probability, 0.0001);
    }

    @Test
    public void testProbabilityPositiveValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double probability = poissonDistribution.probability(1);
        assertEquals(FastMath.exp(-2.5) * FastMath.exp(1) / Math.sqrt(2 * Math.PI), probability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(FastMath.exp(-2.5), cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityOne() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        double expected = FastMath.exp(-2.5 + FastMath.log(FastMath.E)) / Math.sqrt(2 * Math.PI) + FastMath.exp(-2.5);
        assertEquals(cumulativeProbability, expected, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxIterationsAndEpsilon() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5, 1E-12, 100000);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(10);
        assertEquals(cumulativeProbability, Gamma.regularizedGammaQ(10.0 + 1.0, 2.5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(FastMath.exp(-2.5), normalApproximateProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityPositiveValue() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        double expected = 0.398942280401433; // Approximate value of the normal distribution function at x = 1 for Poisson distribution with mean = 2.5
        assertEquals(normalApproximateProbability, expected, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        int expectedMin = (int) (2.5 * 0.9);
        int expectedMax = (int) (2.5 * 10);
        assertTrue(sample >= expectedMin);
        assertTrue(sample <= expectedMax);
    }
}