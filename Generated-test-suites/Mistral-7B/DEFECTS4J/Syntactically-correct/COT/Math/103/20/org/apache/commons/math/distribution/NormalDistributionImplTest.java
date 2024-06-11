package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.distribution.NormalDistribution;
import org.junit.Test;

/**
 * Test class for NormalDistributionImpl
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithGivenMeanAndStdDev() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with mean equal to zero and standard deviation equal to one.
     */
    @Test
    public void testNormalDistributionImplDefaultConstructor() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test method getMean.
     */
    @Test
    public void testGetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, normalDist.getMean(), 0.0);

        NormalDistribution normalDistDefault = new NormalDistributionImpl();
        assertEquals(0.0, normalDistDefault.getMean(), 0.0);
    }

    /**
     * Test method setMean.
     */
    @Test
    public void testSetMean() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.0);
        normalDist.setMean(3.5);
        assertEquals(3.5, normalDist.getMean(), 0.0);

        try {
            NormalDistribution normalDistNegativeMean = new NormalDistributionImpl(2.5, 1.0);
            normalDistNegativeMean.setMean(-1.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method getStandardDeviation.
     */
    @Test
    public void testGetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);

        NormalDistribution normalDistDefault = new NormalDistributionImpl();
        assertEquals(1.0, normalDistDefault.getStandardDeviation(), 0.0);
    }

    /**
     * Test method setStandardDeviation.
     */
    @Test
    public void testSetStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(2.5, 1.0);
        normalDist.setStandardDeviation(2.0);
        assertEquals(2.0, normalDist.getStandardDeviation(), 0.0);

        try {
            NormalDistribution normalDistZeroOrNegativeStdDev = new NormalDistributionImpl(2.5, 1.0);
            normalDistZeroOrNegativeStdDev.setStandardDeviation(0.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            NormalDistribution normalDistNegativeStdDev = new NormalDistributionImpl(2.5, 1.0);
            normalDistNegativeStdDev.setStandardDeviation(-1.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double[] values = new double[]{-3.0, -1.5, 0.0, 1.5, 3.0};
        for (double x : values) {
            double cdf = normalDist.cumulativeProbability(x);
            double expectedCdf = 0.5 * (1.0 + Erf.erf((x - 0.0) / (1.0 * Math.sqrt(2.0))));
            assertEquals(expectedCdf, cdf, 0.001);
        }
    }

    /**
     * Test method inverseCumulativeProbability.
     */
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double[] probabilities = new double[]{0.0, 0.25, 0.5, 0.75, 1.0};
        double[] expectedValues = new double[]{-1.28, -0.67, 0.0, 0.67, 1.28};
        for (int i = 0; i < probabilities.length; i++) {
            double p = probabilities[i];
            double x = normalDist.inverseCumulativeProbability(p);
            assertEquals(expectedValues[i], x, 0.001);
        }

        try {
            normalDist.inverseCumulativeProbability(2.0);
            fail("MathException expected");
        } catch (MathException e) {
            // expected
        }

        try {
            normalDist.inverseCumulativeProbability(-1.0);
            fail("MathException expected");
        } catch (MathException e) {
            // expected
        }
    }
}