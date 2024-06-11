package org.apache.commons.math.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test cases for NormalDistributionImpl class
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation
     * @throws MathException
     */
    @Test
    public void testNormalDistributionImplWithGivenMeanAndStdDev() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(3.0, 2.5);
        assertEquals(3.0, normalDist.getMean(), 0.0);
        assertEquals(2.5, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with default mean and standard deviation
     */
    @Test
    public void testNormalDistributionImplWithDefaultMeanAndStdDev() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test setting mean
     */
    @Test
    public void testSettingMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(5.0);
        assertEquals(5.0, normalDist.getMean(), 0.0);
    }

    /**
     * Test setting standard deviation
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSettingNegativeStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(-1.0);
    }

    /**
     * Test cumulative probability method
     * @throws MathException
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        double result = normalDist.cumulativeProbability(0.5);
        // Using StatUtils.pdf() as a reference
        double expected = StatUtils.pdf(0.5, 0.0, 1.0);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test inverse cumulative probability method
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        double p = 0.95;
        double expected = Erf.erfinv(2 * p - 1) * normalDist.getStandardDeviation() + normalDist.getMean();
        double result = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test inverse cumulative probability with edge cases
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();

        // Test p = 0
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        double result = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, result);

        // Test p = 1
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        result = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, result);

        // Test p = 0.5
        p = 0.5;
        expected = normalDist.getMean();
        result = normalDist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.0);
    }

    /**
     * Test inverse cumulative probability with exception handling
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        double p = 1.5;
        normalDist.inverseCumulativeProbability(p);
    }
}