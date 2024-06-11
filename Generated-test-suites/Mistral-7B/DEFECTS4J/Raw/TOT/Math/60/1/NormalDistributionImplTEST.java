package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 * Tests for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid argument (negative standard deviation).
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeStandardDeviation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    /**
     * Test {@link #density(double)} method with valid arguments.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.398942280401, dist.density(0.0), 5e-6);
        assertEquals(-0.398942280401, dist.density(-0.0), 5e-6);
        assertEquals(0.241970599236, dist.density(1.0), 5e-6);
        assertEquals(-0.241970599236, dist.density(-1.0), 5e-6);
    }

    /**
     * Test {@link #density(double)} method with extreme values.
     */
    @Test
    public void testDensityExtremeValues() {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double smallNegative = -1e9;
        double largeNegative = -1e5;
        double largePositive = 1e5;
        double large = 1e9;
        assertEquals(0.0, dist.density(smallNegative), 5e-6);
        assertEquals(1.0 / (FastMath.SQRT2_PI * 1e19), dist.density(largeNegative), 5e-6);
        assertEquals(1.0 / (FastMath.SQRT2_PI * 1e19), dist.density(largePositive), 5e-6);
        assertEquals(0.0, dist.density(large), 5e-6);
    }

    /**
     * Test {@link #cumulativeProbability(double)} method with valid arguments.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double[] testValues = {-3.0, -1.5, -0.5, 0.0, 0.5, 1.5, 3.0};
        for (double value : testValues) {
            double p = dist.cumulativeProbability(value);
            double expected = Erf.erf(value / 1.0) / 2.0 + 0.5;
            assertEquals(expected, p, 1e-8);
        }
    }

    /**
     * Test {@link #cumulativeProbability(double)} method with extreme values.
     */
    @Test
    public void testCumulativeProbabilityExtremeValues() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double smallNegative = -1e9;
        double largeNegative = -1e5;
        double largePositive = 1e5;
        double large = 1e9;
        assertEquals(0.0, dist.cumulativeProbability(smallNegative), 5e-6);
        assertEquals(0.5, dist.cumulativeProbability(largeNegative), 5e-6);
        assertEquals(0.5, dist.cumulativeProbability(largePositive), 5e-6);
        assertEquals(1.0, dist.cumulativeProbability(large), 5e-6);
    }

    /**
     * Test {@link #sample()} method.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double[] samples = new double[100];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = dist.sample();
        }
        Mean mean = new Mean();
        Mean standardDeviation = new Mean();
        for (double sample : samples) {
            mean.addValue(sample);
            standardDeviation.addValue(Math.pow(sample, 2.0));
        }
        assertEquals(0.0, mean.getValue(), 5e-6);
        assertEquals(1.0, standardDeviation.getValue(), 5e-6);
    }

    /**
     * Test inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double[] testValues = {-1.0, 0.0, 0.1, 0.5, 1.0};
        double[] expectedValues = {-1.25992104510252, 0.0, 0.12702753832494, 0.5, 1.25992104510252};
        for (int i = 0; i < testValues.length; i++) {
            double inverse = dist.inverseCumulativeProbability(testValues[i]);
            assertEquals(expectedValues[i], inverse, 5e-6);
        }
    }

    /**
     * Test inverseCumulativeProbability method with extreme values.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityExtremeValues() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        double[] testValues = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for (double value : testValues) {
            dist.inverseCumulativeProbability(value);
        }
    }
}