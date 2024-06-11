package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_sd1() {
        NormalDistributionImpl dist = new NormalDistributionImpl();
        assertEquals(0.0, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test
    public void testNormalDistributionImpl_meanNonZero_sd1() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2.5, 1.0);
        assertEquals(2.5, dist.getMean(), 0.001);
        assertEquals(1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        dist.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, dist.cumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.cumulativeProbability(1.0), 0.001);
        assertEquals(0.158655254, dist.cumulativeProbability(-1.0), 0.001);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(3.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.841344746, dist.inverseCumulativeProbability(0.841344746), 0.001);
        assertEquals(-1.282127639, dist.inverseCumulativeProbability(0.158655254), 0.001);
        assertThrows(MathException.class, () -> dist.inverseCumulativeProbability(1.5));
        assertThrows(IllegalArgumentException.class, () -> dist.inverseCumulativeProbability(-0.1));
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.0), 0.0);
        assertEquals(0.0, dist.getDomainLowerBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, dist.getDomainLowerBound(1.0), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.0), 0.0);
        assertEquals(0.0, dist.getDomainUpperBound(0.5), 0.0);
        assertEquals(-Double.MAX_VALUE, dist.getDomainUpperBound(1.0), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, dist.getInitialDomain(0.1), 0.0);
        assertEquals(0.0, dist.getInitialDomain(0.5), 0.0);
        assertEquals(1.0, dist.getInitialDomain(0.9), 0.0);
    }
}