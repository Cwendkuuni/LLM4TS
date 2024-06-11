package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testFDistributionImplConstruction() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test {@link FDistributionImpl#cumulativeProbability(double)} method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        double expectedResult = Beta.regularizedBeta((2.0 * p) / (4.0 + 2.0 * p), 1.0, 2.0);
        double result = distribution.cumulativeProbability(p);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#cumulativeProbability(double)} method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double x = -1.0;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test {@link FDistributionImpl#inverseCumulativeProbability(double)} method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        double expectedResult = Beta.incompleteBeta(1.5, 2.0) / Beta.beta(1.5, 2.0);
        double result = distribution.inverseCumulativeProbability(p);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#inverseCumulativeProbability(double)} method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 1.5;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test {@link FDistributionImpl#getDomainLowerBound(double)} method.
     */
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        assertEquals(0.0, distribution.getDomainLowerBound(p), 0.001);
    }

    /**
     * Test {@link FDistributionImpl#getDomainUpperBound(double)} method.
     */
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(p), 0.001);
    }

    /**
     * Test {@link FDistributionImpl#getInitialDomain(double)} method.
     */
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        double expectedResult = 4.0 / (4.0 - 2.0);
        double result = distribution.getInitialDomain(p);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} method with valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInput() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = 5.0;
        distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidInput() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = -1.0;
        distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
    }

    /**
     * Test {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} method with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInput() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = 5.0;
        distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        assertEquals(degreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalidInput() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 4.0);
        double degreesOfFreedom = -1.0;
        distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
    }
}