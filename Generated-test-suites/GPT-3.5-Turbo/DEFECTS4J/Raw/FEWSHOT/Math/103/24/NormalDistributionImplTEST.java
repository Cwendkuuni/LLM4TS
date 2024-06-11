package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0001);
        assertEquals(0.8413, distribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);

        assertThrows(MathException.class, () -> {
            distribution.inverseCumulativeProbability(-0.1);
        });

        assertThrows(MathException.class, () -> {
            distribution.inverseCumulativeProbability(1.1);
        });

    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.1), 0.0001);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, distribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.9), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, distribution.getInitialDomain(0.1), 0.0001);
        assertEquals(1.0, distribution.getInitialDomain(0.9), 0.0001);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0001);
    }
}