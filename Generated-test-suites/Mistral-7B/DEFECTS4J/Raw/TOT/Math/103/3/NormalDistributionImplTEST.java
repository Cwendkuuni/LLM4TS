package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test the constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test the constructor with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImplConstructionInvalidArguments() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    /**
     * Test the cumulativeProbability method with valid arguments.
     */
    @Test
    public void testNormalDistributionImplCumulativeProbabilityValidArguments() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            double expected = 0.5 * (1.0 + Erf.erf((x - mean) / (sd * Math.sqrt(2.0))));
            assertEquals(expected, cdf, 0.001);
        }
    }

    /**
     * Test the cumulativeProbability method with edge cases.
     */
    @Test
    public void testNormalDistributionImplCumulativeProbabilityEdgeCases() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        double cdf = dist.cumulativeProbability(-100.0);
        assertEquals(0.0, cdf, 0.0);
        cdf = dist.cumulativeProbability(Double.POSITIVE_INFINITY);
        assertEquals(1.0, cdf, 0.0);
    }

    /**
     * Test the cumulativeProbability method with error cases.
     */
    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityErrorCases() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        dist.cumulativeProbability(Double.NaN);
    }

    /**
     * Test the inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testNormalDistributionImplInverseCumulativeProbabilityValidArguments() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = mean + sd * Erf.erfinv(2 * p - 1.0);
            assertEquals(expected, x, 0.001);
        }
    }

    /**
     * Test the inverseCumulativeProbability method with edge cases.
     */
    @Test
    public void testNormalDistributionImplInverseCumulativeProbabilityEdgeCases() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        double x = dist.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);
        x = dist.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
    }

    /**
     * Test
     * {@link #testNormalDistributionImplInverseCumulativeProbabilityInvalidArguments()}
     * with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityInvalidArguments() {
        final double mean = 0.5;
        final double sd = 1.0;
        NormalDistributionImpl dist = new NormalDistributionImpl(mean, sd);
        dist.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test the setMean method with valid arguments.
     */
    @Test
    public void testNormalDistributionImplSetMeanValidArguments() {
        final NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(0.5);
        assertEquals(0.5, dist.getMean(), 0.0);
    }

    /**
     * Test the setMean method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImplSetMeanInvalidArguments() {
        final NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setMean(-1.0);
    }

    /**
     * Test the setStandardDeviation method with valid arguments.
     */
    @Test
    public void testNormalDistributionImplSetStandardDeviationValidArguments() {
        final NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(1.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test the setStandardDeviation method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNormalDistributionImplSetStandardDeviationInvalidArguments() {
        final NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(0.0);
    }
}