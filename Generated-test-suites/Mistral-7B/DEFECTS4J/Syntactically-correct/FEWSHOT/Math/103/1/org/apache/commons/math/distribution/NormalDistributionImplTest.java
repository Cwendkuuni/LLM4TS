package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(1.2, dist.getStandardDeviation(), 0.001);

        NormalDistributionImpl distZeroOne = new NormalDistributionImpl();
        assertEquals(0.0, distZeroOne.getMean(), 0.001);
        assertEquals(1.0, distZeroOne.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(1.0, -1.2);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        double p = 0.6827;
        double expected = 0.5 * (1.0 + Erf.erf((1.2 * (p - 2.5)) / (1.2 * Math.sqrt(2.0))));
        double result = dist.cumulativeProbability(2.5 + 1.2 * p);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutsideRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        double x = 2.5 + 1.2 * 10;
        dist.cumulativeProbability(x);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);

        double p = 0.1587; // the value given in the javadoc
        double expected = 2.5 + 1.2 * Erf.erfInv(p / 2.0 - 0.5);
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(expected, result, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        dist.inverseCumulativeProbability(1.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.2);
        dist.inverseCumulativeProbability(1.5D);
    }
}