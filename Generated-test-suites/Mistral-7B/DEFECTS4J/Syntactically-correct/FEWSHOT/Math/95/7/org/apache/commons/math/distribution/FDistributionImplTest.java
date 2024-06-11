package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setNumeratorDegreesOfFreedom(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        fDist.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);

        double p = 0.0;
        assertEquals(0.0, fDist.cumulativeProbability(p), 1e-14);

        p = 0.1;
        assertEquals(0.12795459746846251, fDist.cumulativeProbability(p), 1e-14);

        p = 1.0;
        assertEquals(1.0, fDist.cumulativeProbability(p), 1e-14);

        p = 2.0;
        assertEquals(1.0, fDist.cumulativeProbability(p), 1e-14);

        fDist = new FDistributionImpl(2.0, 3.0);
        p = 0.5;
        assertEquals(0.5, fDist.cumulativeProbability(p), 1e-14);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);

        double p = 0.0;
        assertEquals(0.0, fDist.inverseCumulativeProbability(p), 1e-14);

        p = 0.1;
        assertEquals(1.242548225623932, fDist.inverseCumulativeProbability(p), 1e-14);

        p = 0.5;
        assertEquals(1.4142135623730955, fDist.inverseCumulativeProbability(p), 1e-14);

        p = 1.0;
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(p), 1e-14);

        fDist = new FDistributionImpl(2.0, 3.0);
        p = 0.25;
        assertEquals(1.3523882308139525, fDist.inverseCumulativeProbability(p), 1e-14);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, fDist.getDomainLowerBound(0.1), 1e-14);
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 1e-14);
        assertEquals(0.0, fDist.getDomainLowerBound(1.0), 1e-14);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.1), 1e-14);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 1e-14);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(1.0), 1e-14);
    }

    @Test
    public void testGetInitialDomain() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 2.0);
        assertEquals(2.0 / (2.0 - 2.0), fDist.getInitialDomain(0.1), 1e-14);
        assertEquals(2.0 / (2.0 - 2.0), fDist.getInitialDomain(0.5), 1e-14);
        assertEquals(2.0 / (2.0 - 2.0), fDist.getInitialDomain(1.0), 1e-14);

        fDist = new FDistributionImpl(3.0, 4.0);
        assertEquals(4.0 / (4.0 - 2.0), fDist.getInitialDomain(0.1), 1e-14);
        assertEquals(4.0 / (4.0 - 2.0), fDist.getInitialDomain(0.5), 1e-14);
        assertEquals(4.0 / (4.0 - 2.0), fDist.getInitialDomain(1.0), 1e-14);
    }
}