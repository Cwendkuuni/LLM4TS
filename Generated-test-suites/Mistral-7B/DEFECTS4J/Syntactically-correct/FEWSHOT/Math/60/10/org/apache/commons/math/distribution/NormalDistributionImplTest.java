package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.distribution.NormalDistribution;
import org.apache.commons.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_meanPositiveStandardDeviationPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(0.13595702, distribution.density(0.0), 0.0001);
        assertEquals(0.84134474, distribution.density(2.0), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.975, distribution.cumulativeProbability(1.5), 0.001);
        assertFalse(distribution.cumulativeProbability(3.0) > 0.5);
        assertEquals(-1.5, distribution.inverseCumulativeProbability(0.1587), 0.01);
        assertEquals(1.5, distribution.inverseCumulativeProbability(0.95), 0.01);

        try {
            distribution = new NormalDistributionImpl(-1.0, 2.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            distribution = new NormalDistributionImpl(1.0, -2.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testNormalDistributionImpl_meanZeroStandardDeviationOne() {
        NormalDistribution distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(0.39894228, distribution.density(0.0), 0.0001);
        assertEquals(0.39894228, distribution.density(0), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(1.0), 0.001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(1), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_meanPositiveStandardDeviationZero() {
        NormalDistribution distribution = new NormalDistributionImpl(2.0, 0.0);
        assertEquals(2.0, distribution.getMean(), 0.0);
        assertEquals(0.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(Double.NaN, distribution.density(0.0), 0.0);
        assertEquals(Double.NaN, distribution.density(2.0), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(2.0), 0.001);
        assertFalse(distribution.cumulativeProbability(1.0) > 0.5);
        assertEquals(Double.NaN, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.NaN, distribution.inverseCumulativeProbability(1.0), 0.0);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(0.5));
    }

    @Test
    public void testNormalDistributionImpl_meanZeroStandardDeviationZero() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 0.0);
        assertEquals(0.0, distribution.getMean(), 0.0);
        assertEquals(0.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(1.0, distribution.density(0.0), 0.0001);
        assertEquals(1.0, distribution.density(0), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(1), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(1.0), 0.001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(1), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test
    public void testNormalDistributionImpl_meanNegativeStandardDeviationPositive() {
        NormalDistribution distribution = new NormalDistributionImpl(-1.0, 2.0);
        assertEquals(-1.0, distribution.getMean(), 0.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(-0.13595702, distribution.density(-2.0), 0.0001);
        assertEquals(0.13595702, distribution.density(0.0), 0.0001);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.12716981, distribution.cumulativeProbability(-1.0), 0.001);
        assertFalse(distribution.cumulativeProbability(1.0) > 0.5);
        assertEquals(-1.2821325, distribution.inverseCumulativeProbability(0.1), 0.01);
        assertEquals(1.2821325, distribution.inverseCumulativeProbability(0.9), 0.01);
    }

    @Test
    public void testNormalDistributionImpl_meanNegativeStandardDeviationZero() {
        NormalDistribution distribution = new NormalDistributionImpl(-2.0, 0.0);
        assertEquals(-2.0, distribution.getMean(), 0.0);
        assertEquals(0.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(Double.NaN, distribution.density(-2.0), 0.0);
        assertEquals(Double.NaN, distribution.density(0.0), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(-2.0), 0.001);
        assertEquals(Double.NaN, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.NaN, distribution.inverseCumulativeProbability(1.0), 0.0);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(0.5));
    }

    @Test
    public void testNormalDistributionImpl_meanNegativeStandardDeviationNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(-1.0, -2.0);
        assertEquals(-1.0, distribution.getMean(), 0.0);
        assertEquals(-2.0, distribution.getStandardDeviation(), 0.0);
        assertEquals(Double.NaN, distribution.density(-2.0), 0.0);
        assertEquals(Double.NaN, distribution.density(-4.0), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.0, distribution.cumulativeProbability(-3.0), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(-5.0), 0.001);
        assertEquals(-1.2821325, distribution.inverseCumulativeProbability(0.1), 0.01);
        assertEquals(-2.2821325, distribution.inverseCumulativeProbability(0.9), 0.01);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(0.5));
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(1.0, 2.0);
        double sample1 = distribution.sample();
        double sample2 = distribution.sample();
        double sample3 = distribution.sample();

        double[] samples = {sample1, sample2, sample3};
        assertArrayEquals(new double[]{1.39223114, 0.19443717, 1.99755361}, samples, 0.01);
    }
}