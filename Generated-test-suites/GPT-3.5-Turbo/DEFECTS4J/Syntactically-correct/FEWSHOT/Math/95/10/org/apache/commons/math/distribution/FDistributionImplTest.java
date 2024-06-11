package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3, 4);
    }

    @Test
    public void testNumeratorDegreesOfFreedom() {
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0);
        fDistribution.setNumeratorDegreesOfFreedom(5);
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumeratorDegreesOfFreedomNegative() {
        fDistribution.setNumeratorDegreesOfFreedom(-5);
    }

    @Test
    public void testDenominatorDegreesOfFreedom() {
        assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 0);
        fDistribution.setDenominatorDegreesOfFreedom(6);
        assertEquals(6, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDenominatorDegreesOfFreedomNegative() {
        fDistribution.setDenominatorDegreesOfFreedom(-6);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.456692913, fDistribution.cumulativeProbability(0.5), 0.000001);
        assertEquals(0.977250685, fDistribution.cumulativeProbability(2), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.735760799, fDistribution.inverseCumulativeProbability(0.6),
            0.000001);
        assertEquals(4.157462554, fDistribution.inverseCumulativeProbability(0.95),
            0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.9), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.9), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        double initialDomain = fDistribution.getInitialDomain(0.5);
        assertTrue(initialDomain > 0 && initialDomain < Double.POSITIVE_INFINITY);

        initialDomain = fDistribution.getInitialDomain(0.9);
        assertTrue(initialDomain > 0 && initialDomain < Double.POSITIVE_INFINITY);
    }

    @Test
    public void testSerialVersionUID() {
        assertNotNull(FDistributionImpl.serialVersionUID);
    }
}