package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMean() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 1.0);
        distribution.setMean(2.0);
        assertEquals(2.0, distribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMeanNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(-1.0);
    }

    @Test
    public void testStandardDeviation() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 2.0);
        distribution.setStandardDeviation(1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardDeviationZeroOrNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(0.0);
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double p = distribution.cumulativeProbability(x);
            double expected = 0.5 + 0.5 * Erf.erf(x / (Math.sqrt(2.0) * 1.0));
            assertEquals(expected, p, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutsideRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = distribution.inverseCumulativeProbability(p);
            double expected = p * 2.0 * Math.sqrt(2.0) + 0.0;
            assertEquals(expected, x, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(1.01);
    }

}