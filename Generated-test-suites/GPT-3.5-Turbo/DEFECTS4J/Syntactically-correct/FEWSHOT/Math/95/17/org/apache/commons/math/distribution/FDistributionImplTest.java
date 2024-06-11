package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(0.5, fDistribution.cumulativeProbability(1.0), 0.0001);

        fDistribution = new FDistributionImpl(3, 1);
        assertEquals(0.5, fDistribution.cumulativeProbability(1.0), 0.0001);

        fDistribution = new FDistributionImpl(5, 10);
        assertEquals(0.399994, fDistribution.cumulativeProbability(1.0), 0.0001);

        fDistribution = new FDistributionImpl(10, 5);
        assertEquals(0.899994, fDistribution.cumulativeProbability(1.0), 0.0001);

        fDistribution = new FDistributionImpl(10, 10);
        assertEquals(0.499994, fDistribution.cumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability_zeroP() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability_oneP() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(1.0, fDistribution.inverseCumulativeProbability(0.5), 0.0001);

        fDistribution = new FDistributionImpl(3, 1);
        assertEquals(0.63847, fDistribution.inverseCumulativeProbability(0.5), 0.0001);

        fDistribution = new FDistributionImpl(5, 10);
        assertEquals(1.34344, fDistribution.inverseCumulativeProbability(0.4), 0.0001);

        fDistribution = new FDistributionImpl(10, 5);
        assertEquals(2.77771, fDistribution.inverseCumulativeProbability(0.9), 0.0001);

        fDistribution = new FDistributionImpl(10, 10);
        assertEquals(1.82757, fDistribution.inverseCumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        assertEquals(1.0, fDistribution.getInitialDomain(0.5), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negative() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        fDistribution.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        fDistribution.setNumeratorDegreesOfFreedom(3);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_negative() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        fDistribution.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(1, 1);
        fDistribution.setDenominatorDegreesOfFreedom(3);
        assertEquals(3.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}