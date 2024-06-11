package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {
    @Test
    public void testFDistributionImpl() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertThrows(MathRuntimeException.class, () -> fd.cumulativeProbability(-1.0));
    }

    @Test
    public void testCumulativeProbabilityZeroX() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testCumulativeProbabilityPositiveX() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        double p = 0.75;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(x, fd.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1.0), 0.0);
    }

    @Test
    public void testInverseCumulativeProbabilityInvalidProbability()
            throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertThrows(MathIllegalArgumentException.class,
                () -> fd.inverseCumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbabilityValidProbability()
            throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        double p = 0.25;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(x, fd.cumulativeProbability(x), 0.001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(0.0, fd.getDomainLowerBound(0.25), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        assertEquals(Double.MAX_VALUE, fd.getDomainUpperBound(0.25), 0.0);
    }

    @Test
    public void testGetInitialDomain() throws MathException {
        FDistribution fd = new FDistributionImpl(3.0, 4.0);
        double p = 0.25;
        double x = fd.inverseCumulativeProbability(p);
        assertEquals(Beta.regularizedBeta(3.0 * x / (4.0 + 3.0 * x),
                0.5 * 3.0, 0.5 * 4.0), fd.getInitialDomain(p), 0.001);
    }
}