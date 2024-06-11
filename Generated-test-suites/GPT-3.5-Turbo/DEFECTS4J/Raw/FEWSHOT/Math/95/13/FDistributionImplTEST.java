package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class FDistributionImplTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testCumulativeProbability() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        assertEquals(0.5396039603960396, dist.cumulativeProbability(2), EPSILON);
        assertEquals(0.8181818181818181, dist.cumulativeProbability(4), EPSILON);
        assertEquals(0.9696969696969697, dist.cumulativeProbability(8), EPSILON);
        assertEquals(1.0, dist.cumulativeProbability(Double.POSITIVE_INFINITY), EPSILON);
        assertEquals(1.0, dist.cumulativeProbability(100), EPSILON);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2, 3);
        dist.cumulativeProbability(-1);
    }

    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl dist = new FDistributionImpl(3, 5);
        assertEquals(0.1282410501427191, dist.inverseCumulativeProbability(0.1), EPSILON);
        assertEquals(0.21181702309058604, dist.inverseCumulativeProbability(0.2), EPSILON);
        assertEquals(0.3864823202850397, dist.inverseCumulativeProbability(0.5), EPSILON);
        assertEquals(0.6418541631872285, dist.inverseCumulativeProbability(0.8), EPSILON);
        assertTrue(Double.isInfinite(dist.inverseCumulativeProbability(1)));
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2, 3);
        dist.inverseCumulativeProbability(-1);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl dist = new FDistributionImpl(4, 5);
        assertEquals(0.0, dist.getDomainLowerBound(0.1), EPSILON);
        assertEquals(0.0, dist.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0.0, dist.getDomainLowerBound(0.9), EPSILON);
        assertEquals(0.0, dist.getDomainLowerBound(1.0), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl dist = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.1), EPSILON);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.5), EPSILON);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.9), EPSILON);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(1.0), EPSILON);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl dist = new FDistributionImpl(5, 2);
        assertEquals(2.5, dist.getInitialDomain(0.1), EPSILON);
        assertEquals(2.5, dist.getInitialDomain(0.5), EPSILON);
        assertEquals(2.5, dist.getInitialDomain(0.9), EPSILON);
        assertEquals(2.5, dist.getInitialDomain(1.0), EPSILON);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setNumeratorDegreesOfFreedom(5);
        assertEquals(5, dist.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomZero() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setNumeratorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setNumeratorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(5, 3);
        assertEquals(5, dist.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setDenominatorDegreesOfFreedom(5);
        assertEquals(5, dist.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomZero() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setDenominatorDegreesOfFreedom(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistributionImpl dist = new FDistributionImpl(3, 4);
        dist.setDenominatorDegreesOfFreedom(-1);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(5, 3);
        assertEquals(3, dist.getDenominatorDegreesOfFreedom(), EPSILON);
    }
}