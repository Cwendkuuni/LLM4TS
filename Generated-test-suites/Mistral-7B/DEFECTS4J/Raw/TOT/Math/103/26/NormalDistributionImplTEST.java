package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descript.StatUtils;
import org.junit.Test;

import org.apache.commons.math.MathException;
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
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#NormalDistributionImpl(double, double)}.
     */
    @Test
    public void testParametricConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(0.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.75;
        double expected = 0.67242661;
        double result = dist.cumulativeProbability(p);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double p = 0.75;
        double expected = 0.34356821;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test method for {@link NormalDistributionImpl#inverseCumulativeProbability(double)}
     * with edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        // Test with p = 0
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(0.0));
        // Test with p = 1
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.0));
    }

    /**
     * Test method for {@link NormalDistributionImpl#setMean(double)} and {@link NormalDistributionImpl#setStandardDeviation(double)}.
     */
    @Test
    public void testSetMeanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setMean(2.5);
        dist.setStandardDeviation(0.5);
        assertEquals(2.5, dist.getMean(), 0.0);
        assertEquals(0.5, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#setStandardDeviation(double)} with illegal argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationIllegalArgument() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(-1.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#mean()}.
     */
    @Test
    public void testGetMean() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(2.5, dist.getMean(), 0.0);
        NormalDistribution distDefault = new NormalDistributionImpl();
        assertEquals(0.0, distDefault.getMean(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#standardDeviation()}.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(2.5, 0.5);
        assertEquals(0.5, dist.getStandardDeviation(), 0.0);
        NormalDistribution distDefault = new NormalDistributionImpl();
        assertEquals(1.0, distDefault.getStandardDeviation(), 0.0);
    }

    /**
     * Test method for {@link NormalDistributionImpl#statistic()}.
     */
    @Test
    public void testStatistic() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double[] data = new double[10000];
        for (int i = 0; i < data.length; i++) {
            data[i] = dist.inverseCumulativeProbability(StatUtils.uniform(0, 1));
        }
        double meanExpected = 0.0;
        double stdDevExpected = 1.0;
        assertEquals(meanExpected, dist.getMean(), 0.001);
        assertEquals(stdDevExpected, dist.getStandardDeviation(), 0.001);
        assertEquals(stdDevExpected, StatUtils.standardDeviation(data), 0.001);
        assertEquals(meanExpected, StatUtils.mean(data), 0.001);
    }
}