package org.apache.commons.math.distribution;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {
    private static final double DELTA = 1e-9;
    private NormalDistributionImpl distribution;

    @Test
    public void testConstructor() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), DELTA);
        assertEquals(1, distribution.getStandardDeviation(), DELTA);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroStandardDeviation() {
        distribution = new NormalDistributionImpl(0, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        distribution = new NormalDistributionImpl(0, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroInverseCumAccuracy() {
        distribution = new NormalDistributionImpl(0, 1, 0);
    }

    @Test
    public void testDensity() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), DELTA);
        assertEquals(0.24197072451914337, distribution.density(1), DELTA);
        assertEquals(0.0044318484119380075, distribution.density(-4), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), DELTA);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(1), DELTA);
        assertEquals(0.02275013194817921, distribution.cumulativeProbability(-2), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(1, distribution.inverseCumulativeProbability(0.8413447460685429), DELTA);
        assertEquals(-2, distribution.inverseCumulativeProbability(0.02275013194817921), DELTA);
    }

    @Test
    public void testSample() throws MathException {
        distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), DELTA);
    }

    @Test
    public void testGetDomainLowerBound() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.2), DELTA);
        assertEquals(0, distribution.getDomainLowerBound(0.5), DELTA);
    }

    @Test
    public void testGetDomainUpperBound() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getDomainUpperBound(0.2), DELTA);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), DELTA);
    }

    @Test
    public void testGetInitialDomain() {
        distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getInitialDomain(0.5), DELTA);
        assertEquals(-1, distribution.getInitialDomain(0.4), DELTA);
        assertEquals(1, distribution.getInitialDomain(0.6), DELTA);
    }
}