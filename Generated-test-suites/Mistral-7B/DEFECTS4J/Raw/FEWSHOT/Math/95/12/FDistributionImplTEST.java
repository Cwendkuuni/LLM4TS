package org.apache.commons.math.distribution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionInstance() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertTrue(fd instanceof FDistribution);
        assertTrue(fd instanceof FDistributionImpl);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.001);
        assertEquals(0.1, fd.cumulativeProbability(0.5), 0.001);
        assertEquals(1.0, fd.cumulativeProbability(Double.POSITIVE_INFINITY), 0.001);

        FDistribution fd1 = new FDistributionImpl(4.0, 2.0);
        assertEquals(0.0, fd1.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, fd1.cumulativeProbability(1.0), 0.001);
        assertEquals(1.0, fd1.cumulativeProbability(Double.POSITIVE_INFINITY), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.111, fd.inverseCumulativeProbability(0.75), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.001);

        FDistribution fd1 = new FDistributionImpl(4.0, 2.0);
        assertEquals(0.0, fd1.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(0.4, fd1.inverseCumulativeProbability(0.25), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, fd1.inverseCumulativeProbability(1.0), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.2), 0.001);

        FDistribution fd1 = new FDistributionImpl(4.0, 2.0);
        assertEquals(0.0, fd1.getDomainLowerBound(0.5), 0.001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.2), 0.001);

        FDistribution fd1 = new FDistributionImpl(4.0, 2.0);
        assertEquals(Double.MAX_VALUE, fd1.getDomainUpperBound(0.5), 0.001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        assertEquals(4.0 / 2.0, fd.getInitialDomain(0.2), 0.001);

        FDistribution fd1 = new FDistributionImpl(4.0, 2.0);
        assertEquals(2.0 / (2.0 - 2.0), fd1.getInitialDomain(0.5), 0.001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(2.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }
}