package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_ValidConstruction() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability_ValidInput() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double x = 1.5;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.9356748081758359, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbability_InvalidInput() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        assertThrows(MathException.class, () -> fDist.cumulativeProbability(-1.0));
    }

    @Test
    public void testInverseCumulativeProbability_ValidInput() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double p = 0.95;
        double inverseCumulativeProbability = fDist.inverseCumulativeProbability(p);
        assertEquals(1.4499355925138517, inverseCumulativeProbability, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability_InvalidInput_Zero() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double p = 0.0;
        double inverseCumulativeProbability = fDist.inverseCumulativeProbability(p);
        assertEquals(0.0, inverseCumulativeProbability, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability_InvalidInput_One() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double p = 1.0;
        double inverseCumulativeProbability = fDist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability);
    }

    @Test
    public void testInverseCumulativeProbability_InvalidInput_Negative() throws MathException {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double p = -0.1;
        assertThrows(MathException.class, () -> fDist.inverseCumulativeProbability(p));
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newDegreesOfFreedom = 5.0;
        fDist.setNumeratorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(newDegreesOfFreedom, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_Negative() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newDegreesOfFreedom = -1.0;
        fDist.setNumeratorDegreesOfFreedom(newDegreesOfFreedom);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newDegreesOfFreedom = 5.0;
        fDist.setDenominatorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(newDegreesOfFreedom, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom_Negative() {
        FDistribution fDist = new FDistributionImpl(1.0, 2.0);
        double newDegreesOfFreedom = -1.0;
        fDist.setDenominatorDegreesOfFreedom(newDegreesOfFreedom);
    }

}