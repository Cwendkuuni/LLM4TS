package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
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
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        assertEquals(2.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        assertEquals(0.0, dist.cumulativeProbability(0.0), 0.0);
        double p = 0.5;
        double expected = Beta.regularizedBeta((1.0 * 1.5) / (2.0 + 1.0 * 1.5),
                0.5, 0.5);
        assertEquals(expected, dist.cumulativeProbability(1.5), 1e-12);
        p = 1.0;
        expected = 1.0;
        assertEquals(expected, dist.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
    }

    /**
     * Test cumulativeProbability method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.cumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInputs() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double p = 0.0;
        assertEquals(0.0, dist.inverseCumulativeProbability(p), 0.0);
        p = 0.5;
        double expected = 1.5;
        assertEquals(expected, dist.inverseCumulativeProbability(p), 1e-12);
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, dist.inverseCumulativeProbability(p), 0.0);
    }

    /**
     * Test inverseCumulativeProbability method with invalid inputs.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInputs() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.inverseCumulativeProbability(-1.0);
        dist.inverseCumulativeProbability(1.1);
    }

    /**
     * Test getDomainLowerBound method with valid inputs.
     */
    @Test
    public void testGetDomainLowerBoundValidInputs() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double p = 0.0;
        assertEquals(0.0, dist.getDomainLowerBound(p), 0.0);
        p = 0.5;
        assertEquals(Double.NEGATIVE_INFINITY, dist.getDomainLowerBound(p), 0.0);
        p = 1.0;
        assertEquals(Double.POSITIVE_INFINITY, dist.getDomainLowerBound(p), 0.0);
    }

    /**
     * Test getDomainUpperBound method with valid inputs.
     */
    @Test
    public void testGetDomainUpperBoundValidInputs() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double p = 0.0;
        assertEquals(Double.POSITIVE_INFINITY, dist.getDomainUpperBound(p), 0.0);
        p = 0.5;
        assertEquals(Double.POSITIVE_INFINITY, dist.getDomainUpperBound(p), 0.0);
        p = 1.0;
        assertEquals(0.0, dist.getDomainUpperBound(p), 0.0);
    }

    /**
     * Test getInitialDomain method with valid inputs.
     */
    @Test
    public void testGetInitialDomainValidInputs() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        double p = 0.5;
        double expected = 2.0;
        assertEquals(expected, dist.getInitialDomain(p), 0.0);
    }

    /**
     * Test setNumeratorDegreesOfFreedom with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegativeInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegativeInput() {
        FDistribution dist = new FDistributionImpl(1.0, 2.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        FDistribution dist1 = new FDistributionImpl(1.0, 2.0);
        FDistribution dist2 = new FDistributionImpl(1.0, 2.0);
        FDistribution dist3 = new FDistributionImpl(2.0, 2.0);
        RealDistribution dist4 = new NormalDistribution(0.0, 1.0);
        assertTrue(dist1.equals(dist1));
        assertTrue(dist1.equals(dist2));
        assertFalse(dist1.equals(dist3));
        assertFalse(dist1.equals(dist4));
    }
}