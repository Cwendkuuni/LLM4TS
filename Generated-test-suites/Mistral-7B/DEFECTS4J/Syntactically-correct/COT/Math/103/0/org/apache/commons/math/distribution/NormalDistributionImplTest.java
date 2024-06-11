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
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStdDev() {
        NormalDistribution dist = new NormalDistributionImpl(1.5, 2.5);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertEquals(2.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with default values.
     */
    @Test
    public void testNormalDistributionImplWithDefaultValues() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test setting mean and standard deviation.
     */
    @Test
    public void testSettingMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(2.5);
        dist.setStandardDeviation(1.5);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(1.5, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test throwing IllegalArgumentException when setting negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.5);
    }

    /**
     * Test cumulative probability method with given x.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0.5);
        assertEquals(0.6826894621366172, result, 0.0001);
    }

    /**
     * Test cumulative probability method when x is more than 20 standard deviations from the mean.
     */
    @Test
    public void testCumulativeProbabilityWithLargeX() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(30);
        assertEquals(1.0, result, 0.0001);
    }

    /**
     * Test inverse cumulative probability method with given p.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0.25);
        assertEquals(-0.6744897501708598, result, 0.0001);
    }

    /**
     * Test inverse cumulative probability method with p=0 and p=1.
     */
    @Test
    public void testInverseCumulativeProbabilityWithZeroAndOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);

        result = dist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test inverse cumulative probability method with invalid p.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidP() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(-0.1);
    }
}