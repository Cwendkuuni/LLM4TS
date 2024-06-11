package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.001);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.001);

        NormalDistribution distributionWithMeanAndStdDev = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(3.0, distributionWithMeanAndStdDev.getMean(), 0.001);
        assertEquals(2.0, distributionWithMeanAndStdDev.getStandardDeviation(), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841345, distribution.cumulativeProbability(1.0), 0.001);
        assertEquals(0.158655, distribution.cumulativeProbability(-1.0), 0.001);

        NormalDistribution distributionWithMeanAndStdDev = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(0.158730, distributionWithMeanAndStdDev.cumulativeProbability(1.0), 0.001);
        assertEquals(0.997344, distributionWithMeanAndStdDev.cumulativeProbability(5.0), 0.001);
        assertEquals(0.0, distributionWithMeanAndStdDev.cumulativeProbability(-5.0), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        assertEquals(-1.282125, distribution.inverseCumulativeProbability(0.158655), 0.001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.282125, distribution.inverseCumulativeProbability(0.841345), 0.001);

        NormalDistribution distributionWithMeanAndStdDev = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(4.25553, distributionWithMeanAndStdDev.inverseCumulativeProbability(0.6827), 0.001);
        assertEquals(3.0, distributionWithMeanAndStdDev.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(5.74447, distributionWithMeanAndStdDev.inverseCumulativeProbability(0.95), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        double lowerBound = distribution.getDomainLowerBound(0.1);
        assertTrue(Double.isNegative(lowerBound));

        lowerBound = distribution.getDomainLowerBound(0.9);
        assertEquals(0.0, lowerBound, 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        double upperBound = distribution.getDomainUpperBound(0.1);
        assertEquals(0.0, upperBound, 0.001);

        upperBound = distribution.getDomainUpperBound(0.9);
        assertTrue(Double.isPositive(upperBound));

        upperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(0.0, upperBound, 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

        double initialDomain = distribution.getInitialDomain(0.1);
        assertTrue(initialDomain < 0.0);

        initialDomain = distribution.getInitialDomain(0.5);
        assertEquals(0.0, initialDomain, 0.001);

        initialDomain = distribution.getInitialDomain(0.9);
        assertTrue(initialDomain > 0.0);
    }
}