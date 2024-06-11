package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test class for FDistributionImpl.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double result = dist.cumulativeProbability(1.5);
        assertEquals(0.9184333243327549, result, 0.000001);
    }

    /**
     * Test cumulativeProbability method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.cumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double result = dist.inverseCumulativeProbability(0.1);
        assertEquals(1.4388922463444006, result, 0.000001);
    }

    /**
     * Test inverseCumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.inverseCumulativeProbability(1.5);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method with valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalidInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }
}