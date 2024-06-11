package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {
    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl()}.
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testNormalDistributionImplWithMeanStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, distribution.getMean(), 0.0);
        assertEquals(1.2, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, distribution.cumulativeProbability(1.0), 0.001);
        assertEquals(0.158655254, distribution.cumulativeProbability(-1.0), 0.001);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException, IllegalArgumentException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0);
        assertEquals(1.281552, distribution.inverseCumulativeProbability(0.841344746), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#setMean(double)}.
     */
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        assertEquals(3.0, distribution.getMean(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#setStandardDeviation(double)}.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#getMean()}.
     */
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#getStandardDeviation()}.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#setMean(double)}.
     * Throws IllegalArgumentException if standard deviation is not positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(-1.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)} when x is far from mean.
     */
    @Test
    public void testCumulativeProbabilityFarFromMean() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.cumulativeProbability(10.0);
        assertEquals(0.0, result, 0.0);
        result = distribution.cumulativeProbability(-10.0);
        assertEquals(0.0, result, 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)} when p is out of range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityOutOfRange() {
        NormalDistribution distribution = new NormalDistributionImpl();
        distribution.inverseCumulativeProbability(-1.0);
        distribution.inverseCumulativeProbability(2.0);
    }
}