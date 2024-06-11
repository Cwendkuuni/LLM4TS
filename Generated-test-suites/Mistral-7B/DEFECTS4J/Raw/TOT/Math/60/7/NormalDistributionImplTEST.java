package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

/**
 * Tests for the {@link NormalDistributionImpl} class.
 */
public class NormalDistributionImplTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        NormalDistribution dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(sd, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with invalid argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSD() {
        double mean = 1.0;
        double sd = -1.0;
        new NormalDistributionImpl(mean, sd);
    }

    /**
     * Test inverseCumulativeProbability with valid values.
     */
    @Test
    public void testInverseCumulativeProbability() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double p = 0.5;
        double value;

        value = dist.inverseCumulativeProbability(p);
        assertTrue(Precision.isClose(mean, value, 1e-4));

        value = dist.inverseCumulativeProbability(0.0);
        assertTrue(Double.isNegativeInfinity(value));

        value = dist.inverseCumulativeProbability(1.0);
        assertTrue(Double.isPositiveInfinity(value));

        p = 0.1;
        value = dist.inverseCumulativeProbability(p);
        assertTrue(Precision.isClose(mean - 1.644853627e-1, value, 1e-4));

        p = 0.999;
        value = dist.inverseCumulativeProbability(p);
        assertTrue(Precision.isClose(mean + 3.091532511e2, value, 1e-4));
    }

    /**
     * Test inverseCumulativeProbability with extreme values.
     */
    @Test
    public void testExtremeInverseCumulativeProbability() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        double p = 0.0;
        double value = dist.inverseCumulativeProbability(p);
        assertFalse(Double.isFinite(value));
        assertEquals(-Double.MAX_VALUE, value, 0.0);

        p = 1.0;
        value = dist.inverseCumulativeProbability(p);
        assertFalse(Double.isFinite(value));
        assertEquals(Double.MAX_VALUE, value, 0.0);
    }

    /**
     * Test density with valid values.
     */
    @Test
    public void testDensity() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double x;
        double density;

        x = 0.5;
        density = dist.density(x);
        assertEquals(0.1543080634815276, density, 1e-6);

        x = mean;
        density = dist.density(x);
        assertEquals(0.3989422804014327, density, 1e-6);

        x = mean + sd;
        density = dist.density(x);
        assertEquals(0.1082592763658418, density, 1e-6);

        x = mean - sd;
        density = dist.density(x);
        assertEquals(0.1082592763658418, density, 1e-6);

        x = 10.0 * sd;
        density = dist.density(x);
        assertEquals(1.254926329829326e-10, density, 1e-10);
    }

    /**
     * Test density with extreme values.
     */
    @Test
    public void testExtremeDensity() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        double x = Double.NEGATIVE_INFINITY;
        double density = dist.density(x);
        assertEquals(0.0, density, 1e-14);

        x = Double.POSITIVE_INFINITY;
        density = dist.density(x);
        assertEquals(0.0, density, 1e-14);
    }

    /**
     * Test cumulativeProbability with valid values.
     */
    @Test
    public void testCumulativeProbability() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double x;
        double p;

        x = -10.0;
        p = dist.cumulativeProbability(x);
        assertEquals(0.0, p, 1e-12);

        x = mean - sd;
        p = dist.cumulativeProbability(x);
        assertEquals(0.15865525, p, 1e-6);

        x = mean;
        p = dist.cumulativeProbability(x);
        assertEquals(0.5, p, 1e-6);

        x = mean + sd;
        p = dist.cumulativeProbability(x);
        assertEquals(0.84134475, p, 1e-6);

        x = 10.0;
        p = dist.cumulativeProbability(x);
        assertEquals(1.0, p, 1e-12);
    }

    /**
     * Test cumulativeProbability with extreme values.
     */
    @Test
    public void testExtremeCumulativeProbability() {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);

        double x = Double.NEGATIVE_INFINITY;
        double p = dist.cumulativeProbability(x);
        assertEquals(0.0, p, 1e-12);

        x = Double.POSITIVE_INFINITY;
        p = dist.cumulativeProbability(x);
        assertEquals(1.0, p, 1e-12);
    }

    /**
     * Test sample method with valid distribution.
     */
    @Test
    public void testSampleMethod() throws MathException {
        double mean = 1.0;
        double sd = 2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double sample;
        double expected;

        for (int i = 0; i < 100; i++) {
            sample = dist.sample();
            expected = mean + sd * RandomData.nextGaussian();
            assertTrue(Precision.isClose(expected, sample, 1e-6));
        }
    }

    /**
     * Test sample method with extreme values.
     */
    @Test
    public void testSampleMethodExtremeValues() throws MathException {
        double mean = 1.0;
        double sd = 1000.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        double sample;
        double expected;

        for (int i = 0; i < 100; i++) {
            sample = dist.sample();
            expected = mean + sd * RandomData.nextGaussian();
            assertTrue(Precision.isClose(expected, sample, 1e-4));
        }
    }

    /**
     * Test sample method with negative standard deviation.
     */
    @Test(expected = MathException.class)
    public void testSampleMethodNegativeSD() throws MathException {
        double mean = 1.0;
        double sd = -2.0;
        NormalDistribution dist = new NormalDistributionImpl(mean, sd);
        dist.sample();
    }
}