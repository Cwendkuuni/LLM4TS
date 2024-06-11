package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        
        // test value for x <= 0
        assertEquals(0.0, fd.cumulativeProbability(-1), 0.00001);
        assertEquals(0.0, fd.cumulativeProbability(0), 0.00001);
        
        // test value for x > 0
        double p = Beta.regularizedBeta((10 * 1) / (20 + 10 * 1), 0.5 * 10, 0.5 * 20);
        assertEquals(p, fd.cumulativeProbability(1), 0.00001);
        
        assertTrue(fd.cumulativeProbability(100) < 1);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        
        // test value for p = 0
        assertEquals(0.0, fd.inverseCumulativeProbability(0), 0.00001);
        
        // test value for p = 1
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1), 0.00001);
        
        // test random value of p
        double x = fd.inverseCumulativeProbability(0.5);
        assertTrue(fd.cumulativeProbability(x) > 0.5);
        assertTrue(fd.cumulativeProbability(x - 1) < 0.5);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        assertEquals(0.0, fd.getDomainLowerBound(0.5), 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.5), 0.00001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        double d = 20 / (20 - 2.0);
        assertEquals(d, fd.getInitialDomain(0.5), 0.00001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        fd.setNumeratorDegreesOfFreedom(15);
        assertEquals(15, fd.getNumeratorDegreesOfFreedom(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegative() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        fd.setNumeratorDegreesOfFreedom(-15);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        assertEquals(10, fd.getNumeratorDegreesOfFreedom(), 0.00001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        fd.setDenominatorDegreesOfFreedom(25);
        assertEquals(25, fd.getDenominatorDegreesOfFreedom(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegative() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        fd.setDenominatorDegreesOfFreedom(-25);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fd = new FDistributionImpl(10, 20);
        assertEquals(20, fd.getDenominatorDegreesOfFreedom(), 0.00001);
    }
}