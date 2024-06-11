package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fd;

    @Before
    public void setUp() {
        fd = new FDistributionImpl(3.0, 4.0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.1608, fd.cumulativeProbability(0.5), 0.0001);
        assertEquals(0.56, fd.cumulativeProbability(1.0), 0.01);
        assertEquals(0.7916, fd.cumulativeProbability(1.5), 0.0001);
        assertEquals(1.0, fd.cumulativeProbability(100.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(0.7413, fd.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(3.8462, fd.inverseCumulativeProbability(0.9), 0.0001);
        assertEquals(8.2079, fd.inverseCumulativeProbability(0.99), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fd.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.5), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(0.9), 0.0001);
        assertEquals(0.0, fd.getDomainLowerBound(1.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.9), 0.0001);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(1.0), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(8.0, fd.getInitialDomain(0.0), 0.0001);
        assertEquals(8.0, fd.getInitialDomain(0.5), 0.0001);
        assertEquals(8.0, fd.getInitialDomain(0.9), 0.0001);
        assertEquals(8.0, fd.getInitialDomain(1.0), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_WithZeroDegreesOfFreedom_ShouldThrowException() {
        fd.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_WithNegativeDegreesOfFreedom_ShouldThrowException() {
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_WithZeroDegreesOfFreedom_ShouldThrowException() {
        fd.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_WithNegativeDegreesOfFreedom_ShouldThrowException() {
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}