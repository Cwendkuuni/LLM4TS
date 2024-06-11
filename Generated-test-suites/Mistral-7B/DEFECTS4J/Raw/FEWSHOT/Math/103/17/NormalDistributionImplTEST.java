package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testConstructors() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.001);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);

        distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double p = distribution.cumulativeProbability(x);
            assertEquals(p, Erf.erf((x - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0))), 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            assertEquals(x, Erf.erfInverse(p / 2.0), 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(1.5);
        assertEquals(1.5, distribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeanNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(-1.0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.5);
        assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationZero() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(0.0);
    }

    @Test
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
    }
}