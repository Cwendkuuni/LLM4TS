package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.junit.Test;

/**
 * Test cases for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     */
    @Test
    public void testPoissonDistributionImplPositiveMean() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, poissonDistribution.getMean(), 0.001);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testPoissonDistributionImplNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test probability method with zero.
     */
    @Test
    public void testProbabilityZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double probability = poissonDistribution.probability(0);
        assertEquals(0.22312757356898292, probability, 0.0001);
    }

    /**
     * Test probability method with positive integer.
     */
    @Test
    public void testProbabilityPositiveInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double probability = poissonDistribution.probability(1);
        assertEquals(0.21693568843684257, probability, 0.0001);
    }

    /**
     * Test probability method with large integer.
     */
    @Test
    public void testProbabilityLargeInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double probability = poissonDistribution.probability(100);
        assertEquals(0.0000004169387229453927, probability, 0.0000001);
    }

    /**
     * Test probability method with negative integer.
     */
    @Test
    public void testProbabilityNegativeInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double probability = poissonDistribution.probability(-1);
        assertEquals(0.0, probability, 0.0);
    }

    /**
     * Test cumulative probability method with zero.
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.04986647236028484, cumulativeProbability, 0.0001);
    }

    /**
     * Test cumulative probability method with positive integer.
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityPositiveInteger() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.2608116393834726, cumulativeProbability, 0.0001);
    }

    /**
     * Test cumulative probability method with large integer.
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityLargeInteger() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(100);
        assertEquals(0.9999999999999993, cumulativeProbability, 0.0001);
    }

    /**
     * Test cumulative probability method with negative integer.
     * @throws MathException
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInteger() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        poissonDistribution.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with zero.
     */
    @Test
    public void testNormalApproximateProbabilityZero() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(0.22312757356898292, normalApproximateProbability, 0.0001);
    }

    /**
     * Test normalApproximateProbability method with positive integer.
     */
    @Test
    public void testNormalApproximateProbabilityPositiveInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(1);
        assertEquals(0.21693568843684257, normalApproximateProbability, 0.0001);
    }

    /**
     * Test normalApproximateProbability method with large integer.
     */
    @Test
    public void testNormalApproximateProbabilityLargeInteger() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(100);
        assertEquals(0.0000004169387229453927, normalApproximateProbability, 0.0000001);
    }

    /**
     * Test sample method with small mean.
     * @throws MathException
     */
    @Test
    public void testSampleSmallMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(0.1);
        int sample = poissonDistribution.sample();
        assertEquals(1, sample);
    }

    /**
     * Test sample method with large mean.
     * @throws MathException
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        int sample = poissonDistribution.sample();
        assertEquals(57, sample);
    }

    /**
     * Test inverseCumulativeProbability method with small mean.
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbabilitySmallMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(0.1);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.1);
        assertEquals(1, inverseCumulativeProbability);
    }

    /**
     * Test inverseCumulativeProbability method with large mean.
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbabilityLargeMean() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(100.0);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.99);
        assertEquals(54, inverseCumulativeProbability);
    }

    /**
     * Test inverseCumulativeProbability method with negative probabilities.
     * @throws MathException
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(1.5);
        poissonDistribution.inverseCumulativeProbability(-0.1);
    }
}