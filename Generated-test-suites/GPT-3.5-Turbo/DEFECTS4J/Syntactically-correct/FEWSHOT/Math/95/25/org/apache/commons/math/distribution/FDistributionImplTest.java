package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(0.8500339309226595, fDistribution.cumulativeProbability(2.5), 0.0001);
        assertEquals(0.617023435606414, fDistribution.cumulativeProbability(1.7), 0.0001);
        assertEquals(0.01534667439780262, fDistribution.cumulativeProbability(0.4), 0.0001);
        assertEquals(0.0017404380319330017, fDistribution.cumulativeProbability(0.1), 0.0001);
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(1.1319690135843926, fDistribution.inverseCumulativeProbability(0.3), 0.0001);
        assertEquals(1.8331239353195376, fDistribution.inverseCumulativeProbability(0.6), 0.0001);
        assertEquals(3.596984793409697, fDistribution.inverseCumulativeProbability(0.9), 0.0001);
        assertEquals(6.944271909999158, fDistribution.inverseCumulativeProbability(0.99), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.3), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.6), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.9), 0.0001);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.99), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.3), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.6), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.9), 0.0001);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.99), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(1.3333333333333333, fDistribution.getInitialDomain(0.3), 0.0001);
        assertEquals(1.3333333333333333, fDistribution.getInitialDomain(0.6), 0.0001);
        assertEquals(1.3333333333333333, fDistribution.getInitialDomain(0.9), 0.0001);
        assertEquals(1.3333333333333333, fDistribution.getInitialDomain(0.99), 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        fDistribution.setNumeratorDegreesOfFreedom(5);
        assertEquals(5.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(3.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        fDistribution.setDenominatorDegreesOfFreedom(6);
        assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(3, 4);
        assertEquals(4.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }
}