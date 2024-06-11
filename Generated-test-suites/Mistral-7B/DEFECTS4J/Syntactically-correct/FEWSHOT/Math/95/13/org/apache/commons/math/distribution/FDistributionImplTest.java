package org.apache.commons.math.distribution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(1.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(1.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFDistributionImplNegativeNumeratorDOF() {
        new FDistributionImpl(-1.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testFDistributionImplNegativeDenominatorDOF() {
        new FDistributionImpl(1.0, -1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(0.5, fDist.cumulativeProbability(0.5), 1e-15);
        assertEquals(1.0, fDist.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-15);
        assertEquals(0.0, fDist.cumulativeProbability(Double.NEGATIVE_INFINITY), 1e-15);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityNegativeX() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        fDist.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(0.5, fDist.inverseCumulativeProbability(0.5), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, fDist.inverseCumulativeProbability(1.0), 1e-15);
        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 1e-15);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        fDist.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(0.0, fDist.getDomainLowerBound(0.5), 1e-15);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(Double.MAX_VALUE, fDist.getDomainUpperBound(0.5), 1e-15);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        assertEquals(1.0, fDist.getInitialDomain(0.5), 1e-15);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        fDist.setNumeratorDegreesOfFreedom(2.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(1.0, 1.0);
        fDist.setDenominatorDegreesOfFreedom(2.0);
        assertEquals(2.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testEquals() {
        FDistributionImpl fDist1 = new FDistributionImpl(1.0, 1.0);
        FDistributionImpl fDist2 = new FDistributionImpl(1.0, 1.0);
        assertTrue(fDist1.equals(fDist2));

        FDistributionImpl fDist3 = new FDistributionImpl(2.0, 2.0);
        assertFalse(fDist1.equals(fDist3));

        FDistributionImpl fDist4 = new FDistributionImpl(1.0, 2.0);
        assertFalse(fDist1.equals(fDist4));
    }

    @Test
    public void testHashCode() {
        FDistributionImpl fDist1 = new FDistributionImpl(1.0, 1.0);
        FDistributionImpl fDist2 = new FDistributionImpl(1.0, 1.0);
        assertEquals(fDist1.hashCode(), fDist2.hashCode());

        FDistributionImpl fDist3 = new FDistributionImpl(2.0, 2.0);
        assertNotEquals(fDist1.hashCode(), fDist3.hashCode());

        FDistributionImpl fDist4 = new FDistributionImpl(1.0, 2.0);
        assertNotEquals(fDist1.hashCode(), fDist4.hashCode());
    }
}