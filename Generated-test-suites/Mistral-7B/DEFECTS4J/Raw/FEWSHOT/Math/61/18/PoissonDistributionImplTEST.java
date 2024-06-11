package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     */
    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, dist.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test probability of 0.
     */
    @Test
    public void testProbabilityOfZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals(0.240098594554144, dist.probability(0), 0.0000000000001);
    }

    /**
     * Test probability method for negative values.
     */
    @Test
    public void testProbabilityNegativeValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertFalse(dist.probability(-1) > 0.0);
        assertFalse(dist.probability(Integer.MIN_VALUE) > 0.0);
    }

    /**
     * Test probability method for large positive values.
     */
    @Test
    public void testProbabilityLargePositiveValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        assertEquals(0.01011821527949, dist.probability(1000), 0.000000000001);
    }

    /**
     * Test cumulative probability for small positive values.
     */
    @Test
    public void testCumulativeProbabilitySmallPositiveValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals(0.423606797749979, dist.cumulativeProbability(1), 0.000000000001);
        assertEquals(0.445617906258133, dist.cumulativeProbability(2), 0.000000000001);
    }

    /**
     * Test cumulative probability for large positive values.
     */
    @Test
    public void testCumulativeProbabilityLargePositiveValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        assertEquals(0.999999999988098, dist.cumulativeProbability(999), 0.000000000001);
        assertEquals(1.0, dist.cumulativeProbability(1000), 0.000000000001);
    }

    /**
     * Test cumulative probability method for negative values.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.cumulativeProbability(-1);
    }

    /**
     * Test inverse cumulative probability method for negative values.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeValues() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.inverseCumulativeProbability(-1);
    }

    /**
     * Test inverse cumulative probability for probabilities > 1.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityProbabilityGreaterThanOne() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverse cumulative probability for probabilities > 1.
     */
    @Test
    public void testInverseCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals(1, dist.inverseCumulativeProbability(0.5), 0);
        assertEquals(2, dist.inverseCumulativeProbability(0.75), 0);
        assertEquals(3, dist.inverseCumulativeProbability(0.875), 0);
    }

    /**
     * Test normal approximation probability method for small positive values.
     */
    @Test
    public void testNormalApproximationProbabilitySmallPositiveValues() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals(0.423606797749979, dist.probability(1), 0.000000000001);
        assertEquals(0.445617906258133, dist.probability(2), 0.000000000001);
        assertEquals(0.469407228394241, dist.normalApproximateProbability(1), 0.0000000001);
        assertEquals(0.487978621922204, dist.normalApproximateProbability(2), 0.0000000001);
    }

    /**
     * Test normal approximation probability method for large positive values.
     */
    @Test
    public void testNormalApproximationProbabilityLargePositiveValues() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(100.0);
        assertEquals(0.01011821527949, dist.probability(1000), 0.000000000001);
        assertEquals(0.010077329596603, dist.normalApproximateProbability(1000), 0.0000000001);
    }

    /**
     * Test normal approximation probability method for negative values.
     */
    @Test(expected = MathException.class)
    public void testNormalApproximationProbabilityNegativeValues() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.normalApproximateProbability(-1);
    }

    /**
     * Test normal approximation mean and standard deviation.
     */
    @Test
    public void testNormalApproximationMeanAndStandardDeviation() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        double mean = dist.getMean();
        double stddev = new StandardDeviation().evaluate(dist);
        PoissonDistribution poisson = new PoissonDistribution(mean);
        double normalMean = poisson.getMean();
        double normalStddev = Math.sqrt(normalMean);
        assertEquals(mean, normalMean, 0.0000000001);
        assertEquals(stddev, normalStddev, 0.0000000001);
    }
}