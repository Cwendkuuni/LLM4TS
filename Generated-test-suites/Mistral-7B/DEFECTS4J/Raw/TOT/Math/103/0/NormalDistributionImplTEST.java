package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test mean getter and setter.
     */
    @Test
    public void testMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(1.5);
        assertEquals(1.5, dist.getMean(), 0.0);
    }

    /**
     * Test standard deviation getter and setter.
     */
    @Test
    public void testStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(2.5);
        assertEquals(2.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.23;
        assertEquals(p, dist.cumulativeProbability(dist.inverseCumulativeProbability(p)), 0.001);
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.23;
        assertEquals(dist.inverseCumulativeProbability(p), dist.cumulativeProbability(p), 0.001);
    }

    /**
     * Test mean and standard deviation equals to zero.
     */
    @Test
    public void testZeroMeanAndStandardDeviation() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(0.0, dist.getStandardDeviation(), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.0);
        assertEquals(0.5, dist.cumulativeProbability(1.0), 0.0);
    }

    /**
     * Test mean and standard deviation not equal to zero.
     */
    @Test
    public void testNonZeroMeanAndStandardDeviation() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.0);
        assertEquals(2.0, dist.getStandardDeviation(), 0.0);
        StandardDeviation sd = new StandardDeviation();
        double[] values = new double[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(Math.random());
        }
        double avg = sd.evaluate(values);
        assertEquals(dist.getStandardDeviation(), avg, 0.01);
    }

    /**
     * Test mean and standard deviation equals to negative values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMeanOrStandardDeviation() {
        new NormalDistributionImpl(-1.0, 2.0);
    }

    /**
     * Test mean and standard deviation equals to zero or negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroOrNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, 0.0);
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test extreme values of mean and standard deviation.
     */
    @Test(expected = MathException.class)
    public void testExtremeValues() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }
}