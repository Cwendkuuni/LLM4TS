package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FStudentSDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructor() {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidInput() {
        new FDistributionImpl(-5.0, 10.0);
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidInput2() {
        new FDistributionImpl(5.0, -10.0);
    }

    /**
     * Test method cumulativeProbability.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double x = 2.5;
        double p = fDist.cumulativeProbability(x);
        double expected = Beta.regularizedBeta((5.0 * x) / (10.0 + 5.0 * x),
                0.5 * 5.0, 0.5 * 10.0);
        assertEquals(expected, p, 0.0001);
    }

    /**
     * Test method inverseCumulativeProbability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double x = fDist.inverseCumulativeProbability(p);
        double expected = 5.181152204768362;
        assertEquals(expected, x, 0.0001);
    }

    /**
     * Test method inverseCumulativeProbability with p=0.
     */
    @Test
    public void testInverseCumulativeProbabilityP0() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double p = 0.0;
        double x = fDist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);
    }

    /**
     * Test method inverseCumulativeProbability with p=1.
     */
    @Test
    public void testInverseCumulativeProbabilityP1() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double p = 1.0;
        double x = fDist.inverseCumulativeProbability(p);
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, x, 0.0);
    }

    /**
     * Test method inverseCumulativeProbability with negative p.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityNegativeP() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double p = -0.1;
        fDist.inverseCumulativeProbability(p);
    }

    /**
     * Test method inverseCumulativeProbability with p > 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityPGreaterThan1() throws MathException {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double p = 1.1;
        fDist.inverseCumulativeProbability(p);
    }

    /**
     * Test method setNumeratorDegreesOfFreedom.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double newDegreesOfFreedom = 6.0;
        fDist.setNumeratorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(6.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method setDenominatorDegreesOfFreedom.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(5.0, 10.0);
        double newDegreesOfFreedom = 12.0;
        fDist.setDenominatorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(12.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }
}