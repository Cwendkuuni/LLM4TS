package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testConstructionWithMeanAndStddev() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.5, 2.3);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertEquals(2.3, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testConstructionWithDefaultArguments() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testMeanAndStandardDeviationAccessors() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.5, 2.3);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertEquals(2.3, dist.getStandardDeviation(), 0.001);

        dist.setMean(2.1);
        assertEquals(2.1, dist.getMean(), 0.001);

        dist.setStandardDeviation(0.5);
        assertEquals(0.5, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, -1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test with valid input inside the distribution
        double p = 0.5;
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);

        // Test with valid input outside the distribution
        p = 0.95;
        assertEquals(0.841344746, dist.cumulativeProbability(1.644853627), 0.001);

        // Test with very large input
        p = 0.999;
        assertEquals(0.977236495, dist.cumulativeProbability(3.0), 0.001);

        // Test with very small input
        p = 0.01;
        assertEquals(0.022755367, dist.cumulativeProbability(-1.282), 0.001);

        // Test with input close to mean
        p = 0.51;
        assertEquals(0.51, dist.cumulativeProbability(0.0), 0.001);

        // Test with input far from mean
        p = 0.99;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(2.326347843, x, 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutsideDomain() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(10.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideDomain() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.inverseCumulativeProbability(0.0);
        dist.inverseCumulativeProbability(1.1);
        dist.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test with probability close to 0
        double p = 0.01;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(-1.282, x, 0.001);

        // Test with probability close to 1
        p = 0.99;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(2.326347843, x, 0.001);

        // Test with probability close to 0.5
        p = 0.51;
        x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.001);
    }

    @Test
    public void testCreateInstanceWithInvalidMean() {
        try {
            new NormalDistributionImpl(-1.0, 1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateInstanceWithInvalidStandardDeviation() {
        try {
            new NormalDistributionImpl(1.0, -1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetMeanAndStandardDeviationFromDistribution() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.5, 2.3);
        assertEquals(1.5, dist.getMean(), 0.001);
        assertEquals(2.3, dist.getStandardDeviation(), 0.001);

        dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testStandardDeviationCalculatedWithSample() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double[] values = new double[10000];
        for (int i = 0; i < values.length; i++) {
            values[i] = dist.inverseCumulativeProbability(Math.random());
        }
        StandardDeviation sd = new StandardDeviation();
        double stdDev = sd.evaluate(values);
        assertEquals(1.0, stdDev, 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testCumulativeProbabilityThrowsMaxIterationsExceededException()
            throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.cumulativeProbability(1000000.0);
    }
}