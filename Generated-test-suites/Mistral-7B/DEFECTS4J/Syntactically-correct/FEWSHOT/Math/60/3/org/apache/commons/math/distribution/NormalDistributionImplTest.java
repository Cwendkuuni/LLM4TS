package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals(0.398942280401, density, 0.00001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double cdf = dist.cumulativeProbability(0);
        assertEquals(0.5, cdf, 0.00001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = -1;
        dist.cumulativeProbability(p);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double x = dist.inverseCumulativeProbability(p);
        assertEquals(0, x, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = -1;
        dist.inverseCumulativeProbability(p);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertEquals(0, sample, 0.01);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1;
        double lowerBound = dist.getDomainLowerBound(p);
        assertEquals(-3.036, lowerBound, 0.01);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1;
        double upperBound = dist.getDomainUpperBound(p);
        assertEquals(2.036, upperBound, 0.01);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.1;
        double initialDomain = dist.getInitialDomain(p);
        assertEquals(-1.036, initialDomain, 0.01);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double solverAbsoluteAccuracy = dist.getSolverAbsoluteAccuracy();
        assertEquals(1e-9, solverAbsoluteAccuracy, 1e-10);
    }
}