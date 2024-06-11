package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.random.RandomDataGenerators;
import org.junit.Test;

public class PoissonDistributionImplTest {
    @Test
    public void testCreateWithPositiveMean() {
        PoissonDistribution distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCreateWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        assertEquals(FastMath.exp(-0.5), distribution.probability(0), 0.001);
    }

    @Test
    public void testProbabilityNonZero() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        double probability = distribution.probability(1);
        assertTrue(probability > 0);
        assertEquals(FastMath.exp(-1.5) / FastMath.sqrt(2 * FastMath.PI), probability, 0.001);
    }

    @Test
    public void testProbabilityMaxValue() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        double probability = distribution.probability(Integer.MAX_VALUE);
        assertEquals(0, probability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        assertEquals(0, distribution.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilityNonZero() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = distribution.cumulativeProbability(1);
        assertTrue(cumulativeProbability > 0);
        assertEquals(FastMath.exp(-1.5) / (FastMath.sqrt(2 * FastMath.PI) * (1 + FastMath.E)) + 0.5 * FastMath.erf(FastMath.sqrt(1.5) / FastMath.SQRT_2), cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = distribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1, cumulativeProbability, 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(0.5);
        assertEquals(FastMath.Exp.E, distribution.normalApproximateProbability(0), 0.001);
    }

    @Test
    public void testNormalApproximateProbabilityNonZero() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        double probability = distribution.normalApproximateProbability(1);
        assertEquals(FastMath.exp(-1.5) / FastMath.sqrt(2 * FastMath.PI), probability, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        RandomDataGenerators rng = new RandomDataGenerators();
        PoissonDistribution sampleDistribution = new PoissonDistributionImpl(distribution.sample(rng));
        assertEquals(1.5, sampleDistribution.getMean(), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        assertEquals(0, distribution.getDomainLowerBound(0.1), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistribution distribution = new PoissonDistributionImpl(1.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.1), 0);
    }
}