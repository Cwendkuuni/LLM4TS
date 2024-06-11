package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getMean(), 0.001);
        assertEquals(2.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double p = 0.5;
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(3.0), 0.001);
        assertEquals(0.6826895, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.1586553, dist.cumulativeProbability(-1.0), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.cumulativeProbability(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double p = 0.1587;
        assertEquals(-1.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), 0.001);
        assertEquals(1.0, dist.inverseCumulativeProbability(1.0), 0.001);
        assertEquals(1.1280437, dist.inverseCumulativeProbability(p), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        dist.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.0), 0.0);
        assertEquals(0.0, dist.getDomainLowerBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, dist.getDomainLowerBound(1.0), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(-Double.MAX_VALUE, dist.getDomainUpperBound(0.0), 0.0);
        assertEquals(0.0, dist.getDomainUpperBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(1.0), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        double p = 0.1587;
        assertEquals(-1.0, dist.getInitialDomain(0.0), 0.0);
        assertEquals(0.0, dist.getInitialDomain(0.5), 0.0);
        assertEquals(1.0, dist.getInitialDomain(1.0), 0.0);
        assertEquals(-0.8416235, dist.getInitialDomain(p), 0.01);
    }

}