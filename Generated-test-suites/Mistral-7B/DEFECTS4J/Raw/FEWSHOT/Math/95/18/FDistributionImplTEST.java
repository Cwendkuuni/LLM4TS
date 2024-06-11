package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImplNegativeDegreesOfFreedom() {
        new FDistributionImpl(-2.0, 3.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        assertEquals(0.36787944117144233, distribution.cumulativeProbability(p), 0.001);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testCumulativeProbabilityInfinity() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        assertEquals(1.254361429, distribution.inverseCumulativeProbability(p), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.inverseCumulativeProbability(1.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(3.0 / 4.0, distribution.getInitialDomain(0.5), 0.001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}