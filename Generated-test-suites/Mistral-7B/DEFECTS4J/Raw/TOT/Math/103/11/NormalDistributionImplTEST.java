package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Unit tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplValidConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean.
     */
    @Test
    public void testSettingMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }

    /**
     * Test setting standard deviation.
     */
    @Test
    public void testSettingStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(4.0);
        assertEquals(4.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting negative standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-2.0);
    }

    /**
     * Test CDF calculation with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.0001);
    }

    /**
     * Test CDF calculation with large input.
     */
    @Test
    public void testCumulativeProbabilityLargeInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(100.0);
        assertEquals(0.5, result, 0.0001);
    }

    /**
     * Test CDF calculation with small input.
     */
    @Test
    public void testCumulativeProbabilitySmallInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(-5.0);
        assertEquals(0.5, result, 0.0001);
    }

    /**
     * Test CDF calculation with input far from mean.
     */
    @Test
    public void testCumulativeProbabilityInputFarFromMean() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(5.0);
        assertEquals(0.154, result, 0.01);
    }

    /**
     * Test CDF calculation with large absolute input.
     */
    @Test
    public void testCumulativeProbabilityLargeAbsoluteInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(50.0);
        assertEquals(0.0, result, 0.001);
    }

    /**
     * Test CDF calculation with large negative input.
     */
    @Test
    public void testCumulativeProbabilityLargeNegativeInput() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        double result = distribution.cumulativeProbability(-50.0);
        assertEquals(1.0, result, 0.001);
    }

    /**
     * Test inverse CDF calculation with valid probabilities.
     */
    @Test
    public void testInverseCumulativeProbabilityValidProbabilities() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.5, distribution.inverseCumulativeProbability(0.5), 0.0);
        assertEquals(1.0, distribution.inverseCumulativeProbability(1.0), 0.0);

        double mean = 2.0;
        double stdDev = 3.0;
        NormalDistributionImpl distribution2 = new NormalDistributionImpl(mean, stdDev);
        assertEquals(mean - stdDev, distribution2.inverseCumulativeProbability(0.1587), 0.1); // N(0.1587)
        assertEquals(mean, distribution2.inverseCumulativeProbability(0.5), 0.1); // N(0.5)
        assertEquals(mean + stdDev, distribution2.inverseCumulativeProbability(0.8413), 0.1); // N(0.8413)
    }

    /**
     * Test inverse CDF calculation with invalid probabilities.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverse CDF calculation with probability greater than one.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityProbabilityGreaterThanOne() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverse CDF calculation with probability less than zero.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityProbabilityLessThanZero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-0.1);
    }
}