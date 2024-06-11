package org.apache.commons.cli.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.MathException;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertTrue(dist instanceof NormalDistributionImpl);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testNormalDistributionImplDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.398942280401, dist.density(0), 0.0000001);
        assertEquals(0.154308063482, dist.density(1), 0.0000001);
        assertEquals(0.0, dist.density(-2), 0.0000001);
        assertEquals(0.0, dist.density(3), 0.0000001);
    }

    @Test
    public void testNormalDistributionImplCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), solverAbsoluteAccuracy);
        assertEquals(0.841344746, dist.cumulativeProbability(1), solverAbsoluteAccuracy);
        assertEquals(0.158655254, dist.cumulativeProbability(-1), solverAbsoluteAccuracy);
        assertEquals(0.0, dist.cumulativeProbability(-2), solverAbsoluteAccuracy);
        assertEquals(1.0, dist.cumulativeProbability(2), solverAbsoluteAccuracy);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(-2);
    }

    @Test
    public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(1.0, dist.inverseCumulativeProbability(1), solverAbsoluteAccuracy);
        assertEquals(-1.0, dist.inverseCumulativeProbability(0), solverAbsoluteAccuracy);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.5), solverAbsoluteAccuracy);
        assertEquals(1.64492753685, dist.inverseCumulativeProbability(0.95), solverAbsoluteAccuracy);
        assertEquals(-1.64492753685, dist.inverseCumulativeProbability(0.05), solverAbsoluteAccuracy);
        assertEquals(Double.NEGATIVE_INFINITY, dist.inverseCumulativeProbability(0), solverAbsoluteAccuracy);
        assertEquals(Double.POSITIVE_INFINITY, dist.inverseCumulativeProbability(1), solverAbsoluteAccuracy);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImplInverseCumulativeProbabilityInvalidProbability() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(2);
    }

    @Test
    public void testNormalDistributionImplSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertTrue(dist.sample() > -3 && dist.sample() < 3);
    }

    @Test
    public void testNormalDistributionImplDomainBounds() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0), solverAbsoluteAccuracy);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0), solverAbsoluteAccuracy);
        assertEquals(Double.MAX_VALUE, dist.getDomainLowerBound(0.5), solverAbsoluteAccuracy);
        assertEquals(-Double.MAX_VALUE, dist.getDomainUpperBound(0.5), solverAbsoluteAccuracy);
        assertEquals(0.0, dist.getDomainLowerBound(0.5), solverAbsoluteAccuracy);
        assertEquals(0.0, dist.getDomainUpperBound(0.5), solverAbsoluteAccuracy);
    }

    @Test
    public void testNormalDistributionImplInitialDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(-1.0, dist.getInitialDomain(0), solverAbsoluteAccuracy);
        assertEquals(1.0, dist.getInitialDomain(0.5), solverAbsoluteAccuracy);
        assertEquals(0.0, dist.getInitialDomain(0.5), solverAbsoluteAccuracy);
    }
}