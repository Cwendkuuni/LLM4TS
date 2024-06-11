package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;

import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Test class for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @org.junit.Test
    public void testValidConstruction() {
        final double mean = 0.5;
        final double stdDev = 1.0;
        final NormalDistribution dist = new NormalDistributionImpl(mean, stdDev);
        assertEquals("Expected mean", mean, dist.getMean(), 1e-14);
        assertEquals("Expected standard deviation", stdDev, dist.getStandardDeviation(), 1e-14);
    }

    /**
     * Test constructor with non-positive standard deviation.
     */
    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        final double mean = 0.0;
        final double stdDev = -1.0;
        new NormalDistributionImpl(mean, stdDev);
    }

    /**
     * Test density method.
     */
    @org.junit.Test
    public void testDensity() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -10; x <= 10; x += 0.1) {
            final double density = dist.density(x);
            final double expected = FastMath.exp(-0.5 * Math.pow(x, 2)) / (1.0 * FastMath.sqrt(2.0 * FastMath.PI));
            assertEquals("Expected density " + expected + " at x " + x, expected, density, 1e-6);
        }
    }

    /**
     * Test cumulative probability method.
     */
    @org.junit.Test
    public void testCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double x = -10; x <= 10; x += 0.1) {
            final double p = dist.cumulativeProbability(x);
            final double expected = 0.5 + 0.5 * Erf.erf(x / FastMath.sqrt(2.0));
            assertEquals("Expected cumulative probability " + expected + " at x " + x, expected, p, 1e-6);
        }
    }

    /**
     * Test inverse cumulative probability method with valid arguments.
     */
    @org.junit.Test
    public void testValidInverseCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            final double x = dist.inverseCumulativeProbability(p);
            final double expected = FastMath.sqrt(2.0) * Erf.erfInverse(2 * p - 1);
            assertEquals("Expected inverse cumulative probability " + expected + " at p " + p, expected, x, 1e-6);
        }
    }

    /**
     * Test inverse cumulative probability method with p < 0.
     */
    @org.junit.Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test inverse cumulative probability method with p > 1.
     */
    @org.junit.Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability2() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test sample method.
     */
    @org.junit.Test
    public void testSample() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            final double sample = dist.sample();
            assertTrue("Expected sample to be in range [-3.5, 3.5]", Precision.isClose(sample, 0.0, 3.5));
        }
    }
}