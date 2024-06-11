package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithMeanAndStdDev() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(2.0, 3.0);
        assertNotNull(normalDistribution);
        assertEquals(2.0, normalDistribution.getMean(), 0.0);
        assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        assertNotNull(normalDistribution);
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test getMean method.
     */
    @Test
    public void testGetMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.0);
    }

    /**
     * Test setMean method.
     */
    @Test
    public void testSetMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.0);
    }

    /**
     * Test IllegalArgumentException when setting negative mean.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMean() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(-1.0);
    }

    /**
     * Test getStandardDeviation method.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test setStandardDeviation method.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(4.0);
        assertEquals(4.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test IllegalArgumentException when setting non-positive standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNonPositiveStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(0.0);
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double result = normalDistribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.001);
    }

    /**
     * Test MathException when x is more than 20 standard deviations from the mean.
     */
    @Test(expected = MathException.class)
    public void testMathExceptionWithXFarFromMean() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        double x = 100.0 * normalDistribution.getStandardDeviation();
        normalDistribution.cumulativeProbability(x);
    }

    /**
     * Test inverseCumulativeProbability method with p = 0.
     */
    @Test
    public void testInverseCumulativeProbabilityWithPZero() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        double result = normalDistribution.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with p = 1.
     */
    @Test
    public void testInverseCumulativeProbabilityWithPOne() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        double result = normalDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    /**
     * Test IllegalArgumentException when setting invalid probability.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithInvalidProbability() {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.inverseCumulativeProbability(2.0);
    }

    /**
     * Test MathException when inverse cumulative probability can't be computed.
     */
    @Test(expected = MathException.class)
    public void testMathExceptionWhenInverseCumulativeProbabilityCannotBeComputed() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(0.0);
        normalDistribution.setStandardDeviation(0.0);
        normalDistribution.inverseCumulativeProbability(0.5);
    }
}