package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
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
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertNotNull(distribution);
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        distribution.setDenominatorDegreesOfFreedom(15.0);
        assertEquals(15.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double result = distribution.cumulativeProbability(2.5);
        double expected = Beta.regularizedBeta((5.0 * 2.5) / (10.0 + 5.0 * 2.5),
                0.5 * 5.0,
                0.5 * 10.0);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test cumulativeProbability method with non-positive input.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityNonPositiveInput() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        distribution.cumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double result = distribution.inverseCumulativeProbability(p);
        double expected = (5.0 * result) / (10.0 + 5.0 * result);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test inverseCumulativeProbability method with invalid input.
     */
    @Test(expected = MathRuntimeException.class)
    public void testInverseCumulativeProbabilityInvalidInput() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        distribution.inverseCumulativeProbability(1.5);
    }

    /**
     * Test inverseCumulativeProbability method with p=0 and p=1.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
    }
}