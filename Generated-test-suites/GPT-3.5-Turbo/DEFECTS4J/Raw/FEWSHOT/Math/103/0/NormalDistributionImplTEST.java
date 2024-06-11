package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setMean(1);
        assertEquals(1, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setStandardDeviation(0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setStandardDeviation(2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.6), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0.4), 0);
        assertEquals(1, distribution.getInitialDomain(0.6), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
    }
}