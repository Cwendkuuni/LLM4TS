package org.apache.commons.math.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(0.17603266338214976, distribution.density(9), 0);
        assertEquals(0.05399096651318806, distribution.density(12), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(0.02275013194817921, distribution.cumulativeProbability(8), 0);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(12), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(7.289707253902945, distribution.inverseCumulativeProbability(0.1), 0);
        assertEquals(15.289707253902945, distribution.inverseCumulativeProbability(0.9), 0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        double sample = distribution.sample();
        assertTrue(sample >= 4);
        assertTrue(sample <= 16);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.2), 0);
        assertEquals(10, distribution.getDomainLowerBound(0.8), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getDomainUpperBound(0.2), 0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.8), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(8, distribution.getInitialDomain(0.2), 0);
        assertEquals(12, distribution.getInitialDomain(0.8), 0);
        assertEquals(10, distribution.getInitialDomain(0.5), 0);
    }
}