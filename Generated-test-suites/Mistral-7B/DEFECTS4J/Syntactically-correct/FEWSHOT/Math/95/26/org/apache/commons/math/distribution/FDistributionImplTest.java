package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FShape;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double result;

        result = distribution.cumulativeProbability(0.5);
        assertEquals(0.520509774935163, result, 0.00001);

        result = distribution.cumulativeProbability(1.0);
        assertEquals(1.0, result, 0.00001);

        result = distribution.cumulativeProbability(-1.0);
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p, result;

        p = 0.1;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(0.660469179102451, result, 0.00001);

        p = 0.95;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(3.30226289811915, result, 0.00001);

        p = 1.1;
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(p));

        p = 0.0;
        result = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, result, 0.00001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        double result = distribution.getDomainLowerBound(p);
        assertEquals(0.0, result, 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        double result = distribution.getDomainUpperBound(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        double p = 0.5;
        double result = distribution.getInitialDomain(p);
        assertEquals(1.5, result, 0.00001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegative() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegative() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}