package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor NormalDistributionImpl(mean, sd)
     */
    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Incorrect mean value", 1.0, dist.getMean(), 0.001);
        assertEquals("Incorrect standard deviation value", 2.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor NormalDistributionImpl()
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals("Incorrect mean value", 0.0, dist.getMean(), 0.001);
        assertEquals("Incorrect standard deviation value", 1.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test getMean() method
     */
    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Incorrect mean value", 1.0, dist.getMean(), 0.001);
    }

    /**
     * Test setMean(mean) method
     */
    @Test
    public void testSetMean() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setMean(3.0);
        assertEquals("Incorrect mean value after setting", 3.0, dist.getMean(), 0.001);
    }

    /**
     * Test getStandardDeviation() method
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Incorrect standard deviation value", 2.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test setStandardDeviation(sd) method
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl();
        dist.setStandardDeviation(5.0);
        assertEquals("Incorrect standard deviation value after setting", 5.0, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test cumulativeProbability(x) method with positive x
     */
    @Test
    public void testCumulativeProbabilityPositiveX() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.5 * (1.0 + Erf.erf(1.0 / Math.sqrt(2.0)));
        double actual = dist.cumulativeProbability(1.0);
        assertEquals("Incorrect cumulative probability for positive x", expected, actual, 0.001);
    }

    /**
     * Test cumulativeProbability(x) method with negative x
     */
    @Test
    public void testCumulativeProbabilityNegativeX() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = 0.5 * (1.0 + Erf.erf(-1.0 / Math.sqrt(2.0)));
        double actual = dist.cumulativeProbability(-1.0);
        assertEquals("Incorrect cumulative probability for negative x", expected, actual, 0.001);
    }

    /**
     * Test inverseCumulativeProbability(p) method with p close to 0
     */
    @Test
    public void testInverseCumulativeProbabilitySmallP() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = Math.sqrt(2.0) * Erf.erfInverse(0.0);
        double actual = dist.inverseCumulativeProbability(0.01);
        assertEquals("Incorrect inverse cumulative probability for small p", expected, actual, 0.001);
    }

    /**
     * Test inverseCumulativeProbability(p) method with p close to 1
     */
    @Test
    public void testInverseCumulativeProbabilityNearOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double expected = Math.sqrt(2.0) * Erf.erfInverse(0.99);
        double actual = dist.inverseCumulativeProbability(0.99);
        assertEquals("Incorrect inverse cumulative probability for p near 1", expected, actual, 0.001);
    }

    /**
     * Test inverseCumulativeProbability(p) method with p = 0
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityZero() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(0.0);
    }

    /**
     * Test inverseCumulativeProbability(p) method with p = 1
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOne() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.0);
    }

    /**
     * Test inverseCumulativeProbability(p) method with very large p
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityLargeP() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverseCumulativeProbability(p) method with very small p
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilitySmallP() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability(p) method with p outside the interval [0, 1]
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityOutOfRange() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(2.0);
    }
}