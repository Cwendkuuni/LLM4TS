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
     * Test constructor with mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
        assertEquals(0.5, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with mean equals to zero and standard deviation equals to one.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test getMean method.
     */
    @Test
    public void testGetMean() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    /**
     * Test setMean method.
     */
    @Test
    public void testSetMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(3.5);
        assertEquals(3.5, distribution.getMean(), 0.001);
    }

    /**
     * Test IllegalArgumentException for negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegativeMean() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setMean(-2.5);
    }

    /**
     * Test getStandardDeviation method.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(0.5, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test setStandardDeviation method.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
    }

    /**
     * Test IllegalArgumentException for non-positive standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegativeOrZero() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-2.5);
    }

    /**
     * Test cumulativeProbability method for valid x.
     */
    @Test
    public void testCumulativeProbabilityValidX() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.6827, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method for x equals to mean.
     */
    @Test
    public void testCumulativeProbabilityMean() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(0);
        assertEquals(0.5, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method for x equals to mean + standard deviation.
     */
    @Test
    public void testCumulativeProbabilityMeanPlusStandardDeviation() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(1);
        assertEquals(0.8413, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method for x equals to mean - standard deviation.
     */
    @Test
    public void testCumulativeProbabilityMeanMinusStandardDeviation() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(-1);
        assertEquals(0.1587, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method for x large than 3 standard deviations from mean.
     */
    @Test
    public void testCumulativeProbabilityXLargeThanThreeStandardDeviationsFromMean() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(3);
        assertEquals(0.9773, result, 0.0001);
    }

    /**
     * Test cumulativeProbability method for x large than 20 standard deviations from mean.
     */
    @Test
    public void testCumulativeProbabilityXLargeThanTwentyStandardDeviationsFromMean() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(20);
        assertEquals(1.0, result, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method for p equals to 0.
     */
    @Test
    public void testInverseCumulativeProbabilityPZero() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method for p equals to 1.
     */
    @Test
    public void testInverseCumulativeProbabilityPOne() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method for valid p.
     */
    @Test
    public void testInverseCumulativeProbabilityValidP() throws MathException, InterruptedException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double result = distribution.inverseCumulativeProbability(p);
        double expected = StatUtils.percentile(StatUtils.sample(1000000, distribution), p);
        assertEquals(expected, result, 0.0001);
    }

    /**
     * Test inverseCumulativeProbability method for p less than 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityPLessThanZero() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability method for p greater than 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityPGreaterThanOne() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.1);
    }

}