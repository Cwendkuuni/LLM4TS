package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test cases for NormalDistributionImpl.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with mean=0 and standard deviation=1.
     */
    @Test
    public void testNormalDistributionImplWithDefaults() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 1.0e-9);
        assertEquals(1.0, dist.getStandardDeviation(), 1.0e-9);
    }

    /**
     * Test constructor with specified mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithSpecifiedParams() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.2);
        assertEquals(2.5, dist.getMean(), 1.0e-9);
        assertEquals(3.2, dist.getStandardDeviation(), 1.0e-9);
    }

    /**
     * Test mean and standard deviation getters and setters.
     */
    @Test
    public void testGettersAndSetters() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 1.0e-9);
        assertEquals(1.0, dist.getStandardDeviation(), 1.0e-9);

        dist.setMean(3.14);
        dist.setStandardDeviation(2.718);
        assertEquals(3.14, dist.getMean(), 1.0e-9);
        assertEquals(2.718, dist.getStandardDeviation(), 1.0e-9);
    }

    /**
     * Test cumulative probability method with mean=0 and standard deviation=1.
     */
    @Test
    public void testCumulativeProbabilityWithDefaults() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double expected = 0.5 * (1.0 + Erf.erf(x / Math.sqrt(2.0)));
            double calculated = dist.cumulativeProbability(x);
            assertEquals(expected, calculated, 1.0e-6);
        }
    }

    /**
     * Test cumulative probability method with mean=2.5 and standard deviation=3.2.
     */
    @Test
    public void testCumulativeProbabilityWithSpecifiedParams() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.2);
        for (double x = -10.0; x <= 10.0; x += 0.1) {
            double expected = 0.5 * (1.0 + Erf.erf((x - 2.5) / (3.2 * Math.sqrt(2.0))));
            double calculated = dist.cumulativeProbability(x);
            assertEquals(expected, calculated, 1.0e-6);
        }
    }

    /**
     * Test inverse cumulative probability method with mean=0 and standard deviation=1.
     */
    @Test
    public void testInverseCumulativeProbabilityWithDefaults() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = dist.getMean() + p * dist.getStandardDeviation();
            assertEquals(expected, x, 1.0e-6);
        }
    }

    /**
     * Test inverse cumulative probability method with mean=2.5 and standard deviation=3.2.
     */
    @Test
    public void testInverseCumulativeProbabilityWithSpecifiedParams() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 3.2);
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = dist.inverseCumulativeProbability(p);
            double expected = 2.5 + p * 3.2;
            assertEquals(expected, x, 1.0e-6);
        }
    }

    /**
     * Test getDomainLowerBound, getDomainUpperBound, and getInitialDomain methods.
     */
    @Test
    public void testDomainMethods() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double lowerBound = dist.getDomainLowerBound(p);
            double upperBound = dist.getDomainUpperBound(p);
            double initialDomain = dist.getInitialDomain(p);
            assertTrue(lowerBound <= initialDomain);
            assertTrue(initialDomain <= upperBound);
        }
    }

    /**
     * Test generating random numbers using NormalDistributionImpl.
     */
    @Test
    public void testRandomNumberGeneration() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double[] numbers = new double[1000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = dist.inverseCumulativeProbability(StatUtils.uniform(0.0, 1.0));
        }
        double mean = StatUtils.mean(numbers);
        double stdDev = StatUtils.standardDeviation(numbers);
        assertEquals(dist.getMean(), mean, 1.0e-6);
        assertEquals(dist.getStandardDeviation(), stdDev, 1.0e-6);
    }

    /**
     * Test exception handling for invalid standard deviation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }
}