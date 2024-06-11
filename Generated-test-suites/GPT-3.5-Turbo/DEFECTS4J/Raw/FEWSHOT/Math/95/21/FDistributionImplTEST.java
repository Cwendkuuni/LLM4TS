package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(10.0, 20.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(10.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        fDistribution.setNumeratorDegreesOfFreedom(15.0);
        assertEquals(15.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalid() {
        fDistribution.setNumeratorDegreesOfFreedom(-5.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(20.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        fDistribution.setDenominatorDegreesOfFreedom(25.0);
        assertEquals(25.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalid() {
        fDistribution.setDenominatorDegreesOfFreedom(-5.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.cumulativeProbability(-5.0), 0.0);
        assertEquals(0.8774332228355244, fDistribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(1.0, fDistribution.cumulativeProbability(1000.0), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0);
        assertTrue(Double.isNaN(fDistribution.inverseCumulativeProbability(-0.5)));
        assertTrue(Double.isNaN(fDistribution.inverseCumulativeProbability(1.5)));
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.2), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.2), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(1.3333333333333333, fDistribution.getInitialDomain(0.2), 0.0001);
    }
}