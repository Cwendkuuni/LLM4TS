package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_constructor() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImpl_constructor_illegalArguments() {
        new FDistributionImpl(-1.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionImpl_constructor_negativeDenominator() {
        new FDistributionImpl(1.0, -2.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);

        assertEquals(0.0, fd.cumulativeProbability(0.0), 0.001);
        assertEquals(0.5, fd.cumulativeProbability(1.0), 0.001);
        assertEquals(1.0, fd.cumulativeProbability(Double.POSITIVE_INFINITY), 0.001);

        assertEquals(0.9, fd.cumulativeProbability(2.5), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_outOfBounds() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        fd.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);

        assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.001);
        assertEquals(1.0, fd.inverseCumulativeProbability(1.0), 0.001);

        assertEquals(2.5, fd.inverseCumulativeProbability(0.9), 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_outOfBounds() throws MathException {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        fd.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testGettersAndSetters() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);

        assertEquals(1.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(2.0, fd.getDenominatorDegreesOfFreedom(), 0.001);

        fd.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.001);

        fd.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetters_illegalArguments() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetters_negativeDenominator() {
        FDistribution fd = new FDistributionImpl(1.0, 2.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }
}