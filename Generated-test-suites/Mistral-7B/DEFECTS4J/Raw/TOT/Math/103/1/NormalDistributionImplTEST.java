package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatFunctions;
import org.junit.Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 3.2);
        assertEquals(1.5, normalDist.getMean(), 0.0);
        assertEquals(3.2, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with default mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplDefault() {
        NormalDistribution normalDist = new NormalDistributionImpl();
        assertEquals(0.0, normalDist.getMean(), 0.0);
        assertEquals(1.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test mean accessor and modifier.
     */
    @Test
    public void testMean() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 3.2);
        assertEquals(1.5, normalDist.getMean(), 0.0);
        normalDist.setMean(2.0);
        assertEquals(2.0, normalDist.getMean(), 0.0);
    }

    /**
     * Test standard deviation accessor and modifier.
     */
    @Test
    public void testStandardDeviation() {
        NormalDistribution normalDist = new NormalDistributionImpl(1.5, 3.2);
        assertEquals(3.2, normalDist.getStandardDeviation(), 0.0);
        normalDist.setStandardDeviation(5.0);
        assertEquals(5.0, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test cumulative probability with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        for (int i = -5; i <= 5; i++) {
            double x = i;
            double p = normalDist.cumulativeProbability(x);
            double expected = 0.5 * (1 + Erf.erf((x - normalDist.getMean()) /
                    (normalDist.getStandardDeviation() * Math.sqrt(2.0))));
            assertEquals(expected, p, 0.0001);
        }
    }

    /**
     * Test cumulative probability with out of range input.
     */
    @Test
    public void testCumulativeProbabilityOutOfRangeInput() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double x = Double.POSITIVE_INFINITY;
        double p = normalDist.cumulativeProbability(x);
        assertEquals(1.0, p, 0.0);

        x = Double.NEGATIVE_INFINITY;
        p = normalDist.cumulativeProbability(x);
        assertEquals(0.0, p, 0.0);
    }

    /**
     * Test inverse cumulative probability with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        for (int i = -5; i <= 5; i++) {
            double p = 0.5 + i / 10.0;
            double x = normalDist.inverseCumulativeProbability(p);
            double expected = normalDist.getMean() + i * normalDist.getStandardDeviation() / 10.0;
            assertEquals(expected, x, 0.0001);
        }
    }

    /**
     * Test inverse cumulative probability with invalid input.
     */
    @Test
    public void testInverseCumulativeProbabilityInvalidInput() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        assertFalse(normalDist.inverseCumulativeProbability(1.1) instanceof Number);
        assertThrows(IllegalArgumentException.class, () -> normalDist.inverseCumulativeProbability(1.2));
        assertThrows(MathException.class, () -> normalDist.inverseCumulativeProbability(0.5 + Math.sqrt(2.0) / 10.0));
    }

    /**
     * Test inverse cumulative probability with statistical checks.
     */
    @Test
    public void testInverseCumulativeProbabilityStatisticalChecks() {
        NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        int n = 100000;
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = normalDist.inverseCumulativeProbability(StatFunctions.uniform_01(normalDist.getRand()));
        }
        double mean = StatFunctions.mean(x);
        double stdev = StatFunctions.stdev(x);
        assertEquals(normalDist.getMean(), mean, 0.001);
        assertEquals(normalDist.getStandardDeviation(), stdev, 0.001);
    }
}