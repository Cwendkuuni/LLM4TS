package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {
    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(5, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        fDistribution.setNumeratorDegreesOfFreedom(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        fDistribution.setDenominatorDegreesOfFreedom(-10);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        assertEquals(0.0, fDistribution.cumulativeProbability(0), 0);
        assertEquals(0.1558305665055582, fDistribution.cumulativeProbability(1), 0.00000000000001);
        assertEquals(0.28785505959185003, fDistribution.cumulativeProbability(2), 0.00000000000001);
        assertEquals(0.3987978394605398, fDistribution.cumulativeProbability(3), 0.00000000000001);
        assertEquals(0.5023302273788545, fDistribution.cumulativeProbability(4), 0.00000000000001);
        assertEquals(0.6036797736334238, fDistribution.cumulativeProbability(5), 0.00000000000001);
        assertEquals(0.6907752449435828, fDistribution.cumulativeProbability(6), 0.00000000000001);
        assertEquals(0.7731930956199384, fDistribution.cumulativeProbability(7), 0.00000000000001);
        assertEquals(0.8499975369455422, fDistribution.cumulativeProbability(8), 0.00000000000001);
        assertEquals(0.9204168020852892, fDistribution.cumulativeProbability(9), 0.00000000000001);
        assertEquals(0.9846809905181125, fDistribution.cumulativeProbability(10), 0.00000000000001);
        assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(14.449375873375503, fDistribution.inverseCumulativeProbability(0.5), 0.00000000000001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0.0, fDistribution.getDomainLowerBound(0), 0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0);
        assertEquals(0.0, fDistribution.getDomainLowerBound(1), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertTrue(Double.POSITIVE_INFINITY > fDistribution.getDomainUpperBound(0));
        assertTrue(Double.POSITIVE_INFINITY > fDistribution.getDomainUpperBound(0.5));
        assertTrue(Double.POSITIVE_INFINITY > fDistribution.getDomainUpperBound(1));
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(5.0 / 3.0, fDistribution.getInitialDomain(0), 0);
        assertEquals(3.333333333333333, fDistribution.getInitialDomain(0.5), 0.00000000000001);
        assertEquals(3.333333333333333, fDistribution.getInitialDomain(1), 0);
    }
}