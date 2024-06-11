package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {
    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        distribution.setMean(1.0);
        assertEquals(1.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.0, distribution.cumulativeProbability(-Double.MAX_VALUE), 0.0);
            assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0);
            assertEquals(1.0, distribution.cumulativeProbability(Double.MAX_VALUE), 0.0);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0);
            assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0);
            assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.0), 0.0);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0);
        assertEquals(0.0, distribution.getDomainLowerBound(1.0), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(0.0, distribution.getDomainUpperBound(0.0), 0.0);
        assertEquals(0.0, distribution.getDomainUpperBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.0);
        assertEquals(1.0, distribution.getInitialDomain(0.5), 0.0);
        assertEquals(1.0, distribution.getInitialDomain(1.0), 0.0);
    }
}