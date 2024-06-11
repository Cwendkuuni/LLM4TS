package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithValidParameters() {
        NormalDistribution dist = new NormalDistributionImpl(2.0, 3.0);
        assertEquals(2.0, dist.getMean(), 0.001);
        assertEquals(3.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplWithDefaultParameters() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test setting mean.
     */
    @Test
    public void testSettingMean() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(2.5);
        assertEquals(2.5, dist.getMean(), 0.001);
    }

    /**
     * Test setting standard deviation.
     */
    @Test
    public void testSettingStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(2.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test method cumulativeProbability with valid parameters.
     */
    @Test
    public void testCumulativeProbabilityWithValidParameters() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double result;

        result = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894621366172, result, 0.001);

        result = dist.cumulativeProbability(2.0);
        assertEquals(0.9772459570192317, result, 0.001);

        result = dist.cumulativeProbability(-2.0);
        assertEquals(0.0227540429807683, result, 0.001);
    }

    /**
     * Test method cumulativeProbability with edge cases.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithEdgeCases() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);

        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);

        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    /**
     * Test method inverseCumulativeProbability with valid probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidProbabilities() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double result;

        result = dist.inverseCumulativeProbability(0.1);
        assertEquals(StatUtils.percentile(0.95, NormalDistribution.DISTRIBUTION), result, 0.001);

        result = dist.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.001);

        result = dist.inverseCumulativeProbability(0.95);
        assertEquals(StatUtils.percentile(0.05, NormalDistribution.DISTRIBUTION), result, 0.001);
    }

    /**
     * Test method inverseCumulativeProbability with invalid probability.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-1.0);
    }
}